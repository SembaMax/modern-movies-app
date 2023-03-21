package com.semba.modernmoviesapp.data.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.semba.modernmoviesapp.data.remote.BuildConfig
import com.semba.modernmoviesapp.data.remote.network.MoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor())
            .addInterceptor(loggingInterceptor())
            .build()
    }

    private fun authorizationInterceptor() = Interceptor {
        val url: HttpUrl = it.request().url
            .newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()
        val request: Request = it.request().newBuilder().url(url).build()
        it.proceed(request)
    }

    private fun loggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    @Singleton
    fun providesRetrofitService(okHttpClient: OkHttpClient): MoviesApiService = apiService(MoviesApiService.BASE_URL.toHttpUrl(), okHttpClient)
}

fun apiService(baseUrl: HttpUrl, okHttpClient: OkHttpClient): MoviesApiService
{
    val json = Json {
        ignoreUnknownKeys = true
    }

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(MoviesApiService::class.java)
}