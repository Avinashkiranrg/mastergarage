package com.vendor.mastergarage.ui.outerui.jobcard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.BodyDamagedAdapter
import com.vendor.mastergarage.adapters.InventoryAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.SAVE_JOB_CARD
import com.vendor.mastergarage.databinding.ActivityJobCardBinding
import com.vendor.mastergarage.model.*
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.utlis.toFormattedString
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class JobCardActivity : AppCompatActivity(), InventoryAdapter.OnItemClickListener,
    BodyDamagedAdapter.OnItemClickListener {

    private var leadId: Int? = null
    private var vehicleId: Int? = null
    private var manufacturerName: String? = null
    private var fuelType: String? = null
    private var registrationNo: String? = null
    private var variants: String? = null

    lateinit var binding: ActivityJobCardBinding


    private val viewModel: JobCardViewModel by viewModels()

    lateinit var inventoryAdapter: InventoryAdapter

    //    lateinit var inventoryAdapter: InventoryAdapter
    lateinit var bodydamagedAdapter: BodyDamagedAdapter

    private var fuelPercentage: Int? = null

    private var damagesItem: ArrayList<DamagedItem>? = null

    private var inventoryItem: ArrayList<InventoryItem>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inventoryItem = ArrayList()
        damagesItem = ArrayList()

        binding.title.setOnClickListener {
            onBackPressed()
        }
        try {
            leadId = intent.getIntExtra("leadId", -1)
            vehicleId = intent.getIntExtra("vehicleId", -1)
            manufacturerName = intent.getStringExtra("manufacturerName")
            fuelType = intent.getStringExtra("fuelType")
            registrationNo = intent.getStringExtra("registrationNo")
            variants = intent.getStringExtra("variants")

            val p = "## ## ## ####"
            binding.registrationNumber.text =
                registrationNo?.toFormattedString(p)

        } catch (n: NullPointerException) {

        }

        binding.kmsDrivenHead.setOnClickListener {
            if (binding.kmsDriven.visibility != View.VISIBLE) {
                binding.kmsDrivenHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.kmsDriven.visibility = View.VISIBLE

            } else {
                binding.kmsDrivenHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.kmsDriven.visibility = View.GONE

            }
        }

        binding.fuelHead.setOnClickListener {
            if (binding.sliderHead.visibility != View.VISIBLE) {
                binding.fuelHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.sliderHead.visibility = View.VISIBLE

            } else {
                binding.fuelHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.sliderHead.visibility = View.GONE

            }
        }
        binding.bodyDamageHead.setOnClickListener {
            if (binding.recyclerView.visibility != View.VISIBLE) {
                binding.bodyDamageHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.recyclerView.visibility = View.VISIBLE

            } else {
                binding.bodyDamageHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.recyclerView.visibility = View.GONE

            }
        }
        binding.checkListHead.setOnClickListener {
            if (binding.recyclerViewCheckList.visibility != View.VISIBLE) {
                binding.checkListHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.recyclerViewCheckList.visibility = View.VISIBLE

            } else {
                binding.checkListHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.recyclerViewCheckList.visibility = View.GONE

            }
        }
        binding.instructionHead.setOnClickListener {
            if (binding.serviceInstruction.visibility != View.VISIBLE) {
                binding.instructionHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.serviceInstruction.visibility = View.VISIBLE

            } else {
                binding.instructionHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
                binding.serviceInstruction.visibility = View.GONE

            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                fuelPercentage = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        viewModel.updateStatus.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val updateStatus = it.data
                    if (updateStatus != null) {
                        if (updateStatus.updateStatus == Constraints.SAVE_JOB_CARD) {
                            finish()
                        }
                    }

                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.inventoryChecklist.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<InventoryItem>
                    vItem.forEach { it1 ->
                        it1.counter = 1
                        it1.boolean = false
                        inventoryItem!!.add(it1)
                    }
                    inventoryAdapter = InventoryAdapter(this, inventoryItem!!, this)
                    binding.recyclerViewCheckList.apply {
                        setHasFixedSize(true)
                        adapter = inventoryAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@JobCardActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })
        viewModel.damages.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<DamagedItem>
                    bodydamagedAdapter = BodyDamagedAdapter(this, vItem, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = bodydamagedAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@JobCardActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })


        binding.linkQrBtn.setOnClickListener {
            intentLauncher.launch(Intent(this, LinkQrActivity::class.java))
        }

        binding.uploadImage.setOnClickListener {
            val intent = Intent(this, ImageUploadActivity::class.java)
            intent.putExtra("leadId", leadId)
            startActivity(intent)
        }

        binding.upload.setOnClickListener {
            if (binding.kmsDriven.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter kms", Toast.LENGTH_SHORT)
                    .show()
            } else if (fuelPercentage == null) {
                Toast.makeText(this, "Select fuel", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.serviceInstruction.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter service instruction", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.upload.visibility = View.INVISIBLE

                sendDataToServer()
            }
        }

        viewModel.jobCard.observe(this, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            val intent = Intent(this, VehicleReceivedActivity::class.java)
                            intent.putExtra("leadId", leadId)
                            intent.putExtra("variants", variants)
                            intent.putExtra("fuelType", fuelType)
                            intent.putExtra("manufacturerName", manufacturerName)
                            intent.putExtra("registrationNo", registrationNo)
                            startActivity(intent)
                            finish()
                        } else {
                            binding.upload.visibility = View.VISIBLE
                        }
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(this, "upload all data", Toast.LENGTH_SHORT)
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                    binding.progressBar.visibility = View.GONE
                    binding.upload.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun sendDataToServer() {
        val inventoryItemList = ArrayList<InventoryItem>()
        inventoryItem?.forEach {
            if (it.boolean) {
                inventoryItemList.add(it)
            }
        }
        val gson = Gson()
        val json_damaged_string = gson.toJson(damagesItem)
        val json_inventory_string = gson.toJson(inventoryItemList)

        Log.e("json_damaged_string", json_damaged_string)
        Log.e("json_inventory_string", json_inventory_string)

        val currentDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date()
            )
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
            Date()
        )

        if (json_damaged_string != null)
            if (json_inventory_string != null) {
                fuelPercentage?.let {
                    leadId?.let { it1 ->
                        binding.upload.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE

                        viewModel.uploadJob(
                            it1,
                            json_damaged_string,
                            json_inventory_string,
                            currentDate,
                            currentTime,
                            SAVE_JOB_CARD,
                            "Vehicle Received",
                            binding.kmsDriven.text.toString().toInt(),
                            it,
                            binding.serviceInstruction.text.toString()
                        )
                    }
                }
            }
    }


    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Constraints.QR_SCANNER_CODE) {
                val data = result.data?.getStringExtra("data")
//                Toast.makeText(this, data, Toast.LENGTH_SHORT)
//                    .show()
            }
        }


    companion object {
        private const val TAG = "JobCardActivity"
    }

    override fun onItemInventory(item: InventoryItem, boolean: Boolean) {

    }

    override fun onResume() {
        super.onResume()
        leadId?.let { viewModel.getUpdateStatus(it) }
    }

    override fun onItemDamaged(bodyDamagedItem: DamagedItem, boolean: Boolean) {
        if (boolean) {
            damagesItem?.add(bodyDamagedItem)
        } else {
            damagesItem?.remove(bodyDamagedItem)
        }
    }
}