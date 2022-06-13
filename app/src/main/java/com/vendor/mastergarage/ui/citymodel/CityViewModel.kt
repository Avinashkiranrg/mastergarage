package com.vendor.mastergarage.ui.citymodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.City
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CityViewModel  @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application){


    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCity()
        }
    }

    private val cityLiveData = MutableLiveData<Response<City>>()

    val cityLive: LiveData<Response<City>>
        get() = cityLiveData

    private suspend fun getCity() {
        try {
            val result = repository.getCity()
            if (result.body() != null) {
                cityLiveData.postValue(Response.Success(result.body()))
            } else {
                cityLiveData.postValue(Response.Failure("api error"))
            }
        } catch (e: Exception) {
            cityLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }
}