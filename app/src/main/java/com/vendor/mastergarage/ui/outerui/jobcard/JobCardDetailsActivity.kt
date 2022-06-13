package com.vendor.mastergarage.ui.outerui.jobcard

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
import com.vendor.mastergarage.adapters.HorizontalImageAdapter
import com.vendor.mastergarage.adapters.ServiceRecommInFragAdapter
import com.vendor.mastergarage.adapters.Vertical2ImageAdapter
import com.vendor.mastergarage.adapters.VerticalImageAdapter
import com.vendor.mastergarage.databinding.ActivityJobCardDetailsBinding
import com.vendor.mastergarage.model.ImageModel
import com.vendor.mastergarage.model.JobCardDamagedItem
import com.vendor.mastergarage.model.JobCardInventoryItem
import com.vendor.mastergarage.model.ServiceDetails
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.service.ServiceRecommendationsActivity
import com.vendor.mastergarage.utlis.toFormattedString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobCardDetailsActivity : AppCompatActivity(), HorizontalImageAdapter.OnItemClickListener {

    lateinit var binding: ActivityJobCardDetailsBinding
    private var leadId: Int? = null
    private var vehicleId: Int? = null
    private var manufacturerName: String? = null
    private var fuelType: String? = null
    private var registrationNo: String? = null
    private var variants: String? = null
    private var engineNo: String? = null
    private var classicNo: String? = null


    private val viewModel: JobCardShowViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        try {
            leadId = intent.getIntExtra("leadId", -1)
            vehicleId = intent.getIntExtra("vehicleId", -1)
            manufacturerName = intent.getStringExtra("manufacturerName")
            fuelType = intent.getStringExtra("fuelType")
            registrationNo = intent.getStringExtra("registrationNo")
            variants = intent.getStringExtra("variants")
            engineNo = intent.getStringExtra("engineNo")
            classicNo = intent.getStringExtra("classicNo")

            val p = "## ## ## ####"
            binding.registrationNumber.text =
                registrationNo?.toFormattedString(p)
            binding.carName.setText("$manufacturerName $variants $fuelType")
            binding.engineNo.setText("$engineNo")
            binding.classicNo.setText("$classicNo")

            viewModel.getAssignedDamaged(leadId!!)
            viewModel.getAssignedInventory(leadId!!)
            viewModel.getJobCard(leadId!!)
            viewModel.getJobCardImage(leadId!!)
            viewModel.getServiceRecomm(leadId!!)
        } catch (n: NullPointerException) {

        }

        viewModel.getImage.observe(this, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<ImageModel>
                    if (vItem.size > 3) {
                        binding.numberOfImage.text = "+${vItem.size - 3} Images"
                    }
                    val discountAdapter = HorizontalImageAdapter(this, vItem, this)
                    binding.recyclerView2.apply {
                        setHasFixedSize(true)
                        adapter = discountAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@JobCardDetailsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                    }

                    val vAdapter = VerticalImageAdapter(this, vItem)
                    binding.imageCount.text = "All Images(${vItem.size})"
                    binding.imageCount2.text = "All Images(${vItem.size})"
                    binding.recyclerView3.apply {
                        setHasFixedSize(true)
                        adapter = vAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@JobCardDetailsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }

                    val v2Adapter = Vertical2ImageAdapter(this, vItem)
                    binding.imageCount2.text = "All Images(${vItem.size})"
                    binding.recyclerView4.apply {
                        setHasFixedSize(true)
                        adapter = v2Adapter
                        layoutManager =
                            LinearLayoutManager(
                                this@JobCardDetailsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })

        viewModel.service.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {

                    val vItem = it.data as MutableList<ServiceDetails>
                    val serviceAdaptor =
                        ServiceRecommInFragAdapter(this, vItem)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = serviceAdaptor
                        layoutManager =
                            LinearLayoutManager(
                                this@JobCardDetailsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })

        binding.fullImage.setOnClickListener {
            binding.constraintLayout52.visibility = View.VISIBLE
            binding.bottom.visibility = View.GONE
        }

        binding.layoutBottom.setOnClickListener {
            binding.layoutHalfImage.visibility = View.VISIBLE
            binding.layoutMinimize.visibility = View.GONE
        }

        binding.closest.setOnClickListener {
            binding.layoutHalfImage.visibility = View.GONE
            binding.layoutMinimize.visibility = View.VISIBLE
        }

        binding.closest2.setOnClickListener {
            binding.layoutHalfImage.visibility = View.GONE
            binding.layoutMinimize.visibility = View.VISIBLE
            binding.constraintLayout52.visibility = View.GONE
            binding.bottom.visibility = View.VISIBLE
        }

        viewModel.damaged.observe(this, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<JobCardDamagedItem>
                    Log.e("JobCardDamagedItem", vItem.toString())

                    val myListAdapter = AssignDamagedAdapter(this, vItem)
                    binding.damagedListView.adapter = myListAdapter

//                    val totalHeight: Int = binding.damagedListView.height
//                    val rowHeight: Int = totalHeight / vItem.size

                }
                is Response.Failure -> {
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })

        viewModel.inventory.observe(this, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<JobCardInventoryItem>
                    Log.e("JobCardInventoryItem", vItem.toString())

                    val myListAdapter = AssignInventoryAdapter(this, vItem)
                    binding.checkListListView.adapter = myListAdapter


                }
                is Response.Failure -> {
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })
        viewModel.jobCard.observe(this, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data
                    Log.e("JobCard", vItem.toString())

                    val progress = vItem?.fuel
                    if (vItem != null) {
                        binding.kmsDriven.setText("${vItem.kmsDriven}")
                        binding.serviceInstruction.setText("${vItem.instruction}")
                    }
                    try {
                        if (progress != null) {
                            binding.progressBar.progress = progress * 25
                        }
                    } catch (e: NullPointerException) {
                        binding.progressBar.progress = 0
                    }

                }
                is Response.Failure -> {
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })

        binding.damagedJob.setOnClickListener {
            if (binding.sliderHead.visibility != View.VISIBLE) {
                binding.damagedJob.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.sliderHead.visibility = View.VISIBLE

            } else {
                binding.damagedJob.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.sliderHead.visibility = View.GONE

            }
        }
        binding.brandedDamage.setOnClickListener {
            if (binding.cardViewKms.visibility != View.VISIBLE) {
                binding.brandedDamage.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.cardViewKms.visibility = View.VISIBLE

            } else {
                binding.brandedDamage.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.cardViewKms.visibility = View.GONE

            }
        }
        binding.checkListHead.setOnClickListener {
            if (binding.checkListRecycler.visibility != View.VISIBLE) {
                binding.checkListHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.checkListRecycler.visibility = View.VISIBLE

            } else {
                binding.checkListHead.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                );
                binding.checkListRecycler.visibility = View.GONE

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

        binding.recommondJob.setOnClickListener {
            if (binding.recommondJobCard.visibility != View.VISIBLE) {
                binding.recommondJob.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                );
                binding.recommondJobCard.visibility = View.VISIBLE
                binding.addService.visibility = View.VISIBLE

            } else {
                binding.recommondJob.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
                binding.recommondJobCard.visibility = View.GONE
                binding.addService.visibility = View.GONE

            }
        }

        binding.addService.setOnClickListener {
            val intent = Intent(this, ServiceRecommendationsActivity::class.java)
            intent.putExtra("leadId", leadId)
            startActivity(intent)
        }

    }

    override fun onItemClick(imageModel: ImageModel) {

    }
}