package com.vendor.mastergarage.ui.outerui.schedulepickup

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.DriverAssigned
import com.vendor.mastergarage.model.DriverDrop
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
class SchedulePickUpViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val addLiveString = MutableLiveData<Response<UploadResponse>>()

    val added: LiveData<Response<UploadResponse>>
        get() = addLiveString


    private val liveUpdate = MutableLiveData<Response<UploadResponse>>()

    val update: LiveData<Response<UploadResponse>>
        get() = liveUpdate

    private val assignUpdate = MutableLiveData<Response<DriverAssigned>>()

    val driver: LiveData<Response<DriverAssigned>>
        get() = assignUpdate


    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun addDriver(
        first_name: String,
        last_name: String,
        licence_no: String,
        imageUri: String,
        licenceUri: String,
        dob: String,
        mobile_no: String,
        outletId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                addLiveString.postValue(Response.Loading())
                val result = repository.addDriver(
                    first_name,
                    last_name,
                    licence_no,
                    imageUri,
                    licenceUri,
                    dob,
                    mobile_no,
                    outletId,
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

    fun updateDriver(
        driverId: Int,
        first_name: String,
        last_name: String,
        licence_no: String,
        imageUri: String,
        licenceUri: String,
        dob: String,
        mobile_no: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                addLiveString.postValue(Response.Loading())
                val result = repository.updateDriver(
                    driverId,
                    first_name,
                    last_name,
                    licence_no,
                    imageUri,
                    licenceUri,
                    dob,
                    mobile_no
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

    fun setAssignDriver(
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
                    val result = repository.setAssignDriver(
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

    fun setAssignAdvisorChange(
        driverId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    liveUpdate.postValue(Response.Loading())
                    val result = repository.setAssignDriverChange(
                        driverId, leadId, status, last_up_date,
                        last_up_time
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
}