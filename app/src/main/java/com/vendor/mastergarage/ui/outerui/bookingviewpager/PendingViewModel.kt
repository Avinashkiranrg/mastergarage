package com.vendor.mastergarage.ui.outerui.bookingviewpager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.*
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil.Companion.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val leadsLiveData = MutableLiveData<Response<Leads>>()

    val leads: LiveData<Response<Leads>>
        get() = leadsLiveData

    private val pendingLiveData = MutableLiveData<Response<OnGoingRespo>>()

    val pendingData: LiveData<Response<OnGoingRespo>>
        get() = pendingLiveData

    private val addLiveString = MutableLiveData<Response<UpdateStatus>>()

    val liveData: LiveData<Response<UpdateStatus>>
        get() = addLiveString

    private val declineLiveString = MutableLiveData<Response<UploadResponse>>()

    val onDecline: LiveData<Response<UploadResponse>>
        get() = declineLiveString


    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun getStoredVendorObject(): Vendors? {
        return repository.getStoredVendorObject()
    }

    init {
        getStoredOutletObject()?.let { it.outletId?.let { it1 -> getLeads(it1) } }
    }

    private fun getLeads(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isInternetAvailable(context)) {
                leadsLiveData.postValue(Response.Loading())
                val result = repository.getLeads(outletId)
                if (result.body() != null) {
                    leadsLiveData.postValue(Response.Success(result.body()))
                } else {
                    leadsLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                leadsLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            leadsLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    private fun getPending(vendorId: String,action:String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isInternetAvailable(context)) {
                pendingLiveData.postValue(Response.Loading())
                val result = repository.getPendingData(vendorId,action)
                if (result.body() != null) {
                    pendingLiveData.postValue(Response.Success(result.body()))
                } else {
                    pendingLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                pendingLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            pendingLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }
    fun setOnGoing(
        last_up_date: String,
        last_up_time: String,
        booking_date: String,
        booking_time: String,
        outletId: Int,
        vehicleId: Int,
        addressId: Int,
        leadId: Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isInternetAvailable(context)) {
                    addLiveString.postValue(Response.Loading())
                    val result = repository.setOnGoing(
                        last_up_date,
                        last_up_time,
                        booking_date,
                        booking_time,
                        outletId,
                        vehicleId,
                        addressId,
                        leadId
                    )
                    if (result.body() != null) {
                        addLiveString.postValue(Response.Success(result.body()))
                    } else {
                        addLiveString.postValue(Response.Failure("api error"))
                    }
                } else {
                    addLiveString.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                addLiveString.postValue(Response.Failure(e.message.toString()))
            }
        }

    fun declineLeads(leadId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isInternetAvailable(context)) {
                    declineLiveString.postValue(Response.Loading())
                    val result = repository.declineLeads(
                        leadId
                    )
                    if (result.body() != null) {
                        declineLiveString.postValue(Response.Success(result.body()))
                    } else {
                        declineLiveString.postValue(Response.Failure("api error"))
                    }
                } else {
                    declineLiveString.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                declineLiveString.postValue(Response.Failure(e.message.toString()))
            }
        }

   /* fun loadUi() {
        getStoredOutletObject()?.let { it.outletId?.let { it1 -> getLeads(it1) } }
    }*/

    fun loadUi(vendorId: String, s: String) {
        getPending(vendorId,s)
    }
}