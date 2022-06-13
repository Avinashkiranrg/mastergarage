package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.Outlets
import com.vendor.mastergarage.model.OutletsItem
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
class ProfileVewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository,
    private val vendorPreference: VendorPreference
) : BaseViewModel(application) {
//    class ProfileVewModel @Inject constructor(
//    private val repository: MainRepository,
//    private val vendorPreference: VendorPreference
//) : ViewModel() {

    private val outletLiveData = MutableLiveData<Response<Outlets>>()

    val outlets: LiveData<Response<Outlets>>
        get() = outletLiveData

    val _outletLiveData = MutableLiveData<OutletsItem?>()
    private val _vendorLiveData = MutableLiveData<Vendors?>()

    val _outlet: LiveData<OutletsItem?>
        get() = _outletLiveData

    val _vendor: LiveData<Vendors?>
        get() = _vendorLiveData


    private val addLiveString = MutableLiveData<Response<UploadResponse>>()

    val added: LiveData<Response<UploadResponse>>
        get() = addLiveString


    private fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    fun getStoredVendorObject(): Vendors? {
        return repository.getStoredVendorObject()
    }

    fun storeOutletObject(outletsItem: OutletsItem) {
        repository.storeOutletObject(outletsItem)
    }

    init {
        _outletLiveData.value = getStoredOutletObject()
        _vendorLiveData.value = getStoredVendorObject()
        getStoredOutletObject()?.let {
            it.vendorId?.let { it1 ->
                getOutlet(it1)
            }
        }
    }

    fun updateUi() {
        _outletLiveData.value = getStoredOutletObject()
    }

    private fun getOutlet(vendorId: Int) = viewModelScope.launch(Dispatchers.IO) {
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

    fun logout() = viewModelScope.launch {
        FirebaseAuth.getInstance().signOut()
        ModelPreferencesManager.clear()
        vendorPreference.clear()
    }

    fun addRating(rating: Float, message: String, vendorId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {
                    addLiveString.postValue(Response.Loading())
                    val result = repository.addRating(rating, message, vendorId)
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

}