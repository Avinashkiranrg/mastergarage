package com.vendor.mastergarage.ui.outerui.jobcard

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
class JobCardShowViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private val getJobCard = MutableLiveData<Response<JobCard>>()

    val jobCard: LiveData<Response<JobCard>>
        get() = getJobCard

    private val getImageUploaded = MutableLiveData<Response<Images>>()

    val getImage: LiveData<Response<Images>>
        get() = getImageUploaded

    private val getDamaged = MutableLiveData<Response<JobCardDamaged>>()

    val damaged: LiveData<Response<JobCardDamaged>>
        get() = getDamaged

    private val getInventory = MutableLiveData<Response<JobCardInventory>>()

    val inventory: LiveData<Response<JobCardInventory>>
        get() = getInventory

    fun getAssignedDamaged(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                getDamaged.postValue(Response.Loading())
                val result = repository.getAssignedDamaged(
                    leadId
                )
                if (result.body() != null) {
                    getDamaged.postValue(Response.Success(result.body()))
                }
            } else {
                getDamaged.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            getDamaged.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getJobCard(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                getJobCard.postValue(Response.Loading())
                val result = repository.getJobCard(
                    leadId
                )
                if (result.body() != null) {
                    getJobCard.postValue(Response.Success(result.body()))
                }
            } else {
                getJobCard.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            getJobCard.postValue(Response.Failure(e.message.toString()))
        }
    }


    fun getAssignedInventory(
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                getInventory.postValue(Response.Loading())
                val result = repository.getAssignedInventory(
                    leadId
                )
                if (result.body() != null) {
                    getInventory.postValue(Response.Success(result.body()))
                }
            } else {
                getInventory.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            getInventory.postValue(Response.Failure(e.message.toString()))
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

    private val getService = MutableLiveData<Response<ServiceDetailsList>>()

    val service: LiveData<Response<ServiceDetailsList>>
        get() = getService

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