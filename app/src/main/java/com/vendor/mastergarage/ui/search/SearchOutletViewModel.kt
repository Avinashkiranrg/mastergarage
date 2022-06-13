package com.vendor.mastergarage.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.Outlets
import com.vendor.mastergarage.model.OutletsItem
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
class SearchOutletViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val vendorLiveData = MutableLiveData<Response<Vendors>>()

    val vendor: LiveData<Response<Vendors>>
        get() = vendorLiveData

    private val outletLiveData = MutableLiveData<Response<Outlets>>()

    val outlets: LiveData<Response<Outlets>>
        get() = outletLiveData

    fun getVendor(phone: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                vendorLiveData.postValue(Response.Loading())
                val result = repository.getVendor(phone)
                if (result.body() != null) {
                    vendorLiveData.postValue(Response.Success(result.body()))
                } else {
                    vendorLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                vendorLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            vendorLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun getOutlet(vendorId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                outletLiveData.postValue(Response.Loading())
                val result = repository.getVendorOutLets(vendorId)
                if (result.body() != null) {
                    outletLiveData.postValue(Response.Success(result.body()))
                } else {
                    outletLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                outletLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            outletLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun storeOutletObject(outletsItem: OutletsItem) {
        repository.storeOutletObject(outletsItem)
    }

    fun storeVendorObject(vendors: Vendors) {
        repository.storeVendorObject(vendors)
    }

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun getStoredVendorObject(): Vendors? {
        return repository.getStoredVendorObject()
    }

}