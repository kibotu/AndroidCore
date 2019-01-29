package com.exozet.android.core.services.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import tv.freenet.selfcare.models.gms.FcmMessage

interface GoogleApi {

    /**
     * https://developers.google.com/cloud-messaging/http
     */
    @Headers("Content-Type: application/json")
    @POST("/gcm/send")
    fun sendPushNotification(@Body message: FcmMessage, @Header("Authorization") apiKey: String): Observable<ResponseBody>
}