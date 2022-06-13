package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.team

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.Drivers
import com.vendor.mastergarage.model.OutletsItem
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
class DriverFragViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val driverLiveData = MutableLiveData<Response<Drivers>>()

    val driver: LiveData<Response<Drivers>>
        get() = driverLiveData

    private val deleteLiveData = MutableLiveData<Response<UploadResponse>>()

    val delete: LiveData<Response<UploadResponse>>
        get() = deleteLiveData

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

//    init {
//        getStoredOutletObject()?.let { getDrivers(it.outletId!!) }
//    }

    fun getDrivers(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                driverLiveData.postValue(Response.Loading())
                val result = repository.getDrivers(outletId)
                if (result.body() != null) {
                    driverLiveData.postValue(Response.Success(result.body()))
                } else {
                    driverLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                driverLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            driverLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun deleteDriver(driverId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                deleteLiveData.postValue(Response.Loading())
                val result = repository.deleteDriver(
                    driverId
                )
                if (result.body() != null) {
                    deleteLiveData.postValue(Response.Success(result.body()))
                } else {
                    deleteLiveData.postValue(Response.Failure("Failed"))
                }
            } else {
                deleteLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            deleteLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun loadUi() {
        getStoredOutletObject()?.let { getDrivers(it.outletId!!) }
    }

}