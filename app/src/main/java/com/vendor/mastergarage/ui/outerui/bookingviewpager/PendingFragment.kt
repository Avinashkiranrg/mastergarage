package com.vendor.mastergarage.ui.outerui.bookingviewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.PendingAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.DeleteDialogBoxBinding
import com.vendor.mastergarage.databinding.FragmentPendingBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.LeadsItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.VehicleDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PendingFragment : Fragment(), PendingAdapter.OnItemClickListener {
    lateinit var binding: FragmentPendingBinding

    private val viewModel: PendingViewModel by viewModels()

    private var size: Int? = null

    var filterList: ArrayList<LeadsItem?>? = null

    private lateinit var pendingAdapter: PendingAdapter

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPendingBinding.inflate(inflater, container, false)

        viewModel.leads.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val vItem = it.data.result as MutableList<LeadsItem>
                        size = vItem.size
                        vItem.sortBy { it1 -> it1.leadId }
                        vItem.reverse()
                        filterList = ArrayList()
                        filterList!!.addAll(vItem)
                        pendingAdapter = PendingAdapter(requireActivity(), vItem, this)
                        binding.recyclerView.apply {
                            setHasFixedSize(true)
                            adapter = pendingAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                        }
                    } else {
                        Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        return binding.root
    }

    companion object {
        private const val TAG = "PendingFragment"
    }

    override fun onItemClick(leadItem: LeadsItem) {
        val intent = Intent(requireActivity(), VehicleDetailsActivity::class.java)
        intent.putExtra("leadItem", leadItem)
        requireActivity().startActivity(intent)
    }

    override fun onDecline(leadItem: LeadsItem) {
        confirmBoxDecline(requireActivity(), leadItem)
    }

    private fun confirmBoxDecline(context: Context, leadItem: LeadsItem) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val bindingBox: DeleteDialogBoxBinding = DeleteDialogBoxBinding.inflate(inflater)
        dialogBuilder.setView(bindingBox.root)

        val alertDialog = dialogBuilder.create()
        bindingBox.title.text =
            "Do you want to decline this request'"
        bindingBox.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


        bindingBox.deleteBtn.setOnClickListener {
            leadItem.leadId?.let { it1 -> viewModel.declineLeads(it1) }
            bindingBox.progressBar.visibility = View.VISIBLE
            bindingBox.cancelBtn.visibility = View.INVISIBLE
            bindingBox.deleteBtn.visibility = View.INVISIBLE

            viewModel.onDecline.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Response.Loading -> {
                        Toast.makeText(
                            requireActivity(),
                            "Loading",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Response.Success -> {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {
                                filterList?.remove(leadItem)
                                filterList?.let { it1 -> pendingAdapter.setFilter(it1) }
                            }
                            Toast.makeText(requireActivity(), vItem.message, Toast.LENGTH_SHORT)
                                .show()
                            alertDialog.dismiss()
                        }
                    }
                    is Response.Failure -> {
                        Toast.makeText(
                            requireActivity(),
                            it.errorMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.e(TAG, it.errorMessage.toString())
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

    override fun onItemConfirm(leadItem: LeadsItem, currentDate: String, currentTime: String) {
        val last_up_date = currentDate
        val last_up_time = currentTime
        val booking_date = leadItem.bookingDate
        val booking_time = leadItem.bookingTime
        val outletId = leadItem.outletId
        val vehicleId = leadItem.vehicleId
        val leadId = leadItem.leadId
        val addressId = leadItem.addressId
        if (outletId != null) {
            if (vehicleId != null) {
                if (leadId != null) {
                    if (addressId != null) {
                        if (booking_date != null) {
                            if (booking_time != null) {
                                viewModel.setOnGoing(
                                    last_up_date,
                                    last_up_time,
                                    booking_date,
                                    booking_time,
                                    outletId,
                                    vehicleId,
                                    addressId,
                                    leadId
                                )

                                viewModel.liveData.observe(viewLifecycleOwner, Observer {
                                    when (it) {
                                        is Response.Loading -> {
                                            Toast.makeText(
                                                requireActivity(),
                                                "Loading",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        is Response.Success -> {
                                            val vItem = it.data
                                            if (vItem != null) {
                                                if (vItem.success == TRUE_INT) {
                                                    val intent =
                                                        Intent(
                                                            requireActivity(),
                                                            VehicleDetailsActivity::class.java
                                                        )
                                                    intent.putExtra("leadItem", leadItem)
                                                    intent.putExtra("status", "2")
                                                    requireActivity().startActivity(intent)
                                                } else {
                                                    viewModel.loadUi()
                                                }
                                            }
                                        }
                                        is Response.Failure -> {
                                            Toast.makeText(
                                                requireActivity(),
                                                it.errorMessage,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            Log.e(TAG, it.errorMessage.toString())
                                        }
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUi()
    }
}