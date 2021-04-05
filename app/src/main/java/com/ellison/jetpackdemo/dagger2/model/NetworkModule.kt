package com.ellison.jetpackdemo.dagger2.model

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideLoginService(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): MovieService {
        return Retrofit.Builder()
                .baseUrl("http://omdbapi.com/")
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
                .create(MovieService::class.java)
    }
}