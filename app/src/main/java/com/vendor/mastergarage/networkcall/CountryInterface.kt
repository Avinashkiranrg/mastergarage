package com.vendor.mastergarage.networkcall

import com.vendor.mastergarage.model.CountryCode
import retrofit2.Response
import retrofit2.http.GET

interface CountryInterface {

    @GET("country_dial_info.json")
    suspend fun getCountry(): Response<CountryCode>

}