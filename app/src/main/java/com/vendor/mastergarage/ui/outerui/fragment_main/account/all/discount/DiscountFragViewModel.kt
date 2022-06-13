package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.discount

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uidesign.model.Discounts
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
class DiscountFragViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val discountsLiveData = MutableLiveData<Response<Discounts>>()

    val discount: LiveData<Response<Discounts>>
        get() = discountsLiveData

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    private fun getDiscounts(outletId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                discountsLiveData.postValue(Response.Loading())
                val result = repository.getDiscounts(outletId)
                if (result.body() != null) {
                    discountsLiveData.postValue(Response.Success(result.body()))
                } else {
                    discountsLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                discountsLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            discountsLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun loadUi() {
        getStoredOutletObject()?.outletId?.let { getDiscounts(it) }
    }

}