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
class EditRecomViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val addLiveString = MutableLiveData<Response<UploadResponse>>()

    val update: LiveData<Response<UploadResponse>>
        get() = addLiveString

    private val deleteService = MutableLiveData<Response<UploadResponse>>()

    val delete: LiveData<Response<UploadResponse>>
        get() = deleteService

    fun getStoredVendorObject(): Vendors? {
        return ModelPreferencesManager.get<Vendors>(Constraints.VENDOR_STORE)
    }

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun deleteService(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                deleteService.postValue(Response.Loading())
                val result = repository.deleteService(
                    leadId,
                )
                if (result.body() != null) {
                    deleteService.postValue(Response.Success(result.body()))
                } else {
                    deleteService.postValue(Response.Failure("Failed"))
                }
            } else {
                deleteService.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            deleteService.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun deleteRcommServiceSpare(sparesId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                addLiveString.postValue(Response.Loading())
                val result = repository.deleteRcommServiceSpare(
                    sparesId,
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

    private val editServiceLive = MutableLiveData<Response<UploadResponse>>()

    val editService: LiveData<Response<UploadResponse>>
        get() = editServiceLive


    fun editService(
        serviceId: Int,
        service_name: String,
        service_cost: Float,
        other_charges: Float,
        additionalInfo: String,
        attachmentUri: String?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                editServiceLive.postValue(Response.Loading())
                val result = repository.editService(
                    serviceId,
                    service_name,
                    service_cost,
                    other_charges,
                    additionalInfo,
                    attachmentUri,
                )
                if (result.body() != null) {
                    editServiceLive.postValue(Response.Success(result.body()))
                } else {
                    editServiceLive.postValue(Response.Failure("Failed"))
                }
            } else {
                editServiceLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            val updateResponse = UploadResponse(1, -1, -1, "successfully upload")
            editServiceLive.postValue(Response.Success(updateResponse))
        }
    }

    private val editServiceSpareLive = MutableLiveData<Response<UploadResponse>>()

    val editServiceSpare: LiveData<Response<UploadResponse>>
        get() = editServiceSpareLive

    fun editServiceSpare(
        sparesId: Int,
        manufacturer_name: String,
        part_name: String,
        warranty: String,
        year: Int,
        part_type: Int,
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                editServiceSpareLive.postValue(Response.Loading())
                val result = repository.editServiceSpare(
                    sparesId,
                    manufacturer_name,
                    part_name,
                    warranty,
                    year,
                    part_type,
                )
                if (result.body() != null) {
                    editServiceSpareLive.postValue(Response.Success(result.body()))
                } else {
                    editServiceSpareLive.postValue(Response.Failure("Failed"))
                }
            } else {
                editServiceSpareLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            val updateResponse = UploadResponse(1, -1, -1, "successfully upload")
            editServiceSpareLive.postValue(Response.Success(updateResponse))
        }
    }


}