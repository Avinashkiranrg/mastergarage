package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.discount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.AddDiscountServiceAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.ActivityAddServiceInDiscountBinding
import com.vendor.mastergarage.model.ServicePackageItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddServiceInDiscountActivity : AppCompatActivity(),
    AddDiscountServiceAdapter.OnItemClickListener {

    lateinit var binding: ActivityAddServiceInDiscountBinding

    private val viewModel: AddDiscountFragViewModel by viewModels()

    var filteredDataList: ArrayList<ServicePackageItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceInDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filteredDataList = ArrayList()


        viewModel.service.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    try {
//                        val vItem = it.data as ArrayList<ServicePackageItem>
//                        val serviceAdapter = AddDiscountServiceAdapter(this, vItem, this)
//                        binding.recyclerView.apply {
//                            setHasFixedSize(true)
//                            adapter = serviceAdapter
//                            layoutManager =
//                                LinearLayoutManager(
//                                    this@AddServiceInDiscountActivity,
//                                    LinearLayoutManager.VERTICAL,
//                                    false
//                                )
//                        }

                        if (it.data?.success == TRUE_INT) {
                            val vItem = it.data.result as ArrayList<ServicePackageItem>
                            val serviceAdapter = AddDiscountServiceAdapter(this, vItem, this)
                            binding.recyclerView.apply {
                                setHasFixedSize(true)
                                adapter = serviceAdapter
                                layoutManager =
                                    LinearLayoutManager(
                                        this@AddServiceInDiscountActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                            }
                        }

                    } catch (e: NullPointerException) {

                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.addBtn.setOnClickListener {
            if (filteredDataList != null) {
                if (filteredDataList!!.size > 0) {
                    val data = Intent()
                    data.putExtra("list", filteredDataList)
                    setResult(Constraints.DATA_CODE, data);
                    finish();
                }
            }
        }
    }

    override fun onItemClick(serviceListItem: ServicePackageItem, flag: Boolean) {
        if (filteredDataList != null) {
            if (flag) {
                filteredDataList!!.add(serviceListItem)
            } else {
                filteredDataList!!.remove(serviceListItem)
            }
        }
    }

    companion object {
        private const val TAG = "AddServiceInDiscountActivity"
    }
}