package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.faq

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.mastergarage.model.Drivers
import com.vendor.mastergarage.model.Faq
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
class FaqFragViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val faqLiveData = MutableLiveData<Response<Faq>>()

    val faq: LiveData<Response<Faq>>
        get() = faqLiveData

    init {
        getFaq()
    }

    private fun getFaq() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (NetworkUtil.isInternetAvailable(context)) {
                faqLiveData.postValue(Response.Loading())
                val result = repository.getFaq()
                if (result.body() != null) {
                    faqLiveData.postValue(Response.Success(result.body()))
                } else {
                    faqLiveData.postValue(Response.Failure("api error"))
                }
            } else {
                faqLiveData.postValue(Response.Failure("No network"))
            }
        } catch (e: Exception) {
            faqLiveData.postValue(Response.Failure(e.message.toString()))
        }
    }

}