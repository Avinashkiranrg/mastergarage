package com.vendor.mastergarage.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vendor.mastergarage.networkcall.ApiInterface
import com.vendor.mastergarage.networkcall.CityInterface
import com.vendor.mastergarage.networkcall.CountryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        const val FLAG_URL = "https://purecatamphetamine.github.io/country-flag-icons/3x2/"
//        private const val BASE_URL = "http://192.168.129.22/garage/"
        private const val BASE_URL = "http://mastergarage.in/garageapi/"

        private const val COUNTRY_URL =
            "https://gist.githubusercontent.com/DmytroLisitsyn/1c31186e5b66f1d6c52da6b5c70b12ad/raw/01b1af9b267471818f4f8367852bd4a2814cbae6/"
        private const val CITY_URL =
            "https://raw.githubusercontent.com/nshntarora/Indian-Cities-JSON/master/"
    }


    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Named(BASE_URL)
    fun provideUrl1() = BASE_URL

    private fun add(): OkHttpClient {
        val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
        return client

    }

    @Provides
    @Singleton
    fun provideApiService1(@Named(BASE_URL) url: String): ApiInterface =
        Retrofit.Builder()
            .baseUrl(url)
            .client(add())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiInterface::class.java)

    @Provides
    @Named(CITY_URL)
    fun provideUrl2() = CITY_URL

    @Provides
    @Singleton
    fun provideApiService2(@Named(CITY_URL) url: String): CityInterface =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CityInterface::class.java)

    @Provides
    @Named(COUNTRY_URL)
    fun provideUrl3() = COUNTRY_URL

    @Provides
    @Singleton
    fun provideApiService3(@Named(COUNTRY_URL) url: String): CountryInterface =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryInterface::class.java)

}
