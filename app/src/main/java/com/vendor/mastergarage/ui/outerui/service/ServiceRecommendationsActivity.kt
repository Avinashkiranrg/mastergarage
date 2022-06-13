package com.vendor.mastergarage.ui.outerui.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.ServiceRecommAdapter
import com.vendor.mastergarage.databinding.ActivityServiceRecommendationsBinding
import com.vendor.mastergarage.model.ServiceDetails
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceRecommendationsActivity : AppCompatActivity(),
    ServiceRecommAdapter.OnItemClickListener {

    private var leadId: Int? = null
    lateinit var binding: ActivityServiceRecommendationsBinding

    val viewModel: ServiceRecomViewModel by viewModels()
    var filterList: ArrayList<ServiceDetails?>? = null

    private lateinit var serviceAdaptor: ServiceRecommAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceRecommendationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        try {
            leadId = intent.getIntExtra("leadId", -1)
            viewModel.getServiceRecomm(leadId!!)

        } catch (n: NullPointerException) {

        }

        binding.addMoreService.setOnClickListener {
            val intent = Intent(this, AddServiceActivity::class.java)
            intent.putExtra("leadId", leadId)
            startActivity(intent)
        }

        binding.getFromOthers.setOnClickListener {
            val intent = Intent(this, ServiceRecommendations::class.java)
            intent.putExtra("leadId", leadId)
            startActivity(intent)
        }
        viewModel.service.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val vItem = it.data as MutableList<ServiceDetails>
                    filterList = ArrayList()
                    filterList!!.addAll(vItem)
                    serviceAdaptor =
                        ServiceRecommAdapter(this, vItem, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = serviceAdaptor
                        layoutManager =
                            LinearLayoutManager(
                                this@ServiceRecommendationsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getServiceRecomm(leadId!!)
    }

    companion object {
        private const val TAG = "ServiceRecommendationsActivity"
    }

    override fun onSelect(serviceDetails: ServiceDetails) {
        val intent = Intent(this, EditRecommServiceActivity::class.java)
        intent.putExtra("serviceDetails", serviceDetails)
        startActivity(intent)
    }
}