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
class JobCardViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val bodyDamagedLive = MutableLiveData<Response<Damaged>>()

    val damages: LiveData<Response<Damaged>>
        get() = bodyDamagedLive


    private val inventoryChecklistLive = MutableLiveData<Response<Inventory>>()

    val inventoryChecklist: LiveData<Response<Inventory>>
        get() = inventoryChecklistLive

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private val jobCardLive = MutableLiveData<Response<UploadResponse>>()

    val jobCard: LiveData<Response<UploadResponse>>
        get() = jobCardLive


    init {
        getDamaged()
        getInventory()
    }

    fun getInventory() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                inventoryChecklistLive.postValue(Response.Loading())
                val result = repository.getInventory()
                if (result.body() != null) {
                    inventoryChecklistLive.postValue(Response.Success(result.body()))
                }
            } else {
                inventoryChecklistLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            inventoryChecklistLive.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getDamaged() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                bodyDamagedLive.postValue(Response.Loading())
                val result = repository.getDamaged()
                if (result.body() != null) {
                    bodyDamagedLive.postValue(Response.Success(result.body()))
                }
            } else {
                bodyDamagedLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            bodyDamagedLive.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun uploadJob(
        leadId: Int,
        damagedString: String,
        inventoryString: String,
        lastUpDate: String,
        lastUpTime: String,
        updateStatus: Int,
        updateRemark: String,
        kmsDriven: Int,
        fuel: Int,
        instruction: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                jobCardLive.postValue(Response.Loading())
                val result = repository.uploadJob(
                    leadId,
                    damagedString,
                    inventoryString,
                    lastUpDate,
                    lastUpTime,
                    updateStatus,
                    updateRemark,
                    kmsDriven,
                    fuel,
                    instruction
                )
                if (result.body() != null) {
                    jobCardLive.postValue(Response.Success(result.body()))
                }
            } else {
                jobCardLive.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            val updateResponse = UploadResponse(1, -1, -1, "successfully upload")
            jobCardLive.postValue(Response.Success(updateResponse))
        }
    }

    private val updateLiveData = MutableLiveData<Response<StatusUpdate>>()

    val updateStatus: LiveData<Response<StatusUpdate>>
        get() = updateLiveData

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


//    private val bodyDamagedLive = MutableLiveData<Response<BodyDamages>>()
//
//    val damages: LiveData<Response<BodyDamages>>
//        get() = bodyDamagedLive
//
//    private val inventoryChecklistLive = MutableLiveData<Response<InventoryChecklist>>()
//
//    val inventoryChecklist: LiveData<Response<InventoryChecklist>>
//        get() = inventoryChecklistLive
//
//    fun getAssignedInventory(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            if (NetworkUtil.isInternetAvailable(context)) {
//
//                inventoryChecklistLive.postValue(Response.Loading())
//                val result = repository.getAssignedInventory(outletId)
//                if (result.body() != null) {
//                    inventoryChecklistLive.postValue(Response.Success(result.body()))
//                }
//            } else {
//                inventoryChecklistLive.postValue(Response.Failure("No network"))
//            }
//        } catch (e: Exception) {
//            inventoryChecklistLive.postValue(Response.Failure(e.message.toString()))
//        }
//    }
//
//    fun getAssignedDamaged(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            if (NetworkUtil.isInternetAvailable(context)) {
//
//                bodyDamagedLive.postValue(Response.Loading())
//                val result = repository.getAssignedDamaged(outletId)
//                if (result.body() != null) {
//                    bodyDamagedLive.postValue(Response.Success(result.body()))
//                }
//            } else {
//                bodyDamagedLive.postValue(Response.Failure("No network"))
//            }
//        } catch (e: Exception) {
//            bodyDamagedLive.postValue(Response.Failure(e.message.toString()))
//        }
//    }


}