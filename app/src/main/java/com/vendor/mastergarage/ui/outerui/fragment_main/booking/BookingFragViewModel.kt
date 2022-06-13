package com.vendor.mastergarage.ui.outerui.fragment_main.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.TotalRequests
import com.vendor.mastergarage.model.Vendors
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingFragViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val vendorLiveData = MutableLiveData<Response<Vendors>>()

    val vendor: LiveData<Response<Vendors>>
        get() = vendorLiveData

    private val allRequest = MutableLiveData<Response<TotalRequests>>()

    val total: LiveData<Response<TotalRequests>>
        get() = allRequest

    private fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    init {
        getStoredOutletObject()?.outletId?.let { getCounts(it) }
    }

    fun getVendor(phone: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                val result = repository.getVendor(phone)
                if (result.body() != null) {
                    vendorLiveData.postValue(Response.Success(result.body()))
                } else {
                    vendorLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                vendorLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            vendorLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getCounts(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                val result = repository.getCounts(outletId)
                if (result.body() != null) {
                    allRequest.postValue(Response.Success(result.body()))
                } else {
                    allRequest.postValue(Response.Failure("api error"))
                }
            } else {
                allRequest.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            allRequest.postValue(Response.Failure(e.message.toString()))
        }
    }


}