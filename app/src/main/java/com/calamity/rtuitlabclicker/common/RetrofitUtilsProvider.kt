package com.calamity.rtuitlabclicker.common

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class RetrofitUtilsProvider {
    companion object {
        fun getLoggingInterceptor(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            return httpClient.build()
        }

    }
}