package tv.freenet.selfcare.services.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class RetrofitException(
    message: String?,
    exception: Throwable?,
    /**
     * The request URL which produced the error.
     */
    val url: String?,
    /**
     * Response object containing status code, headers, body, etc
     */
    val response: Response<*>?,
    /**
     * The event kind which triggered this error.
     */
    val kind: Kind,
    /**
     * The Retrofit this request was executed on
     */
    val retrofit: Retrofit?
) : RuntimeException(message, exception) {

    companion object {
        fun httpError(url: String, response: Response<*>, retrofit: Retrofit): RetrofitException =
            RetrofitException(
                "${response.code()} ${response.message()}",
                null,
                url,
                response,
                Kind.HTTP,
                retrofit
            )

        fun networkError(exception: IOException): RetrofitException =
            RetrofitException(exception.message, exception, null, null, Kind.NETWORK, null)

        fun unexpectedError(exception: Throwable): RetrofitException =
            RetrofitException(exception.message, exception, null, null, Kind.UNEXPECTED, null)
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        if (response?.errorBody() == null || retrofit == null) {
            return null
        }
        val converter: Converter<ResponseBody, T> = retrofit.responseBodyConverter(type, arrayOfNulls<Annotation>(0))
        return converter.convert(response.errorBody() ?: return null)
    }

    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        HTTP,
        HTTP_422_WITH_DATA,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }
}

val Throwable.retrofitException: RetrofitException
    get() = RetrofitException.unexpectedError(this)

fun String.asRetrofitException(): Throwable = Throwable(this).retrofitException