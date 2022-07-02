package com.vendor.mastergarage.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.CountryCode
import com.vendor.mastergarage.model.SignUpRespon
import com.vendor.mastergarage.model.VerifyOtpRespon
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
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

    private val signUpLiveData = MutableLiveData<Response<SignUpRespon>>()

    val signUpData : LiveData<Response<SignUpRespon>>
    get() = signUpLiveData

    private val verifyOtpLiveData = MutableLiveData<Response<VerifyOtpRespon>>()

    val verifyOtpData : LiveData<Response<VerifyOtpRespon>>
    get() = verifyOtpLiveData

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

     fun signUp (mobileNumber : String) = viewModelScope.launch(Dispatchers.IO){

        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                val result = repository.signUp(mobileNumber)
                if (result.body() != null) {
                    signUpLiveData.postValue(Response.Success(result.body()))
                } else {
                    signUpLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                signUpLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            signUpLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun verifyOtp (mobileNumber : String,Otp : String) = viewModelScope.launch(Dispatchers.IO){

        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                val result = repository.verifyOtp(mobileNumber,Otp)
                if (result.body() != null) {
                    verifyOtpLiveData.postValue(Response.Success(result.body()))
                } else {
                    verifyOtpLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                verifyOtpLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            verifyOtpLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }
}