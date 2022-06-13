package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.team

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.ServiceAdvisor
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
class ServiceAdvisorViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val serviceAdvisorLiveData = MutableLiveData<Response<ServiceAdvisor>>()

    val serviceAdvisor: LiveData<Response<ServiceAdvisor>>
        get() = serviceAdvisorLiveData

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private val deleteLiveData = MutableLiveData<Response<UploadResponse>>()

    val delete: LiveData<Response<UploadResponse>>
        get() = deleteLiveData

//    init {
//        getStoredOutletObject()?.let { getServiceAdvisor(it.outletId) }
//    }

    fun getServiceAdvisor(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                serviceAdvisorLiveData.postValue(Response.Loading())
                val result = repository.getServiceAdvisor(outletId)
                if (result.body() != null) {
                    serviceAdvisorLiveData.postValue(Response.Success(result.body()))
                } else {
                    serviceAdvisorLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                serviceAdvisorLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            serviceAdvisorLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun deleteAdvisor(advisorId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                deleteLiveData.postValue(Response.Loading())
                val result = repository.deleteAdvisor(advisorId)
                if (result.body() != null) {
                    deleteLiveData.postValue(Response.Success(result.body()))
                } else {
                    deleteLiveData.postValue(Response.Failure("Failed"))
                }
            } else {
                deleteLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            deleteLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun loadUi() {
        getStoredOutletObject()?.let { it.outletId?.let { it1 -> getServiceAdvisor(it1) } }
    }

}