package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.contact

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.*
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactUsFragViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val contactLiveData = MutableLiveData<Response<ContactusItem>>()

    val contact: LiveData<Response<ContactusItem>>
        get() = contactLiveData

    init {
        getContactus()
    }

    private fun getContactus() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                contactLiveData.postValue(Response.Loading())
                val result = repository.getContactus()
                if (result.body() != null) {
                    contactLiveData.postValue(Response.Success(result.body()))
                } else {
                    contactLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                contactLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            contactLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

}