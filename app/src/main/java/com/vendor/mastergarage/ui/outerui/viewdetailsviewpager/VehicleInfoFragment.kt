package com.vendor.mastergarage.ui.outerui.viewdetailsviewpager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.FragmentVehicleinfoBinding
import com.vendor.mastergarage.model.LeadsItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.utlis.toFormattedString
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VehicleInfoFragment : Fragment() {

    lateinit var binding: FragmentVehicleinfoBinding

    private val viewModel: VehicleInfoViewModel by viewModels()

    private var booking_date: String? = null
    private var booking_time: String? = null
    private val pickup_date: String? = null
    private val pickup_time: String? = null
    private var outletId: Int? = null
    private var vehicleId: Int? = null
    private var leadId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVehicleinfoBinding.inflate(inflater, container, false)

        try {
            if (arguments != null) {
                leadId = requireArguments().getInt("leadId", -1)
                if (leadId != -1) {
                    Log.e("leadId", leadId.toString())
                    viewModel.getLeadsById(leadId!!)
                }
            }
        } catch (e: NullPointerException) {

        }

        viewModel.leads.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {

                    val vItem = it.data
                    if (vItem?.success == Constraints.TRUE_INT) {
                        val leadsItem = it.data.result as LeadsItem
                        Log.e("leadsItem", leadsItem.toString())


                        booking_date = leadsItem.bookingDate
                        booking_time = leadsItem.bookingTime
                        outletId = leadsItem.outletId
                        vehicleId = leadsItem.vehicleId
                        leadId = leadsItem.leadId
                        binding.carFuelType.text = leadsItem.fuelType
                        binding.carMake.text = leadsItem.manufacturerName
//                        binding.customerNam.text = leadsItem.owner_name
                        val p1 = "### ###"
                        binding.address.text = "${leadsItem.address} ${
                            leadsItem.pin_code.toString().toFormattedString(p1)
                        }"

                        binding.bKTime.text =
                            "${leadsItem.bookingDate} at ${leadsItem.bookingTime}"

                        binding.city.text =
                            "${leadsItem.city}"

                        binding.carModel.text = leadsItem.model
//                        binding.classicNo.text = leadsItem.classicNo
//                        binding.engineNo.text = leadsItem.engineNo
//                        binding.carColor.text = leadsItem.color
//
//                        binding.insuranceCompany.text = leadsItem.company
//                        binding.insuranceName.text = leadsItem.insurance_number
//                        binding.insuranceType.text = leadsItem.insurance_type


                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }

        })

        viewModel.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val updateStatus = it.data
                    if (updateStatus != null) {
                        if (updateStatus.success == Constraints.TRUE_INT) {
                            if (updateStatus.update_status == Constraints.ACCEPT_REQUEST) {
//                                binding.constraintLayout11.visibility = View.GONE
//                                binding.constraintLayout66.visibility = View.VISIBLE
                            } else if (updateStatus.update_status == Constraints.CHOOSE_SERVICE_ADVISOR) {
//                                binding.constraintLayout11.visibility = View.GONE
//                                binding.constraintLayout66.visibility = View.GONE
//                                binding.constraintLayout67.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }
        })

        binding.cardView2.setOnClickListener {
            val latitude = 24.2562233
            val longitude = 77.2556856
            val uri =
                "http://maps.google.com/maps?saddr=$latitude,$longitude"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val ShareSub = "Here is my location"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, ShareSub)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        viewModel.ownerDetailLive.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            if (vItem.result?.firstName != null)
                                binding.customerName.setText("${vItem.result?.firstName} ${vItem.result?.lastName}")
                        } else {
                            Toast.makeText(requireActivity(), vItem.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.userAddressLive.observe(viewLifecycleOwner, Observer {
            val p1 = "### ###"
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            if (vItem.result?.city != null) {
                                binding.address.text =
                                    "${vItem.result?.houseNo} " +
                                            "${vItem.result?.address} ${vItem.result.city}- ${
                                                vItem.result?.pincode.toString()
                                                    .toFormattedString(p1)
                                            } ${vItem.result.state}"
                            } else {
                                Toast.makeText(requireActivity(), vItem.message, Toast.LENGTH_SHORT)
                                    .show()

                            }
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        leadId?.let { viewModel.getOwnerDetails(it) }
        leadId?.let { viewModel.getOwnerAddress(it) }

    }
}