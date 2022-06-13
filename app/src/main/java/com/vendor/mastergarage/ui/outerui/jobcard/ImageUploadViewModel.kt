package com.vendor.mastergarage.ui.outerui.jobcard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.Images
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
class ImageUploadViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private val jobCardLive = MutableLiveData<Response<UploadResponse>>()

    val jobCard: LiveData<Response<UploadResponse>>
        get() = jobCardLive

    private val getImageUploaded = MutableLiveData<Response<Images>>()

    val getImage: LiveData<Response<Images>>
        get() = getImageUploaded

    val deleteResponse = MutableLiveData<Response<UploadResponse>>()

    val response: LiveData<Response<UploadResponse>>
        get() = deleteResponse


    fun uploadJobCardImage(
        leadId: Int,
        imageString: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                jobCardLive.postValue(Response.Loading())
                val result = repository.uploadJobCardImage(
                    leadId,
                    imageString
                )
                if (result.body() != null) {
                    jobCardLive.postValue(Response.Success(result.body()))
                }
            } else {
                jobCardLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            jobCardLive.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getJobCardImage(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                getImageUploaded.postValue(Response.Loading())
                val result = repository.getJobCardImage(
                    leadId
                )
                if (result.body() != null) {
                    getImageUploaded.postValue(Response.Success(result.body()))
                }
            } else {
                getImageUploaded.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            getImageUploaded.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun deleteImage(
        imageId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                deleteResponse.postValue(Response.Loading())
                val result = repository.deleteImage(
                    imageId
                )
                if (result.body() != null) {
                    deleteResponse.postValue(Response.Success(result.body()))
                }
            } else {
                deleteResponse.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            deleteResponse.postValue(Response.Failure(e.message.toString()))
        }
    }

}