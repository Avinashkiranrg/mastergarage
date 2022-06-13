package com.vendor.mastergarage.ui.mainactivity

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.Outlets
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.Vendors
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {
//    private val vendorLiveData = MutableLiveData<Response<Vendors>>()
//
//    val vendor: LiveData<Response<Vendors>>
//        get() = vendorLiveData
//
//    private val outletLiveData = MutableLiveData<Response<Outlets>>()
//
//    val outlets: LiveData<Response<Outlets>>
//        get() = outletLiveData
//
//    private val outletDefaultSelectedLiveData = MutableLiveData<Response<OutletsItem>>()
//
//    val outletDefaultSelected: LiveData<Response<OutletsItem>>
//        get() = outletDefaultSelectedLiveData
//
//
//    fun getVendor(phone: String) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            val result = repository.getVendor(phone)
//            if (result.body() != null) {
//                vendorLiveData.postValue(Response.Success(result.body()))
//            } else {
//                vendorLiveData.postValue(Response.Failure("api error"))
//            }
//        } catch (e: Exception) {
//            vendorLiveData.postValue(Response.Failure(e.message.toString()))
//        }
//    }
//
//    fun getOutlet(vendorId: String) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            val result = repository.getVendorOutLetDefaultSelected(vendorId)
//            if (result.body() != null) {
//                outletDefaultSelectedLiveData.postValue(Response.Success(result.body()))
//            } else {
//                outletDefaultSelectedLiveData.postValue(Response.Failure("api error"))
//            }
//        } catch (e: Exception) {
//            outletDefaultSelectedLiveData.postValue(Response.Failure(e.message.toString()))
//        }
//    }

}