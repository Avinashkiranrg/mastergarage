package com.vendor.mastergarage.ui.outerui.viewdetailsviewpager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.PaymentInfo
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.repository.MainRepository
import com.vendor.mastergarage.ui.BaseViewModel
import com.vendor.mastergarage.utlis.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentInfoViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val paymentInfoLive = MutableLiveData<Response<PaymentInfo>>()

    val paymentInfo: LiveData<Response<PaymentInfo>>
        get() = paymentInfoLive


    private fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

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