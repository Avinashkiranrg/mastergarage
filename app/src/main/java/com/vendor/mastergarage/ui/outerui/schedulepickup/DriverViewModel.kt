package com.vendor.mastergarage.ui.outerui.schedulepickup

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
class DriverViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val addLiveString = MutableLiveData<Response<UploadResponse>>()

    val added: LiveData<Response<UploadResponse>>
        get() = addLiveString


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
        imageUri: String?,
        licenceUri: String?,
        dob: String,
        mobile_no: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

//                Log.e("List ", "$driverId, $first_name, $last_name, $imageUri, $licenceUri, $licence_no, $dob, $mobile_no")
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


}