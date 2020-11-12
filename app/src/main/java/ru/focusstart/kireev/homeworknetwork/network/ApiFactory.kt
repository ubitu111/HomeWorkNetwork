package ru.focusstart.kireev.homeworknetwork.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory private constructor() {
    companion object {
        private var apiFactory: ApiFactory? = null
        private lateinit var retrofit: Retrofit
        private const val BASE_UPLOAD_PHOTO_URL = "https://api.imgur.com/"

        fun getInstance(): ApiFactory {
            apiFactory?.let { return it }
            val instance = ApiFactory()
            apiFactory = instance
            return instance
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_UPLOAD_PHOTO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
}