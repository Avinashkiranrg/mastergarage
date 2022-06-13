package com.vendor.mastergarage.ui.outerui.viewdetailsviewpager

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.ServiceRecommInFragAdapter
import com.vendor.mastergarage.adapters.ServiceRequestedAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.SCHEDULE_DROP_OFF
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.constraints.Constraints.Companion.Verification_Successful
import com.vendor.mastergarage.databinding.FragmentServiceInfoBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.ServiceDetails
import com.vendor.mastergarage.model.ServiceRequestedItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.schedulepickup.SchedulePickUpActivity
import com.vendor.mastergarage.ui.outerui.schedulepickup.SchedulePickUpActivity.Companion.DROP
import com.vendor.mastergarage.ui.outerui.schedulepickup.SchedulePickUpActivity.Companion.PICK_UP
import com.vendor.mastergarage.ui.outerui.service.ServiceRecommendationsActivity
import com.vendor.mastergarage.ui.outerui.serviceadvisor.ServiceAdvisorActivity
import com.vendor.mastergarage.utlis.getFormattedInToFullDate2
import com.vendor.mastergarage.utlis.getFormattedTime
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ServiceInfoFragment : Fragment() {

    lateinit var binding: FragmentServiceInfoBinding

    var jobCardInterfaceListener: jobCardInterfaceListener? = null

    private val viewModel: ServiceInfoViewModel by viewModels()
    private var leadId: Int? = null
    private var vehicleId: Int? = null
    private var dropType: Int? = null

    @Inject
    lateinit var vendorPreference: VendorPreference
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            jobCardInterfaceListener = context as jobCardInterfaceListener
        } catch (ex: Exception) {
            //welp, you screwed up :P
        }
    }

    fun someButtonClicked() {
        jobCardInterfaceListener?.onJobCardListener(true)
//        Toast.makeText(requireActivity(), "Hi jobCardInterfaceListener", Toast.LENGTH_SHORT).show()
    }

    fun jobCardButtonClicked() {
        jobCardInterfaceListener?.onJobCardButtonListener(true)
//        Toast.makeText(requireActivity(), "Hi jobCardInterfaceListener", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceInfoBinding.inflate(inflater, container, false)

        dropType = -1


        try {
            if (arguments != null) {
                leadId = requireArguments().getInt("leadId", -1)
                if (leadId != -1) {
                    Log.e("leadId", leadId.toString())
                    viewModel.getServiceRequested(leadId!!)
                }
            }
        } catch (e: NullPointerException) {

        }


        viewModel.requested.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val vItem = it.data.result as MutableList<ServiceRequestedItem>
                        val savedOutletAdapter = ServiceRequestedAdapter(requireActivity(), vItem)
                        binding.recyclerView.apply {
                            setHasFixedSize(true)
                            adapter = savedOutletAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                        }
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        viewModel.service.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    binding.listCard.visibility = View.VISIBLE
                    binding.addServiceIns.visibility = View.VISIBLE

                    val vItem = it.data as MutableList<ServiceDetails>
                    val serviceAdaptor =
                        ServiceRecommInFragAdapter(requireActivity(), vItem)
                    binding.recyclerView2.apply {
                        setHasFixedSize(true)
                        adapter = serviceAdaptor
                        layoutManager =
                            LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    binding.listCard.visibility = View.GONE
                    binding.addServiceIns.visibility = View.GONE
                    Log.e(TAG, it.errorMessage.toString())
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
                        Toast.makeText(
                            requireActivity(),
                            "${updateStatus.updateId}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        if (updateStatus.updateStatus == Constraints.ACCEPT_REQUEST) {
                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                            viewModel.getServiceInstruction(leadId!!)
                        } else if (updateStatus.updateStatus == Constraints.CHOOSE_SERVICE_ADVISOR) {
//                                viewModel.getAdvisor(leadId)
                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE
                            viewModel.getServiceInstruction(leadId!!)

                        } else if (updateStatus.updateStatus == Constraints.Schedule_Pick_UP) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            Log.e("dropType 1", "PICK_UP ${PICK_UP}")
                            dropType = PICK_UP
                            binding.constraint66.visibility = View.VISIBLE
                            binding.qrcode.visibility = View.GONE
                            binding.editOrCancel.visibility = View.VISIBLE
                            binding.requestOTP.tag = Constraints.Schedule_Pick_UP
                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"

                            viewModel.getServiceInstruction(leadId!!)

                            binding.constraint.visibility = View.VISIBLE
                        } else if (updateStatus.updateStatus == Constraints.Verification_Successful) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            binding.constraint66.visibility = View.VISIBLE
                            dropType = PICK_UP
                            Log.e("dropType 2", "PICK_UP ${PICK_UP}")

                            binding.qrcode.visibility = View.GONE
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.VISIBLE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                            viewModel.getServiceInstruction(leadId!!)

                        } else if (updateStatus.updateStatus == Constraints.SAVE_JOB_CARD) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

//                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            binding.constraint66.visibility = View.GONE

                            binding.qrcode.visibility = View.VISIBLE
                            binding.qrcode.setText("Update QR")
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.VISIBLE

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"

                            viewModel.getServiceInstruction(leadId!!)

                        } else if (updateStatus.updateStatus == Constraints.UPDATE_STATUS_INSPECTION) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

//                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            binding.constraint66.visibility = View.GONE

                            binding.qrcode.visibility = View.VISIBLE
                            binding.qrcode.setText("Update QR")
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.VISIBLE
                            binding.addService.visibility = View.VISIBLE
                            binding.listCard.visibility = View.VISIBLE
                            binding.addServiceIns.visibility = View.VISIBLE

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"

                            viewModel.getServiceInstruction(leadId!!)

                            viewModel.getServiceRecomm(leadId!!)
                        } else if (updateStatus.updateStatus == Constraints.WORK_IN_PROGRESS) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

//                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            binding.constraint66.visibility = View.GONE

                            binding.qrcode.visibility = View.VISIBLE
                            binding.qrcode.setText("Update QR")
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.VISIBLE
                            binding.addService.visibility = View.VISIBLE
                            binding.listCard.visibility = View.VISIBLE
                            binding.addServiceIns.visibility = View.VISIBLE
                            viewModel.getServiceRecomm(leadId!!)

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"

                            viewModel.getServiceInstruction(leadId!!)

                        } else if (updateStatus.updateStatus == Constraints.VEHICLE_READY) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

//                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            binding.constraint66.visibility = View.GONE

                            binding.qrcode.visibility = View.VISIBLE
                            binding.qrcode.setText("Update QR")
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.VISIBLE
                            binding.addService.visibility = View.GONE
                            binding.listCard.visibility = View.GONE
                            binding.addServiceIns.visibility = View.GONE

                            binding.serviceInstructionsHead.visibility = View.GONE
                            binding.materialCardView2.visibility = View.GONE

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                        } else if (updateStatus.updateStatus == Constraints.PAYMENT_PROCESS) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

//                            leadId?.let { it1 -> viewModel.getAssignedDriver(it1) }
                            binding.constraint66.visibility = View.GONE

                            binding.qrcode.visibility = View.VISIBLE
                            binding.qrcode.setText("Update QR")
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.VISIBLE
                            binding.addService.visibility = View.GONE
                            binding.serviceInstructionsHead.visibility = View.GONE
                            binding.materialCardView2.visibility = View.GONE

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                        } else if (updateStatus.updateStatus == Constraints.SCHEDULE_DROP_OFF) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

                            leadId?.let { it1 -> viewModel.getAssignedDriverDrop(it1) }
                            dropType = DROP
                            binding.constraint66.visibility = View.VISIBLE

                            binding.qrcode.visibility = View.GONE
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.VISIBLE
                            binding.addService.visibility = View.GONE
                            binding.editOrCancel.visibility = View.VISIBLE
                            binding.editOrCancel.text = "Edit Drop Info"
                            binding.editOrCancel.tag = SCHEDULE_DROP_OFF

                            binding.serviceInstructionsHead.visibility = View.GONE
                            binding.materialCardView2.visibility = View.GONE


                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                        } else if (updateStatus.updateStatus == Constraints.OUT_OF_DELIVERY) {

                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

                            leadId?.let { it1 -> viewModel.getAssignedDriverDrop(it1) }
                            dropType = DROP

                            binding.constraint66.visibility = View.VISIBLE

                            binding.qrcode.visibility = View.GONE
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.GONE
                            binding.addService.visibility = View.GONE
                            binding.editOrCancel.visibility = View.GONE
                            binding.constraint.visibility = View.VISIBLE
                            binding.requestOTP.tag = Constraints.OUT_OF_DELIVERY
                            binding.serviceInstructionsHead.visibility = View.GONE
                            binding.materialCardView2.visibility = View.GONE
                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                        } else {
                            leadId?.let { it1 -> viewModel.getAssignedAdvisor(it1) }
                            binding.constraintRequest.visibility = View.VISIBLE

                            binding.constraint66.visibility = View.GONE

                            binding.qrcode.visibility = View.GONE
                            binding.editOrCancel.visibility = View.GONE

                            binding.verifyText.visibility = View.GONE

                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.vieJobCard.visibility = View.GONE
                            binding.addService.visibility = View.GONE
                            binding.editOrCancel.visibility = View.GONE
                            binding.constraint.visibility = View.GONE
                            binding.serviceInstructionsHead.visibility = View.GONE
                            binding.materialCardView2.visibility = View.GONE

                            binding.estimatedTotalHead.text = "${updateStatus.updateRemarks}"
                            binding.lastUpdateDate.text =
                                "Last updated on ${updateStatus.lastUpDate}"
                            viewModel.getServiceRecomm(leadId!!)

                        }

                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }
        })

        binding.vieJobCard.setOnClickListener {
            jobCardButtonClicked()
        }
        binding.addService.setOnClickListener {
            val intent = Intent(requireActivity(), ServiceRecommendationsActivity::class.java)
            intent.putExtra("leadId", leadId)
            startActivity(intent)
        }
        viewModel.advisorItem.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem?.success == Constraints.TRUE_INT) {
                        binding.advisorName.text =
                            "Service Advisor - ${vItem.result?.firstName} ${vItem.result?.lastName}"
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }
        })

        viewModel.driver.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem?.success == Constraints.TRUE_INT) {
//                        binding.bKTime.text =
//                            "${vItem.result?.pick_up_date} at ${vItem.result?.pick_up_time}"

                        binding.bKTime.text =
                            "${
                                vItem.result?.pick_up_date?.let { it1 ->
                                    getFormattedInToFullDate2(
                                        it1
                                    )
                                }
                            } at ${vItem.result?.pick_up_time?.let { it1 -> getFormattedTime(it1) }}"
                        binding.driverName.text =
                            "${vItem.result?.firstName} ${vItem.result?.lastName}"
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }
        })

        viewModel.serviceInstruction.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        binding.serviceInstruction.text =
                            "${it.data.result?.instruction}"
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(
                        requireActivity(),
                        "ServiceInstruction ${it.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.driverDrop.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        binding.bKTime.text =
                            "${vItem.dropUpDate} at ${vItem.dropUpTime}"
                        binding.driverName.text =
                            "${vItem.firstName} ${vItem.lastName}"
//                    } else {
//                        val last_up_date =
//                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
//                                Date()
//                            )
//                        val last_up_time =
//                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
//                                Date()
//                            )
//                        val outletsItems =
//                            ModelPreferencesManager.get<DriversItem>(Constraints.DRIVER_STORE)
//                        if (outletsItems != null) {
//                            if (outletsItems.driverId != -1) {
//                                viewModel.setAssignDropDriver(
//                                    outletsItems.driverId!!,
//                                    leadId!!,
//                                    "1",
//                                    last_up_date,
//                                    last_up_time,
//                                    Constraints.SCHEDULE_DROP_OFF,
//                                    "Drop Up Scheduled"
//                                )
//                            }
//                        }
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }
        })

        if (dropType == DROP) {
            Log.e("dropType 5", "PICK_UP ${PICK_UP}")

        } else if (dropType == PICK_UP) {
            Log.e("dropType 6", "PICK_UP ${PICK_UP}")

        } else {
            Log.e("dropType 7", "PICK_UP ${PICK_UP}")
        }

        viewModel.update.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == TRUE_INT) {
                            leadId?.let { it1 -> viewModel.getAssignedDriverDrop(it1) }
                        }
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }
        })
        binding.change.setOnClickListener {
            val intent = Intent(requireActivity(), ServiceAdvisorActivity::class.java)
            intent.putExtra("leadId", leadId)
            intent.putExtra("status", 2)
            startActivity(intent)
        }

        binding.editOrCancel.tag = 515
        binding.editOrCancel.setOnClickListener {
            val intent = Intent(requireActivity(), SchedulePickUpActivity::class.java)
            if (binding.editOrCancel.tag == SCHEDULE_DROP_OFF) {
                intent.putExtra("leadId", leadId)
                intent.putExtra("dropType", DROP)
            } else {
                intent.putExtra("leadId", leadId)
                intent.putExtra("dropType", PICK_UP)
            }
            startActivity(intent)
        }
        binding.requestOTP.tag = 2313
        binding.requestOTP.setOnClickListener {
            binding.editOrCancel.visibility = View.GONE
            binding.constraint.visibility = View.GONE
            Toast.makeText(requireActivity(), "otp sent", Toast.LENGTH_SHORT).show()
            binding.constraint666.visibility = View.VISIBLE
        }

        binding.scanQR.setOnClickListener {
            Toast.makeText(requireActivity(), "otp sent", Toast.LENGTH_SHORT).show()
        }
        binding.resend.setOnClickListener {
            Toast.makeText(requireActivity(), "resend otp", Toast.LENGTH_SHORT).show()
        }

        binding.submit.setOnClickListener {
            if (binding.enterOtp.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Enter otp", Toast.LENGTH_SHORT).show()
            } else {
                val last_up_date =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date()
                    )
                val last_up_time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                    Date()
                )
                try {
                    if (binding.requestOTP.tag == Constraints.OUT_OF_DELIVERY) {
                        try {
                            leadId?.let { it1 ->
                                viewModel.updateStatus(
                                    last_up_date,
                                    last_up_time,
                                    Constraints.OUT_OF_DELIVERY_OTP_VERIFY,
                                    "Delivery Verified",
                                    it1
                                )
                            }
                        } catch (n: java.lang.NullPointerException) {

                        }
                    } else if (binding.requestOTP.tag == Constraints.Schedule_Pick_UP) {
                        leadId?.let { it1 ->
                            viewModel.updateStatus(
                                last_up_date,
                                last_up_time,
                                Verification_Successful,
                                "Pick Up Scheduled",
                                it1
                            )
                        }
                    }
                } catch (n: NullPointerException) {

                }
            }
        }

        viewModel.otpVerify.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == TRUE_INT) {
                            viewModel.loadUi(leadId)
                            binding.constraint666.visibility = View.GONE
                            binding.constraint.visibility = View.GONE

                            someButtonClicked()
                        }
                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })


        return binding.root
    }

    companion object {
        private const val TAG = "ServiceInfoFragment"

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUi(leadId)
    }
}

interface jobCardInterfaceListener {
    fun onJobCardListener(boolean: Boolean)
    fun onJobCardButtonListener(boolean: Boolean)
}