package com.zibro.fooddeliveryapp.di

import com.zibro.fooddeliveryapp.BuildConfig
import com.zibro.fooddeliveryapp.data.network.MapApiService
import com.zibro.fooddeliveryapp.data.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideMapApiService(retrofit: Retrofit) : MapApiService{
    return retrofit.create(MapApiService::class.java)
}
fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit{
    return Retrofit.Builder()
        .baseUrl(Url.TMAP_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideGsonConvertFactory() : GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun buildOkHttpClient() : OkHttpClient{
    val interceptor = HttpLoggingInterceptor()
    if(BuildConfig.DEBUG){
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }else{
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}