package tv.freenet.selfcare.services.network

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {

    private val original by lazy {
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> =
        RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>)

    private class RxCallAdapterWrapper<R>(val retrofit: Retrofit, val wrapped: CallAdapter<R, *>) : CallAdapter<R, Any> {

        override fun adapt(call: Call<R>): Any {

            val adapted = wrapped.adapt(call)

            if (adapted is Completable) {
                return adapted.onErrorResumeNext { Completable.error(it.asRetrofitException()) }
            }

            if (adapted is Single<*>) {
                return adapted.onErrorResumeNext { Single.error(it.asRetrofitException()) }
            }


            if (adapted is Observable<*>) {
                return adapted.onErrorResumeNext { throwable: Throwable -> Observable.error(throwable.asRetrofitException()) }
            }

            throw RuntimeException("Observable Type not supported")
        }

        override fun responseType(): Type = wrapped.responseType()

        private fun Throwable.asRetrofitException(): RetrofitException = when (this) {

            // We had non-200 http error
            is HttpException -> {
                val response = response()
                RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit)
            }

            // A network error happened
            is IOException -> RetrofitException.networkError(this)

            // We don't know what happened. We need to simply convert to an unknown error
            else -> RetrofitException.unexpectedError(this)
        }
    }

    companion object {
        fun create(): CallAdapter.Factory = RxErrorHandlingCallAdapterFactory()
    }
}