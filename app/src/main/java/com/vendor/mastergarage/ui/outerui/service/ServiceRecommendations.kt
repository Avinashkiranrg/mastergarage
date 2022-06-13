package com.vendor.mastergarage.ui.outerui.service

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.ServiceRecomm2Adapter
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.ActivityServiceRecommendations2Binding
import com.vendor.mastergarage.model.ServiceDetails
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceRecommendations : AppCompatActivity(), ServiceRecomm2Adapter.OnItemClickListener {
    lateinit var binding: ActivityServiceRecommendations2Binding

    private var leadId: Int? = null

    val viewModel: ServiceRecom2ViewModel by viewModels()
    var filterList: ArrayList<ServiceDetails?>? = null

    private lateinit var serviceAdaptor: ServiceRecomm2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceRecommendations2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        try {
            leadId = intent.getIntExtra("leadId", -1)
            viewModel.getStoredVendorObject()?.vendorId?.let {
                viewModel.getServiceRecommByVendorId(
                    it, leadId!!
                )
            }
        } catch (n: NullPointerException) {

        }
        viewModel.service.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<ServiceDetails>

                    serviceAdaptor =
                        ServiceRecomm2Adapter(this, vItem, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = serviceAdaptor
                        layoutManager =
                            LinearLayoutManager(
                                this@ServiceRecommendations,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

    }

    companion object {
        private const val TAG = "ServiceRecommendations"
    }

    override fun onSelect(serviceDetails: ServiceDetails) {
        leadId?.let {
            val list = serviceDetails.spares
            val gson = Gson()
            val spare = gson.toJson(list)
            viewModel.getStoredOutletObject()?.outletId?.let { it1 ->
                viewModel.getStoredVendorObject()?.vendorId?.let { it2 ->
                    viewModel.copyServicetoAnotherLead(
                        it,
                        it1,
                        it2,
                        serviceDetails.name,
                        serviceDetails.cost,
                        serviceDetails.other_charges,
                        serviceDetails.additional_details,
                        serviceDetails.attachedUri,
                        spare
                    )
                }
            }
        }
        viewModel.added.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == TRUE_INT) {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                                .show()
                            reload()
                        } else {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })
    }

    private fun reload() {
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            val intent = intent;
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}