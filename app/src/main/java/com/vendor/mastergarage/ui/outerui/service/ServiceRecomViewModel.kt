package com.vendor.mastergarage.ui.outerui.service

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.ServiceDetailsList
import com.vendor.mastergarage.model.UploadResponse
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
class ServiceRecomViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val addLiveString = MutableLiveData<Response<UploadResponse>>()

    val added: LiveData<Response<UploadResponse>>
        get() = addLiveString

    private val getService = MutableLiveData<Response<ServiceDetailsList>>()

    val service: LiveData<Response<ServiceDetailsList>>
        get() = getService

    fun getStoredVendorObject(): Vendors? {
        return ModelPreferencesManager.get<Vendors>(Constraints.VENDOR_STORE)
    }

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun deleteService(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                addLiveString.postValue(Response.Loading())
                val result = repository.deleteService(
                    leadId,
                )
                if (result.body() != null) {
                    addLiveString.postValue(Response.Success(result.body()))
                } else {
                    addLiveString.postValue(Response.Failure("Failed"))
                }
            } else {
                addLiveString.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            addLiveString.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getServiceRecomm(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                getService.postValue(Response.Loading())
                val result = repository.getServiceRecomm(
                    leadId,
                )
                if (result.body() != null) {
                    getService.postValue(Response.Success(result.body()))
                } else {
                    getService.postValue(Response.Failure("Failed"))
                }
            } else {
                getService.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            getService.postValue(Response.Failure(e.message.toString()))
        }
    }

}