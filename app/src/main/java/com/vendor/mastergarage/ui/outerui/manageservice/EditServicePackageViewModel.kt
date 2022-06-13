package com.vendor.mastergarage.ui.outerui.manageservice

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.ServiceList
import com.vendor.mastergarage.model.ServicePackageProvide
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
class EditServicePackageViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val packageLiveData = MutableLiveData<Response<ServicePackageProvide>>()

    val packageProvideItem: LiveData<Response<ServicePackageProvide>>
        get() = packageLiveData

    private val isOnOffLive = MutableLiveData<Response<UploadResponse>>()

    val isOnOff: LiveData<Response<UploadResponse>>
        get() = isOnOffLive

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun getServicePackageProvide(outletId: Int, serviceId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                packageLiveData.postValue(Response.Loading())
                val result = repository.getServicePackageProvide(outletId, serviceId)
                if (result.body() != null) {
                    packageLiveData.postValue(Response.Success(result.body()))
                }
            } else {
                packageLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            packageLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun disablePackage(vpId: Int, outletId: Int, packageId: Int, flag: String) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    isOnOffLive.postValue(Response.Loading())
                    val result = repository.disablePackage(vpId, outletId, packageId, flag)
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

    fun loadUi(serviceId: Int) {
        getStoredOutletObject()?.outletId?.let { getServicePackageProvide(it, serviceId) }
    }

    private val serviceLiveData = MutableLiveData<Response<ServiceList>>()

    val service: LiveData<Response<ServiceList>>
        get() = serviceLiveData

    fun getServiceProvide(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
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

}