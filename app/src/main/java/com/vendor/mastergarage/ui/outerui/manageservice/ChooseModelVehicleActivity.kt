package com.vendor.mastergarage.ui.outerui.manageservice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.ChooseVehicleModelAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.constraints.Constraints.Companion.VEHICLE_CODE_MODEL
import com.vendor.mastergarage.databinding.ActivityChooseModelVehicleBinding
import com.vendor.mastergarage.model.*
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.utlis.assetsToBitmapBrands
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseModelVehicleActivity : AppCompatActivity(),
    ChooseVehicleModelAdapter.OnItemClickListener {

    private val viewModel: ChooseVehicleModelViewModel by viewModels()

    lateinit var binding: ActivityChooseModelVehicleBinding
    private var vehicleModelItem: ArrayList<VehicleProvideItem>? = null
    private var vehiclesItem: VehicleProvideItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseModelVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        try {
            val intent = intent
            vehiclesItem = intent.getParcelableExtra<VehicleProvideItem>("vehiclesItem")

            val bitmap = vehiclesItem?.imageUri?.let { assetsToBitmapBrands(it) }

            bitmap?.apply {
                binding.imageView.setImageBitmap(this)
            }

            if (vehiclesItem != null) {
                binding.carName.text = vehiclesItem!!.name
            }
            try {
                binding.carName.isChecked = !vehiclesItem?.status.equals("0")
            } catch (e: NullPointerException) {

            }

            viewModel.getStoredOutletObject()?.outletId?.let {
                vehiclesItem?.vehicleId?.let { it1 ->
                    viewModel.getVehicleModels(
                        it1,
                        it
                    )
                    Log.e("IDDDDD", "vehicleId $it1 outletId $it")
                }
            }


            binding.carName.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    vehiclesItem?.vcId?.let { viewModel.disableVehicle(it, "1") }
                } else {
                    vehiclesItem?.vcId?.let { viewModel.disableVehicle(it, "0") }
                }
                binding.progressBar.visibility = View.VISIBLE
            }
        } catch (e: NullPointerException) {

        }
        viewModel.models.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val vItem = it.data.result as MutableList<VehicleModelItem>
                        vItem.sortBy { it1 -> it1.name }
                        val serviceAdapter = ChooseVehicleModelAdapter(
                            this, vItem, this
                        )
                        binding.recyclerView.apply {
                            setHasFixedSize(true)
                            adapter = serviceAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    this@ChooseModelVehicleActivity,
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
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

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
        private const val TAG = "ChooseModelVehicleActivity"
    }

    override fun onItemClick(
        variantsItem: VehicleVariantsItem,
        vehicleModelItem: VehicleModelItem
    ) {
        val data = Intent()
        data.putExtra("vehicleId", vehicleModelItem.vehicleId)
        data.putExtra("vehicle_name", vehicleModelItem.name)
        data.putExtra("brand_name", vehiclesItem?.name)
        data.putExtra("brand_imageUri", vehiclesItem?.imageUri)
        data.putExtra("vehicle_imageUri", vehicleModelItem.imageUri)
        data.putExtra("modelId", vehicleModelItem.modelId)
        data.putExtra("model_name", vehicleModelItem.name)
        data.putExtra("variantId", variantsItem.variantId)
        data.putExtra("variant_name", variantsItem.name)
//        data.putExtra("capacity", variantsItem.capacity)
        data.putExtra("fuel", variantsItem.fuel)
        setResult(VEHICLE_CODE_MODEL, data);
        finish();
    }

    override fun onOnOff(modelItem: VehicleModelItem, flag: String) {
        binding.progressBar.visibility = View.VISIBLE
        modelItem.vmId?.let { viewModel.disableModel(it, flag) }
    }

    override fun onOnOffVariants(vehicleVariantsItem: VehicleVariantsItem, flag: String) {
        binding.progressBar.visibility = View.VISIBLE
        vehicleVariantsItem.vvId?.let { viewModel.disableVariants(it, flag) }
        viewModel.isOnOffVariant.observe(this, Observer {
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
                    viewModel.getStoredOutletObject()?.outletId?.let {
                        vehiclesItem?.vehicleId?.let { it1 ->
                            viewModel.getVehicleModels(
                                it1,
                                it
                            )
                        }
                    }
//                     Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })
    }
}