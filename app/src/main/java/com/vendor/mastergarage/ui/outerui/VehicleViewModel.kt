package com.vendor.mastergarage.ui.outerui

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
class VehicleViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val _ownerMutLiveData = MutableLiveData<Response<VehicleOwner>>()

    val _owner: LiveData<Response<VehicleOwner>>
        get() = _ownerMutLiveData

//    private val leadsLiveData = MutableLiveData<Response<LeadItemData>>()
//
//    val leads: LiveData<Response<LeadItemData>>
//        get() = leadsLiveData

    private val addLiveString = MutableLiveData<Response<UpdateStatus>>()

    val liveData: LiveData<Response<UpdateStatus>>
        get() = addLiveString

    private val declineLiveString = MutableLiveData<Response<AcceptLeadsResp>>()

    val onDecline: LiveData<Response<AcceptLeadsResp>>
        get() = declineLiveString

    private val updateLiveData = MutableLiveData<Response<StatusUpdate>>()

    val updateStatus: LiveData<Response<StatusUpdate>>
        get() = updateLiveData

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
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


    fun getUpdateStatus(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                updateLiveData.postValue(Response.Loading())
                val result = repository.getUpdateStatus(leadId)
                if (result.body() != null) {
                    updateLiveData.postValue(Response.Success(result.body()))
                } else {
                    updateLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                updateLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            updateLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }


//    fun getLeadsById(leadId: Int) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            if (NetworkUtil.isInternetAvailable(context)) {
//
//                leadsLiveData.postValue(Response.Loading())
//                val result = repository.getLeadsById(leadId)
//                if (result.body() != null) {
//                    leadsLiveData.postValue(Response.Success(result.body()))
//                } else {
//                    leadsLiveData.postValue(Response.Failure("api error"))
//                }
//            } else {
//                leadsLiveData.postValue(Response.Failure("No network"))
//            }
//        } catch (e: Exception) {
//            leadsLiveData.postValue(Response.Failure(e.message.toString()))
//        }
//    }

    fun loadUi(leadId: Int) {
//        getLeadsById(leadId)
    }

//    fun getOwnerDetails(ownerId: Int) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            if (NetworkUtil.isInternetAvailable(context)) {
//
//                _ownerMutLiveData.postValue(Response.Loading())
//                val result = repository.getOwnerDetails(ownerId)
//                if (result.body() != null) {
//                    _ownerMutLiveData.postValue(Response.Success(result.body()))
//                } else {
//                    _ownerMutLiveData.postValue(Response.Failure("api error"))
//                }
//            } else {
//                _ownerMutLiveData.postValue(Response.Failure("No network"))
//            }
//        } catch (e: Exception) {
//            _ownerMutLiveData.postValue(Response.Failure(e.message.toString()))
//        }
//    }

    fun setOnGoing(
        last_up_date: String,
        last_up_time: String,
        booking_date: String,
        booking_time: String,
        outletId: Int,
        vehicleId: Int,
        addressId: Int,
        leadId: Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    addLiveString.postValue(Response.Loading())
                    val result = repository.setOnGoing(
                        last_up_date,
                        last_up_time,
                        booking_date,
                        booking_time,
                        outletId,
                        vehicleId,
                        addressId,
                        leadId
                    )
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

    private val closedLiveData = MutableLiveData<Response<UploadResponse>>()

    val closed: LiveData<Response<UploadResponse>>
        get() = closedLiveData

    fun closedLeads(
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String,
        outletId: Int,
        vehicleId: Int,
        addressId: Int,
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                closedLiveData.postValue(Response.Loading())
                val result = repository.closedLeads(
                    last_up_date,
                    last_up_time,
                    update_status,
                    update_remarks,
                    outletId,
                    vehicleId,
                    addressId,
                    leadId
                )
                if (result.body() != null) {
                    closedLiveData.postValue(Response.Success(result.body()))
                } else {
                    closedLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                closedLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            closedLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun declineLeads(leadId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    declineLiveString.postValue(Response.Loading())
                    val result = repository.declineLeads(
                        leadId
                    )
                    if (result.body() != null) {
                        declineLiveString.postValue(Response.Success(result.body()))
                    } else {
                        declineLiveString.postValue(Response.Failure("api error"))
                    }
                } else {
                    declineLiveString.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                declineLiveString.postValue(Response.Failure(e.message.toString()))
            }
        }

    private val inspectionLiveUpdate = MutableLiveData<Response<UploadResponse>>()

    val inspectionLive: LiveData<Response<UploadResponse>>
        get() = inspectionLiveUpdate

    fun updateStatus(
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String,
        leadId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                inspectionLiveUpdate.postValue(Response.Loading())
                val result = repository.updateStatus(
                    last_up_date,
                    last_up_time,
                    update_status,
                    update_remarks,
                    leadId
                )
                if (result.body() != null) {
                    inspectionLiveUpdate.postValue(Response.Success(result.body()))
                } else {
                    inspectionLiveUpdate.postValue(Response.Failure("api error"))
                }
            } else {
                inspectionLiveUpdate.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            inspectionLiveUpdate.postValue(Response.Failure(e.message.toString()))
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

    private val paymentInfoLive = MutableLiveData<Response<PaymentInfo>>()

    val paymentInfo: LiveData<Response<PaymentInfo>>
        get() = paymentInfoLive



    fun getPaymentInfo(leadId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    paymentInfoLive.postValue(Response.Loading())
                    val result = repository.getPaymentInfo(leadId)
                    if (result.body() != null) {
                        paymentInfoLive.postValue(Response.Success(result.body()))
                    } else {
                        paymentInfoLive.postValue(Response.Failure("Failed"))
                    }
                } else {
                    paymentInfoLive.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                paymentInfoLive.postValue(Response.Failure(e.message.toString()))
            }
        }
}