package com.vendor.mastergarage.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.CountryCode
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCountry()
        }
    }

    private val countryLiveData = MutableLiveData<Response<CountryCode>>()

    val countrys: LiveData<Response<CountryCode>>
        get() = countryLiveData

    private suspend fun getCountry() {
        try {
            val result = repository.getCountry()
            if (result.body() != null) {
                countryLiveData.postValue(Response.Success(result.body()))
            } else {
                countryLiveData.postValue(Response.Failure("api error"))
            }
        } catch (e: Exception) {
            countryLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

}