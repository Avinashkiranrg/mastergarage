package com.vendor.mastergarage.ui.outerui

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
class AcceptLeadsViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository
) : BaseViewModel(application) {

    private val _acceptLeadsMutLiveData = MutableLiveData<Response<AcceptLeadsResp>>()

    val _aceptLeadsData: LiveData<Response<AcceptLeadsResp>>
        get() = _acceptLeadsMutLiveData


    private val _declineLeadsMutLiveData = MutableLiveData<Response<AcceptLeadsResp>>()

    val _declineLeadsData: LiveData<Response<AcceptLeadsResp>>
        get() = _declineLeadsMutLiveData



    fun acceptLeads(
       acceptLeadsReq: AcceptLeadsReq
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    _acceptLeadsMutLiveData.postValue(Response.Loading())
                    val result = repository.acceptLeads(acceptLeadsReq)
                    if (result.body() != null) {
                        _acceptLeadsMutLiveData.postValue(Response.Success(result.body()))
                    } else {
                        _acceptLeadsMutLiveData.postValue(Response.Failure("Failed"))
                    }
                } else {
                    _acceptLeadsMutLiveData.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                _acceptLeadsMutLiveData.postValue(Response.Failure(e.message.toString()))
            }
        }


    fun declineLeads(
        leadId: Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    _declineLeadsMutLiveData.postValue(Response.Loading())
                    val result = repository.declineLeads(leadId)
                    if (result.body() != null) {
                        _declineLeadsMutLiveData.postValue(Response.Success(result.body()))
                    } else {
                        _declineLeadsMutLiveData.postValue(Response.Failure("Failed"))
                    }
                } else {
                    _declineLeadsMutLiveData.postValue(Response.Failure("No network"))
                }
            } catch (e: Exception) {
                _declineLeadsMutLiveData.postValue(Response.Failure(e.message.toString()))
            }
        }



}