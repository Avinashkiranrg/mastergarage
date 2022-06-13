package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.discount

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.ServicePackage
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
class AddDiscountFragViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val serviceLiveData = MutableLiveData<Response<ServicePackage>>()

    val service: LiveData<Response<ServicePackage>>
        get() = serviceLiveData


    private val loadResponse = MutableLiveData<Response<UploadResponse>>()

    val response: LiveData<Response<UploadResponse>>
        get() = loadResponse

    fun getStoredOutletObject(): OutletsItem? {
        return repository.getStoredOutletObject()
    }

    init {
        getServicePackage()
    }

    private fun getServicePackage() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                serviceLiveData.postValue(Response.Loading())
                val result = repository.getServicePackage()
                if (result.body() != null) {
                    serviceLiveData.postValue(Response.Success(result.body()))
                } else {
                    serviceLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                serviceLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            serviceLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

    fun addDiscounts(
        applicable_on: String,
        end_date: String,
        start_date: String,
        type_discount: String,
        min_order_value: Int,
        discount_code: String,
        outletId: Int,
        max_discount: Int,
        discount_value: Int,
        apply_users: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {

                loadResponse.postValue(Response.Loading())

                Log.e(
                    "applicable_on", "applicable_on: $applicable_on")
                Log.e(
                    "Data", "end_date: $end_date, start_date: $start_date, type_discount: $type_discount, min_order_value: $min_order_value, " +
                            "discount_code : $discount_code,outletId: $outletId, max_discount: $max_discount,discount_value: $discount_value, apply_users: $apply_users"
                )
                val result = repository.addDiscounts(
                    applicable_on,
                    end_date,
                    start_date,
                    type_discount,
                    min_order_value,
                    discount_code,
                    outletId,
                    max_discount,
                    discount_value,
                    apply_users
                )
                if (result.body() != null) {
                    loadResponse.postValue(Response.Success(result.body()))
                } else {
                    loadResponse.postValue(Response.Failure("api error"))
                }
            } else {
                loadResponse.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            loadResponse.postValue(Response.Failure(e.message.toString()))
        }
    }

}