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
class ServiceInfoViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val requestedLiveData = MutableLiveData<Response<ServiceRequested>>()

    val requested: LiveData<Response<ServiceRequested>>
        get() = requestedLiveData

    private val addLiveString = MutableLiveData<Response<StatusUpdate>>()

    val liveData: LiveData<Response<StatusUpdate>>
        get() = addLiveString

    private val selectedAdvisor = MutableLiveData<Response<ServiceAdvisorAssigned>>()

    val advisorItem: LiveData<Response<ServiceAdvisorAssigned>>
        get() = selectedAdvisor

    private val advisorMutableLive = MutableLiveData<Response<UpdateStatus>>()

    val advisorData: LiveData<Response<UpdateStatus>>
        get() = advisorMutableLive

    private val otpLive = MutableLiveData<Response<UploadResponse>>()

    val otpVerify: LiveData<Response<UploadResponse>>
        get() = otpLive

    private val assignUpdate = MutableLiveData<Response<DriverAssigned>>()

    val driver: LiveData<Response<DriverAssigned>>
        get() = assignUpdate

    private fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private val serviceInstructionLive = MutableLiveData<Response<Instructions>>()

    val serviceInstruction: LiveData<Response<Instructions>>
        get() = serviceInstructionLive


    fun getServiceInstruction(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                serviceInstructionLive.postValue(Response.Loading())
                val result = repository.getServiceInstructionByOwner(
                    leadId,
                )
                if (result.body() != null) {
                    serviceInstructionLive.postValue(Response.Success(result.body()))
                } else {
                    serviceInstructionLive.postValue(Response.Failure("Failed"))
                }
            } else {
                serviceInstructionLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            serviceInstructionLive.postValue(Response.Failure(e.message.toString()))
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


    fun updateStatus(
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String,
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                otpLive.postValue(Response.Loading())
                val result = repository.updateStatus(
                    last_up_date,
                    last_up_time,
                    update_status,
                    update_remarks,
                    leadId
                )
                if (result.body() != null) {
                    otpLive.postValue(Response.Success(result.body()))
                } else {
                    otpLive.postValue(Response.Failure("api error"))
                }
            } else {
                otpLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            otpLive.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getServiceRequested(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                requestedLiveData.postValue(Response.Loading())
                val result = repository.getServiceRequested(leadId)
                if (result.body() != null) {
                    requestedLiveData.postValue(Response.Success(result.body()))
                } else {
                    requestedLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                requestedLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            requestedLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    private fun getUpdateStatus(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                addLiveString.postValue(Response.Loading())
                val result = repository.getUpdateStatus(leadId)
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

    fun getAssignedAdvisor(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                selectedAdvisor.postValue(Response.Loading())
                val result = repository.getAssignedAdvisor(leadId)
                if (result.body() != null) {
                    selectedAdvisor.postValue(Response.Success(result.body()))
                } else {
                    selectedAdvisor.postValue(Response.Failure("api error"))
                }
            } else {
                selectedAdvisor.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            selectedAdvisor.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getAssignedDriver(leadId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    assignUpdate.postValue(Response.Loading())
                    val result = repository.getAssignedDriver(leadId)
                    if (result.body() != null) {
                        assignUpdate.postValue(Response.Success(result.body()))
                    } else {
                        assignUpdate.postValue(Response.Failure("Failed"))
                    }
                } else {
                    assignUpdate.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                assignUpdate.postValue(Response.Failure(e.message.toString()))
            }
        }

    private val liveUpdate = MutableLiveData<Response<UploadResponse>>()

    val update: LiveData<Response<UploadResponse>>
        get() = liveUpdate

    fun setAssignDropDriver(
        driverId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    liveUpdate.postValue(Response.Loading())
                    val result = repository.setAssignDropDriver(
                        driverId, leadId, status, last_up_date,
                        last_up_time,
                        update_status,
                        update_remarks
                    )
                    if (result.body() != null) {
                        liveUpdate.postValue(Response.Success(result.body()))
                    } else {
                        liveUpdate.postValue(Response.Failure("Failed"))
                    }
                } else {
                    liveUpdate.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                liveUpdate.postValue(Response.Failure(e.message.toString()))
            }
        }


    private val assignUpdateDrop = MutableLiveData<Response<DriverDrop>>()

    val driverDrop: LiveData<Response<DriverDrop>>
        get() = assignUpdateDrop

    fun getAssignedDriverDrop(leadId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    assignUpdateDrop.postValue(Response.Loading())
                    val result = repository.getAssignedDriverDrop(leadId)
                    if (result.body() != null) {
                        assignUpdateDrop.postValue(Response.Success(result.body()))
                    } else {
                        assignUpdateDrop.postValue(Response.Failure("Failed"))
                    }
                } else {
                    assignUpdateDrop.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                assignUpdateDrop.postValue(Response.Failure(e.message.toString()))
            }
        }

    fun loadUi(leadId: Int?) {
        if (leadId != null) {
            getUpdateStatus(leadId)
        }
    }

//    fun getAdvisorOnLead(leadId: Int?) {
//        try {
//            if (NetworkUtil.isInternetAvailable(context)) {
//
//                advisorMutableLive.postValue(Response.Loading())
//                val result = repository.getAdvisorOnLead(leadId)
//                if (result.body() != null) {
//                    advisorMutableLive.postValue(Response.Success(result.body()))
//                } else {
//                    advisorMutableLive.postValue(Response.Failure("api error"))
//                }
//            } else {
//                advisorMutableLive.postValue(Response.Failure("No network"))
//            }
//        } catch (e: Exception) {
//            advisorMutableLive.postValue(Response.Failure(e.message.toString()))
//        }
//    }
}