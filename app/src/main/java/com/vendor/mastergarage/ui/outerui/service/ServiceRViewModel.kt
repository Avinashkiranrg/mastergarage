package com.vendor.mastergarage.ui.outerui.service

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.OutletsItem
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
class ServiceRViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val addLiveString = MutableLiveData<Response<UploadResponse>>()

    val added: LiveData<Response<UploadResponse>>
        get() = addLiveString

    fun getStoredVendorObject(): Vendors? {
        return ModelPreferencesManager.get<Vendors>(Constraints.VENDOR_STORE)
    }

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun addService(
        leadId: Int,
        outletId: Int,
        vendorId: Int,
        service_name: String,
        service_cost: Float,
        other_charges: Float,
        additionalInfo: String,
        attachmentUri: String,
        json_spare_string: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                addLiveString.postValue(Response.Loading())
                val result = repository.addService(
                    leadId,
                    outletId,
                    vendorId,
                    service_name,
                    service_cost,
                    other_charges,
                    additionalInfo,
                    attachmentUri,
                    json_spare_string,
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

}