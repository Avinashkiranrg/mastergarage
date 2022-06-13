package com.vendor.mastergarage.ui.outerui.manageservice

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.UploadResponse
import com.vendor.mastergarage.model.VehicleProvide
import com.vendor.mastergarage.model.Vehicles
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseVehicleViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val vehicleLiveData = MutableLiveData<Response<VehicleProvide>>()

    val vehicles: LiveData<Response<VehicleProvide>>
        get() = vehicleLiveData

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun loadUi() {
        getStoredOutletObject()?.outletId?.let { getVehicles(it) }
    }

    private fun getVehicles(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                vehicleLiveData.postValue(Response.Loading())
                val result = repository.getVehicles(outletId)
                if (result.body() != null) {
                    vehicleLiveData.postValue(Response.Success(result.body()))
                }
            } else {
                vehicleLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            vehicleLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    private val isOnOffLive = MutableLiveData<Response<UploadResponse>>()

    val isOnOff: LiveData<Response<UploadResponse>>
        get() = isOnOffLive

    fun disableVehicle(vsId: Int, flag: String) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    isOnOffLive.postValue(Response.Loading())
                    val result = repository.disableVehicle(vsId, flag)
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

}