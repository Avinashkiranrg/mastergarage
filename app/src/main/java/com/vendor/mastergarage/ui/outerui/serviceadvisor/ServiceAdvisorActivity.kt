package com.vendor.mastergarage.ui.outerui.serviceadvisor

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.ChooseServiceAdvisorAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.CHOOSE_SERVICE_ADVISOR
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.ActivityServiceAdvisorBinding
import com.vendor.mastergarage.databinding.DeleteDialogBoxBinding
import com.vendor.mastergarage.model.ServiceAdvisorItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ServiceAdvisorActivity : AppCompatActivity(),
    ChooseServiceAdvisorAdapter.OnItemClickListener {
    lateinit var binding: ActivityServiceAdvisorBinding

    private lateinit var serviceAdaptor: ChooseServiceAdvisorAdapter

    private val viewModel: MainServiceAdvisorViewModel by viewModels()

    private var size: Int? = null

    var filterList: ArrayList<ServiceAdvisorItem?>? = null

    private var leadId: Int? = null
    private var status: Int? = null
    private var notifyBtn: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceAdvisorBinding.inflate(layoutInflater)
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
//                viewModel.getServiceAdvisor(it1)
//            }
//        }
        viewModel.serviceAdvisor.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data != null) {
                        if (it.data.success == TRUE_INT) {
                            val vItem = it.data.result as MutableList<ServiceAdvisorItem>
                            size = vItem.size
                            filterList = ArrayList()
                            filterList!!.addAll(vItem)
                            notifyBtn = 1
                            serviceAdaptor =
                                ChooseServiceAdvisorAdapter(this, vItem, this)
                            binding.recyclerView.apply {
                                setHasFixedSize(true)
                                adapter = serviceAdaptor
                                layoutManager =
                                    LinearLayoutManager(
                                        this@ServiceAdvisorActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                            }
                            serviceAdaptor.setNotifyBtn(notifyBtn)
                        } else {
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.editBtn.setOnClickListener {
            Toast.makeText(this, "editBtn", Toast.LENGTH_SHORT)
                .show()
            binding.cancelBtn.visibility = View.VISIBLE
            binding.editBtn.visibility = View.INVISIBLE
            notifyBtn = 2
            serviceAdaptor.setNotifyBtn(notifyBtn)

        }

        binding.cancelBtn.setOnClickListener {
            Toast.makeText(this, "cancelBtn", Toast.LENGTH_SHORT)
                .show()
            binding.cancelBtn.visibility = View.INVISIBLE
            binding.editBtn.visibility = View.VISIBLE
            notifyBtn = 1
            serviceAdaptor.setNotifyBtn(notifyBtn)
        }

        binding.addAdvisor.setOnClickListener {
            val intent = Intent(this, AddServiceAdvisorActivity::class.java)
            startActivity(intent)
        }

        viewModel.update.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val vItem = it.data
                    if (vItem?.success == TRUE_INT) {
                        finish()
                    }
                    if (vItem != null) {
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
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

    }

    override fun onResume() {
        super.onResume()
        viewModel.getStoredOutletObject()?.let {
            it.outletId?.let { it1 ->
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getServiceAdvisor(it1)
            }
        }
    }


    companion object {
        private const val TAG = "ServiceAdvisorActivity"
    }

    override fun onItemDeleteClick(serviceAdvisorItem: ServiceAdvisorItem) {
        confirmBox(this, serviceAdvisorItem)

    }

    private fun confirmBox(
        context: Context,
        serviceAdvisorItem: ServiceAdvisorItem
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val bindingBox: DeleteDialogBoxBinding = DeleteDialogBoxBinding.inflate(inflater)
        dialogBuilder.setView(bindingBox.root)

        val alertDialog = dialogBuilder.create()
        bindingBox.title.text =
            "Do you want to delete '${serviceAdvisorItem.firstName} ${serviceAdvisorItem.lastName}'"
        bindingBox.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


        bindingBox.deleteBtn.setOnClickListener {
            serviceAdvisorItem.advisorId?.let { it1 -> viewModel.deleteAdvisor(it1) }
            bindingBox.progressBar.visibility = View.VISIBLE
            bindingBox.cancelBtn.visibility = View.INVISIBLE
            bindingBox.deleteBtn.visibility = View.INVISIBLE

            viewModel.delete.observe(this, Observer {
                when (it) {
                    is Response.Success -> {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {
                                filterList?.remove(serviceAdvisorItem)
                                filterList?.let { it1 -> serviceAdaptor.setFilter(it1) }
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

    override fun onItemEditClick(position: Int, serviceAdvisorItem: ServiceAdvisorItem) {
        val intent = Intent(this, EditServiceAdvisorActivity::class.java)
        intent.putExtra("serviceAdvisorItem", serviceAdvisorItem)
        startActivity(intent)

    }

    override fun onSelect(serviceAdvisorItem: ServiceAdvisorItem, flag: String) {
        val currentDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date()
            )
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        if (leadId != -1) {
            when (status) {
                1 -> {
                    leadId?.let {
                        serviceAdvisorItem.advisorId?.let { it1 ->
                            viewModel.setAssignAdvisor(
                                it1,
                                it,
                                flag,
                                currentDate,
                                currentTime,
                                CHOOSE_SERVICE_ADVISOR,
                                "Advisor Assigned"
                            )
                        }
                    }
                }
                2 -> {
                    leadId?.let {
                        serviceAdvisorItem.advisorId?.let { it1 ->
                            viewModel.setAssignAdvisorChange(
                                it1,
                                it,
                                flag,
                                currentDate,
                                currentTime
                            )
                        }
                    }
                }
                else -> {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }

    }
}