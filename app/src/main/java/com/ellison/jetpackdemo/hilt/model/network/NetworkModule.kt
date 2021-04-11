package com.ellison.jetpackdemo.hilt.model.network

import com.ellison.jetpackdemo.hilt.model.interceptor.CallServerInterceptorOkHttpClient
import com.ellison.jetpackdemo.hilt.model.interceptor.LoggingInterceptorOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.internal.http.CallServerInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {
    @LoggingInterceptorOkHttpClient
    @Provides
    fun provideLoggingInterceptorOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @CallServerInterceptorOkHttpClient
    @Provides
    fun provideCallServerInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(CallServerInterceptor(false)).build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideMovieService(@LoggingInterceptorOkHttpClient okHttpClient: OkHttpClient,
                            gsonConverterFactory: GsonConverterFactory): NetworkService {
        return Retrofit.Builder()
                .baseUrl("http://omdbapi.com/")
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
                .create(NetworkService::class.java)
    }
}