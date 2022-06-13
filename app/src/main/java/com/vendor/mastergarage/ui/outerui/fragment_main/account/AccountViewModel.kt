package com.vendor.mastergarage.ui.outerui.fragment_main.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.constraints.Constraints.Companion.OUTLET_STORE
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.Vendors
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    val outletLiveData = MutableLiveData<OutletsItem?>()
    val vendorLiveData = MutableLiveData<Vendors?>()

    val _outlet: LiveData<OutletsItem?>
        get() = outletLiveData

    val _vendor: LiveData<Vendors?>
        get() = vendorLiveData

    private val isOperatingLiveData = MutableLiveData<Response<String>>()

    val _isOperating: LiveData<Response<String>>
        get() = isOperatingLiveData


    fun storeOutletObject(outletsItem: OutletsItem) {
        repository.storeOutletObject(outletsItem)
    }

    fun storeVendorObject(vendors: Vendors) {
        repository.storeVendorObject(vendors)
    }

    private fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private fun getStoredVendorObject(): Vendors? {
        return repository.getStoredVendorObject()
    }

    init {
        outletLiveData.value = getStoredOutletObject()
        vendorLiveData.value = getStoredVendorObject()
    }

    fun updateUi() {
        outletLiveData.value = getStoredOutletObject()
    }

    fun getIsOperating(isOperating: String, outletId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {
                    isOperatingLiveData.postValue(Response.Loading())
                    val result = repository.getIsOperating(isOperating, outletId)
                    isOperatingLiveData.postValue(Response.Success(result.toString()))
                } else {
                    isOperatingLiveData.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                isOperatingLiveData.postValue(Response.Failure(e.message.toString()))
            }
        }


}