package com.vendor.mastergarage.ui.outerui.schedulepickup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.PickUpDriverAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityPickUpDropDriverBinding
import com.vendor.mastergarage.databinding.DeleteDialogBoxBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.DriversItem
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.fragment_main.account.all.team.DriverFragment
import com.vendor.mastergarage.ui.outerui.serviceadvisor.ServiceAdvisorActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PickUpDropDriverActivity : AppCompatActivity(), PickUpDriverAdapter.OnItemClickListener {

    lateinit var binding: ActivityPickUpDropDriverBinding

    private val viewModel: PickUpDriverViewModel by viewModels()
    lateinit var driverAdapter: PickUpDriverAdapter

    private var leadId: Int? = null
    private var status: Int? = null
    private var notifyBtn: Int = 1

    var filterList: ArrayList<DriversItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickUpDropDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }


        leadId = intent.getIntExtra("leadId", -1)
        status = intent.getIntExtra("status", -1)

        Log.e("leadId", leadId.toString())


//        viewModel.getStoredOutletObject()?.let {
//            it.outletId?.let { it1 ->
//                binding.progressBar.visibility = View.VISIBLE
//                viewModel.getDrivers(it1)
//            }
//        }

        viewModel.driver.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val vItem = it.data as MutableList<DriversItem>
                    filterList = ArrayList()
                    filterList!!.addAll(vItem)
                    notifyBtn = 1
                    driverAdapter = PickUpDriverAdapter(this@PickUpDropDriverActivity, vItem, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = driverAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@PickUpDropDriverActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                    driverAdapter.setNotifyBtn(notifyBtn)
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.addDriver.setOnClickListener {
            val intent = Intent(this, AddDriverActivity::class.java)
            startActivity(intent)
        }

        binding.editBtn.setOnClickListener {
            Toast.makeText(this, "editBtn", Toast.LENGTH_SHORT)
                .show()
            binding.cancelBtn.visibility = View.VISIBLE
            binding.editBtn.visibility = View.INVISIBLE
            notifyBtn = 2
            driverAdapter.setNotifyBtn(notifyBtn)

        }

        binding.cancelBtn.setOnClickListener {
            Toast.makeText(this, "cancelBtn", Toast.LENGTH_SHORT)
                .show()
            binding.cancelBtn.visibility = View.INVISIBLE
            binding.editBtn.visibility = View.VISIBLE
            notifyBtn = 1
            driverAdapter.setNotifyBtn(notifyBtn)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getStoredOutletObject()?.let {
            it.outletId?.let { it1 ->
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getDrivers(it1)
            }
        }
        binding.cancelBtn.visibility = View.INVISIBLE
        binding.editBtn.visibility = View.VISIBLE
    }

    override fun onItemDeleteClick(driversItem: DriversItem) {
        confirmBox(this, driversItem)

    }

    private fun confirmBox(
        context: Context,
        driversItem: DriversItem
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val bindingBox: DeleteDialogBoxBinding = DeleteDialogBoxBinding.inflate(inflater)
        dialogBuilder.setView(bindingBox.root)

        val alertDialog = dialogBuilder.create()
        bindingBox.title.text =
            "Do you want to delete '${driversItem.firstName} ${driversItem.lastName}'"
        bindingBox.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


        bindingBox.deleteBtn.setOnClickListener {
            driversItem.driverId?.let { it1 -> viewModel.deleteDriver(it1) }
            bindingBox.progressBar.visibility = View.VISIBLE
            bindingBox.cancelBtn.visibility = View.INVISIBLE
            bindingBox.deleteBtn.visibility = View.INVISIBLE

            viewModel.delete.observe(this, Observer {
                when (it) {
                    is Response.Success -> {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {
                                filterList?.remove(driversItem)
                                filterList?.let { it1 -> driverAdapter.setFilter(it1) }
                            }
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                                .show()
                            alertDialog.dismiss()
                        }
                    }
                    is Response.Failure -> {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, it.errorMessage.toString())
                        binding.progressBar.visibility = View.GONE
                        bindingBox.progressBar.visibility = View.GONE
                        bindingBox.cancelBtn.visibility = View.VISIBLE
                        bindingBox.deleteBtn.visibility = View.VISIBLE
                    }
                }
            })
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onItemEditClick(position: Int, driversItem: DriversItem) {
        val intent = Intent(this, EditDriverActivity::class.java)
        intent.putExtra("driversItem", driversItem)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "PickUpDropDriverActivity"
    }

    override fun onSelect(driversItem: DriversItem, flag: String) {
        ModelPreferencesManager.put(driversItem, Constraints.DRIVER_STORE)
        val data = Intent()
        data.putExtra("driversItem", driversItem)
        data.putExtra("flag", flag)
        setResult(101, data);
        finish();

        val currentDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date()
            )
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
//        if (leadId != -1) {
//            leadId?.let {
//                driversItem.driverId?.let { it1 ->
//                    viewModel.setAssignDriver(
//                        it1,
//                        it,
//                        flag,
//                        currentDate,
//                        currentTime,
//                        Constraints.Schedule_Pick_UP,
//                        "Driver Assigned"
//                    )
//                }
//            }
//        } else {
//            Toast.makeText(this, "error", Toast.LENGTH_SHORT)
//                .show()
//        }
//
//        viewModel.update.observe(this, Observer {
//            when (it) {
//                is Response.Loading -> {
//                }
//                is Response.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    val vItem = it.data
//                    if (vItem != null) {
//                        if (vItem.success == Constraints.TRUE_INT) {
//                            finish()
//                        }
//                    }
//                    if (vItem != null) {
//                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//                is Response.Failure -> {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
//                    Log.e(TAG, it.errorMessage.toString())
//                }
//            }
//        })

    }
}