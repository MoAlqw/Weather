package com.example.dagger2.model.weather.retrofit

import com.example.dagger2.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = BuildConfig.token

        val newUrl = original.url.newBuilder()
            .addQueryParameter("key", token)
            .build()
        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}