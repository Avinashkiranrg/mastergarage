package com.vendor.mastergarage.ui.outerui.viewdetailsviewpager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class VehicleInfoViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val leadsLiveData = MutableLiveData<Response<LeadItemData>>()

    val leads: LiveData<Response<LeadItemData>>
        get() = leadsLiveData

    private val addLiveString = MutableLiveData<Response<UpdateStatus>>()

    val liveData: LiveData<Response<UpdateStatus>>
        get() = addLiveString


    private fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun getLeadsById(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                leadsLiveData.postValue(Response.Loading())
                val result = repository.getLeadsById(leadId)
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

    private val ownerDetailLiveUpdate = MutableLiveData<Response<OwnerInfo>>()

    val ownerDetailLive: LiveData<Response<OwnerInfo>>
        get() = ownerDetailLiveUpdate

    fun getOwnerDetails(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                ownerDetailLiveUpdate.postValue(Response.Loading())
                val result = repository.getOwnerDetails(
                    leadId
                )
                if (result.body() != null) {
                    ownerDetailLiveUpdate.postValue(Response.Success(result.body()))
                } else {
                    ownerDetailLiveUpdate.postValue(Response.Failure("api error"))
                }
            } else {
                ownerDetailLiveUpdate.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            ownerDetailLiveUpdate.postValue(Response.Failure(e.message.toString()))
        }
    }


    private val userAddressLiveUpdate = MutableLiveData<Response<UserAddress>>()

    val userAddressLive: LiveData<Response<UserAddress>>
        get() = userAddressLiveUpdate

    fun getOwnerAddress(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                userAddressLiveUpdate.postValue(Response.Loading())
                val result = repository.getOwnerAddress(
                    leadId
                )
                if (result.body() != null) {
                    userAddressLiveUpdate.postValue(Response.Success(result.body()))
                } else {
                    userAddressLiveUpdate.postValue(Response.Failure("api error"))
                }
            } else {
                userAddressLiveUpdate.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            userAddressLiveUpdate.postValue(Response.Failure(e.message.toString()))
        }
    }


    private val pick_up_dateLiveUpdate = MutableLiveData<Response<BookingIds>>()

    val pick_up_dateLive: LiveData<Response<BookingIds>>
        get() = pick_up_dateLiveUpdate

    fun getScheduledService(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                pick_up_dateLiveUpdate.postValue(Response.Loading())
                val result = repository.getScheduledService(
                    leadId
                )
                if (result.body() != null) {
                    pick_up_dateLiveUpdate.postValue(Response.Success(result.body()))
                } else {
                    pick_up_dateLiveUpdate.postValue(Response.Failure("api error"))
                }
            } else {
                pick_up_dateLiveUpdate.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            pick_up_dateLiveUpdate.postValue(Response.Failure(e.message.toString()))
        }
    }

}