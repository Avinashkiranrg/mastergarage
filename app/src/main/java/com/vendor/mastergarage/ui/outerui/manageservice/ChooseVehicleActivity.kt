package com.vendor.mastergarage.ui.outerui.manageservice

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.ChooseVehicleAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.constraints.Constraints.Companion.VEHICLE_CODE
import com.vendor.mastergarage.constraints.Constraints.Companion.VEHICLE_CODE_MODEL
import com.vendor.mastergarage.databinding.ActivityChooseVehicleBinding
import com.vendor.mastergarage.model.VehicleProvideItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChooseVehicleActivity : AppCompatActivity(), TextWatcher,
    ChooseVehicleAdapter.OnItemClickListener {

    lateinit var binding: ActivityChooseVehicleBinding

    private val viewModel: ChooseVehicleViewModel by viewModels()

    lateinit var chooseVehicleAdapter: ChooseVehicleAdapter

    var vehiclesItem: ArrayList<VehicleProvideItem>? = null
    var filteredVehiclesItem: List<VehicleProvideItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }


        viewModel.vehicles.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val vItem = it.data.result as MutableList<VehicleProvideItem>
                        vehiclesItem = ArrayList()
                        vehiclesItem!!.addAll(vItem)
                        binding.materialTextView5.text = "All Brands ${vItem.size}"
                        chooseVehicleAdapter = ChooseVehicleAdapter(this, vItem, this)
                        binding.recyclerView.apply {
                            setHasFixedSize(true)
                            adapter = chooseVehicleAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    this@ChooseVehicleActivity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                        }
                    } else {
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.searchVehicle.addTextChangedListener(this)

        viewModel.isOnOff.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    try {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {

                            }
                            binding.progressBar.visibility = View.GONE
                        }
                        if (vItem != null) {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                        }


                    } catch (e: NullPointerException) {

                    }
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
//                     Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

    }

    companion object {
        private const val TAG = "ChooseVehicleActivity"
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        try {
            val str: String = s.toString()
            filteredVehiclesItem = filter(vehiclesItem!!, str)
            chooseVehicleAdapter.setFilter(filteredVehiclesItem as List<VehicleProvideItem?>)
        } catch (e: NullPointerException) {

        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun filter(
        outletsItem: List<VehicleProvideItem>,
        queryText: String
    ): List<VehicleProvideItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        val filteredDataList = ArrayList<VehicleProvideItem>()
        for (dataFromDataList in outletsItem) {
            text = dataFromDataList.name!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }


    override fun onItemClick(vehiclesItem: VehicleProvideItem) {
        val intent = Intent(this, ChooseModelVehicleActivity::class.java)
        intent.putExtra("vehiclesItem", vehiclesItem)
        intentLauncher.launch(intent)
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == VEHICLE_CODE_MODEL) {
                val vehicleId = result.data?.getStringExtra("vehicleId")
                val vehicle_name = result.data?.getStringExtra("vehicle_name")
                val vehicle_imageUri = result.data?.getStringExtra("vehicle_imageUri")
                val modelId = result.data?.getStringExtra("modelId")
                val model_name = result.data?.getStringExtra("model_name")
                val variantId = result.data?.getStringExtra("variantId")
                val variant_name = result.data?.getStringExtra("variant_name")
                val capacity = result.data?.getStringExtra("capacity")
                val fuel = result.data?.getStringExtra("fuel")
                setResult(VEHICLE_CODE, result.data)
                finish();
            }
        }

    override fun onOnOff(vehiclesItem: VehicleProvideItem, flag: String) {
        binding.progressBar.visibility = View.VISIBLE
        vehiclesItem.vcId?.let { viewModel.disableVehicle(it, flag) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUi()
    }
}