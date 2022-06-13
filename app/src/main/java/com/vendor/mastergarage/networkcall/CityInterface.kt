package com.vendor.mastergarage.networkcall

import com.vendor.mastergarage.model.City
import retrofit2.Response
import retrofit2.http.GET

interface CityInterface {

    @GET("cities.json")
    suspend fun getCity(): Response<City>

}