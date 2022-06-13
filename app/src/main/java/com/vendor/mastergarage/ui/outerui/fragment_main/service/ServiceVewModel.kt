package com.vendor.mastergarage.ui.outerui.fragment_main.service

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.ServiceList
import com.vendor.mastergarage.model.UploadResponse
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceVewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val serviceLiveData = MutableLiveData<Response<ServiceList>>()

    val service: LiveData<Response<ServiceList>>
        get() = serviceLiveData

    private val isOnOffLive = MutableLiveData<Response<UploadResponse>>()

    val isOnOff: LiveData<Response<UploadResponse>>
        get() = isOnOffLive

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    init {
        getStoredOutletObject()?.outletId?.let { getServiceProvide(it) }
    }

    private fun getServiceProvide(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                serviceLiveData.postValue(Response.Loading())
                val result = repository.getServiceProvide(outletId)
                if (result.body() != null) {
                    serviceLiveData.postValue(Response.Success(result.body()))
                } else {
                    serviceLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                serviceLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            serviceLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun disableService(vsId: Int, outletId: Int, serviceId: Int, flag: String) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    isOnOffLive.postValue(Response.Loading())
                    val result = repository.disableService(vsId, outletId, serviceId, flag)
                    if (result.body() != null) {
                        isOnOffLive.postValue(Response.Success(result.body()))
                    }
                } else {
                    isOnOffLive.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                isOnOffLive.postValue(Response.Failure(e.message.toString()))
            }
        }

    fun loadUi() {
        getStoredOutletObject()?.outletId?.let { getServiceProvide(it) }
    }

}