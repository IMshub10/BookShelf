package com.summer.bookshelf.networking.remotes

import com.summer.bookshelf.networking.APIConstants.BOOK_KEEPER_BASE_URL
import com.summer.bookshelf.networking.services.BookService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

object BookServiceMaker {
    operator fun invoke(): BookService {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headers = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .header("Content-Type", "application/json")
                .addHeader("Connection", "close")
                .method(original.method, original.body)
            chain.proceed(builder.build())
        }

        val okHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            addInterceptor(headers)
            addInterceptor(logger)
            hostnameVerifier { _, _ -> true }
        }.build()

        return Retrofit.Builder().client(okHttpClient).baseUrl(BOOK_KEEPER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(BookService::class.java)
    }
}