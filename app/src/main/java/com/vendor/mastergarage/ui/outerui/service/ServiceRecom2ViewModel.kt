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
class ServiceRecom2ViewModel @Inject constructor(
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


    fun copyServicetoAnotherLead(
        leadId: Int,
        outletId: Int,
        vendorId: Int,
        service_name: String?,
        service_cost: Float?,
        other_charges: Float?,
        additionalInfo: String?,
        attachmentUri: String?,
        spares: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                addLiveString.postValue(Response.Loading())
                val result = repository.copyServicetoAnotherLead(
                    leadId,
                    outletId,
                    vendorId,
                    service_name,
                    service_cost,
                    other_charges,
                    additionalInfo,
                    attachmentUri,
                    spares
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
            val updateResponse = UploadResponse(1, -1, -1, "successfully upload")
            addLiveString.postValue(Response.Success(updateResponse))
        }
    }

    fun getServiceRecommByVendorId(vendorId: Int, leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                getService.postValue(Response.Loading())
                val result = repository.getServiceRecommByVendorId(
                    vendorId, leadId
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