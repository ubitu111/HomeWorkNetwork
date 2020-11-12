package ru.focusstart.kireev.homeworknetwork.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("3/upload")
    fun postImage(
        @Header("Authorization") token: String,
        @Part name: MultipartBody.Part,
        @Part title: MultipartBody.Part,
        @Part description: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ): Call<ImageResponse>
}