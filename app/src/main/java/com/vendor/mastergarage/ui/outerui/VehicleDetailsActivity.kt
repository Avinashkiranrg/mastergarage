package com.vendor.mastergarage.ui.outerui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.ACCEPT_REQUEST
import com.vendor.mastergarage.constraints.Constraints.Companion.CHOOSE_SERVICE_ADVISOR
import com.vendor.mastergarage.constraints.Constraints.Companion.OUT_OF_DELIVERY
import com.vendor.mastergarage.constraints.Constraints.Companion.OUT_OF_DELIVERY_OTP_VERIFY
import com.vendor.mastergarage.constraints.Constraints.Companion.PAYMENT_PROCESS
import com.vendor.mastergarage.constraints.Constraints.Companion.SAVE_JOB_CARD
import com.vendor.mastergarage.constraints.Constraints.Companion.SCHEDULE_DROP_OFF
import com.vendor.mastergarage.constraints.Constraints.Companion.UPDATE_STATUS_INSPECTION
import com.vendor.mastergarage.constraints.Constraints.Companion.WORK_IN_PROGRESS
import com.vendor.mastergarage.databinding.ActivityVehicleDetails6Binding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.*
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.notifications.NotificationUi
import com.vendor.mastergarage.ui.outerui.fragment_main.booking.BookingFragment.Companion.NUM_TABS
import com.vendor.mastergarage.ui.outerui.jobcard.JobCardActivity
import com.vendor.mastergarage.ui.outerui.jobcard.JobCardDetailsActivity
import com.vendor.mastergarage.ui.outerui.jobcard.VehicleDeliveredActivity
import com.vendor.mastergarage.ui.outerui.schedulepickup.SchedulePickUpActivity
import com.vendor.mastergarage.ui.outerui.schedulepickup.SchedulePickUpActivity.Companion.DROP
import com.vendor.mastergarage.ui.outerui.schedulepickup.SchedulePickUpActivity.Companion.PICK_UP
import com.vendor.mastergarage.ui.outerui.serviceadvisor.ServiceAdvisorActivity
import com.vendor.mastergarage.ui.outerui.viewdetailsviewpager.PaymentInfoFragment
import com.vendor.mastergarage.ui.outerui.viewdetailsviewpager.ServiceInfoFragment
import com.vendor.mastergarage.ui.outerui.viewdetailsviewpager.VehicleInfoFragment
import com.vendor.mastergarage.ui.outerui.viewdetailsviewpager.jobCardInterfaceListener
import com.vendor.mastergarage.utlis.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class VehicleDetailsActivity : AppCompatActivity(), jobCardInterfaceListener {
    lateinit var binding: ActivityVehicleDetails6Binding
    val animalsArray = arrayOf(
        "Vehicle Info",
        "Service Info",
        "Payment Info"
    )

    private var dropType: Int? = null

    @Inject
    lateinit var vendorPreference: VendorPreference

    private var leadId: Int? = null
    private var vehicleId: Int? = null
    private var addressId: Int? = null
    private var manufacturerName: String? = null
    private var model: String? = null
    private var fuelType: String? = null

    //    private var registrationNo: String? = null
//    private var variants: String? = null
//    private var classicNo: String? = null
//    private var engineNo: String? = null

    private val viewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleDetails6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notification.setOnClickListener {
            val bottomSheetFragment: BottomSheetDialogFragment = NotificationUi()
            bottomSheetFragment.show(supportFragmentManager, NotificationUi.TAG)
        }

        dropType = -1

        try {

            val leadsItem = intent.getParcelableExtra<ResultOnGoing>("leadItem")
            val onGoingDataItem = intent.getParcelableExtra<OnGoingDataItem>("onGoingDataItem")
            val onDeliveredItem = intent.getParcelableExtra<OnDeliveredItem>("onDeliveredItem")
            val status = intent.getStringExtra("status")

            if (leadsItem != null) {

                leadId = leadsItem.leadId
                vehicleId = leadsItem.vehicleId
                addressId = leadsItem.addressId
                binding.estimatedTotal.setText("₹ ${leadsItem.totalCost}")

//                leadId?.let { viewModel.getLeadsById(it) }
                try {
                    if (leadId != null) {
                        pager(0)
                    }
                } catch (e: NullPointerException) {

                }
//                variants = leadsItem.variants
                fuelType = leadsItem.fuelType
                manufacturerName = leadsItem.manufacturer_name
               // model = leadsItem.model.toString()

//                registrationNo = leadsItem.registrationNo
//                engineNo = leadsItem.engineNo
//                classicNo = leadsItem.classicNo

                binding.carFuelType.text = leadsItem.fuelType
                binding.carName.text = leadsItem.manufacturer_name
//                binding.name.text = leadsItem.owner_name
                val p1 = "### ###"
                binding.city.text =
                    "${leadsItem.city_owner}-${leadsItem.pin_code.toString().toFormattedString(p1)}"
//                binding.registrationNumber.text = leadsItem.registrationNo
//                val p = "## ## ## ####"
//                binding.registrationNumber.text =
//                    leadsItem.registrationNo?.toFormattedString(p)
                try {
//                    leadsItem.v_imageUri?.let { binding.imageView.imageFromUrl(it) }

                    val bitmap = leadsItem.v_imageUri?.let { assetsToBitmapModel(it) }
                    bitmap?.apply {
                        binding.imageView.setImageBitmap(this)
                    }

                    leadsItem.imageUri?.let { binding.userProfile.imageFromUrl(it) }
                } catch (e: NullPointerException) {

                }
                try {
                    if (status.equals("2")) {
                        binding.constraintLayout11.visibility = View.GONE
                        binding.constraintLayout66.visibility = View.VISIBLE
                    } else {
                        val mills: Long = calculateMillis(leadsItem)
                        if (mills == 0L) {
                            binding.constraintLayout11.visibility = View.GONE
                        } else {
                            binding.constraintLayout11.visibility = View.VISIBLE
                        }
                        object : CountDownTimer(mills, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                val seconds: Long = millisUntilFinished / 1000
                                val minutes = seconds / 60
                                val hours = minutes / 60
                                val days = hours / 24
                                val time =
                                    "${minutes % 60} min ${seconds % 60} sec"
                                binding.timeLeft.text = time
                            }

                            override fun onFinish() {
                                binding.constraintLayout11.visibility = View.GONE
                            }
                        }.start()
                    }



                    binding.declineBtn.setOnClickListener {
                        leadId?.let { it1 -> onDecline(it1) }
                    }
                    binding.confirmBtn.setOnClickListener {
                        val currentDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date()
                            )
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                            Date()
                        )
                        onItemConfirm(leadsItem, currentDate, currentTime)
                    }
                } catch (n: NullPointerException) {

                }
            }
            if (onGoingDataItem != null) {
                leadId = onGoingDataItem.leadId
                vehicleId = onGoingDataItem.vehicleId
                addressId = onGoingDataItem.addressId
                try {
                    if (leadId != null) {
                        pager(0)
                    }
                } catch (e: NullPointerException) {

                }
                binding.estimatedTotal.setText(
                    "₹ ${
                        onGoingDataItem.paymentInfo?.let {
                            calculateMoney(
                                it
                            )
                        }
                    }"
                )

//                variants = onGoingDataItem.variants
                fuelType = onGoingDataItem.fuelType
                manufacturerName = onGoingDataItem.manufacturerName
                model = onGoingDataItem.model.toString()

//                registrationNo = onGoingDataItem.registrationNo
//                engineNo = onGoingDataItem.engineNo
//                classicNo = onGoingDataItem.classicNo

                binding.carFuelType.text = onGoingDataItem.fuelType
                binding.carName.text = onGoingDataItem.manufacturerName
                binding.name.text = onGoingDataItem.ownerName
                val p1 = "### ###"
                binding.city.text =
                    "${onGoingDataItem.cityOwner}-${
                        onGoingDataItem.pinCode.toString().toFormattedString(p1)
                    }"
//                binding.registrationNumber.text = onGoingDataItem.registrationNo
//                val p = "## ## ## ####"
//                binding.registrationNumber.text =
//                    onGoingDataItem.registrationNo?.toFormattedString(p)
                try {
//                    onGoingDataItem.vImageUri?.let { binding.imageView.imageFromUrl(it) }
                    val bitmap = onGoingDataItem.vImageUri?.let { assetsToBitmapModel(it) }
                    bitmap?.apply {
                        binding.imageView.setImageBitmap(this)
                    }
                    onGoingDataItem.imageUri?.let { binding.userProfile.imageFromUrl(it) }
                } catch (e: NullPointerException) {

                }
                binding.constraintLayout11.visibility = View.GONE

            }
            if (onDeliveredItem != null) {
                leadId = onDeliveredItem.leadId
                vehicleId = onDeliveredItem.vehicleId
                addressId = onDeliveredItem.addressId
                leadId?.let {
//                    viewModel.getLeadsById(it)
//                    viewModel.getUpdateStatus(it)
                }
                try {
                    if (leadId != null) {
                        pager(0)
                    }
                } catch (e: NullPointerException) {

                }

                binding.estimatedTotal.setText(
                    "₹ ${
                        onDeliveredItem.paymentInfo?.let {
                            calculateMoney(
                                it
                            )
                        }
                    }"
                )

//                variants = onDeliveredItem.variants
                fuelType = onDeliveredItem.fuelType
                manufacturerName = onDeliveredItem.manufacturerName
                model = onDeliveredItem.model.toString()

//                registrationNo = onDeliveredItem.registrationNo
//                engineNo = onDeliveredItem.engineNo
//                classicNo = onDeliveredItem.classicNo

                binding.carFuelType.text = onDeliveredItem.fuelType
                binding.carName.text = onDeliveredItem.manufacturerName
                binding.name.text = onDeliveredItem.ownerName
                val p1 = "### ###"
                binding.city.text =
                    "${onDeliveredItem.cityOwner}-${
                        onDeliveredItem.pinCode.toString().toFormattedString(p1)
                    }"
//                binding.registrationNumber.text = onDeliveredItem.registrationNo
//                val p = "## ## ## ####"
//                binding.registrationNumber.text =
//                    onDeliveredItem.registrationNo?.toFormattedString(p)
                try {
//                    onDeliveredItem.vImageUri?.let { binding.imageView.imageFromUrl(it) }

                    val bitmap = onDeliveredItem.vImageUri?.let { assetsToBitmapModel(it) }
                    bitmap?.apply {
                        binding.imageView.setImageBitmap(this)
                    }

                    onDeliveredItem.imageUri?.let { binding.userProfile.imageFromUrl(it) }
                } catch (e: NullPointerException) {

                }
                binding.constraintLayout11.visibility = View.GONE

            }

        } catch (e: Exception) {

        }

        viewModel.updateStatus.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it != null) {
                        val updateStatus = it.data
                        if (updateStatus != null) {
                            if (updateStatus.updateStatus == ACCEPT_REQUEST) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.VISIBLE
                                pager(1)
                            } else if (updateStatus.updateStatus == CHOOSE_SERVICE_ADVISOR) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.VISIBLE
                                dropType = PICK_UP
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.Verification_Successful) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.VISIBLE
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.SAVE_JOB_CARD) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.VISIBLE
                                binding.proceedStatus.tag = SAVE_JOB_CARD
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.UPDATE_STATUS_INSPECTION) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.VISIBLE

                                //show Add Service Recommendations
                                binding.proceedStatus.tag = UPDATE_STATUS_INSPECTION
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.WORK_IN_PROGRESS) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.VISIBLE

                                //show Add Service Recommendations
                                binding.proceedStatus.tag = WORK_IN_PROGRESS
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.VEHICLE_READY) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.GONE
                                binding.constraintLayout621.visibility = View.VISIBLE

                                //hide Add Service Recommendations
//                                binding.proceedStatus.tag = PAYMENT_PROCESS
//                                proceedBtn cash btn
                                pager(1)
                                leadId?.let { it1 -> viewModel.getPaymentInfo(it1) }

                            } else if (updateStatus.updateStatus == Constraints.PAYMENT_PROCESS) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.GONE
                                binding.constraintLayout621.visibility = View.GONE

                                binding.constraintLayout62.visibility = View.VISIBLE

                                // QR code and update btn
                                //hide Add Service Recommendations
                                dropType = DROP
                                binding.proceedBtnScheduleDropOff.tag = PAYMENT_PROCESS
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.SCHEDULE_DROP_OFF) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout621.visibility = View.GONE
                                binding.constraintLayout62.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.VISIBLE

                                //hide Add Service Recommendations
                                binding.proceedStatus.tag = SCHEDULE_DROP_OFF
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.OUT_OF_DELIVERY) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout621.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.VISIBLE

                                //hide Add Service Recommendations
                                binding.proceedStatus.tag = OUT_OF_DELIVERY
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.OUT_OF_DELIVERY_OTP_VERIFY) {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.GONE
                                binding.constraintLayout621.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.VISIBLE

                                //hide Add Service Recommendations
                                binding.proceedStatus.tag = OUT_OF_DELIVERY_OTP_VERIFY
                                pager(1)
                            } else if (updateStatus.updateStatus == Constraints.DELIVERY_VEHICLE) {
                                val last_up_date =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        Date()
                                    )
                                val last_up_time =
                                    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                        Date()
                                    )
                                updatetatu()
                                viewModel.getStoredOutletObject()?.outletId?.let { it1 ->
                                    vehicleId?.let { it2 ->
                                        leadId?.let { it3 ->
                                            addressId?.let { it4 ->
                                                viewModel.closedLeads(
                                                    last_up_date,
                                                    last_up_time,
                                                    Constraints.LEAD_CLOSED,
                                                    "Vehicle Delivered",
                                                    it1,
                                                    it2,
                                                    it4,
                                                    it3
                                                )
                                            }
                                        }
                                    }
                                }

                            } else {
                                binding.constraintLayout11.visibility = View.GONE
                                binding.constraintLayout66.visibility = View.GONE
                                binding.constraintLayout67.visibility = View.GONE
                                binding.constraintLayout68.visibility = View.GONE
                                binding.constraintLayout680.visibility = View.GONE
                                binding.constraintLayout621.visibility = View.GONE
                                binding.constraintLayout62.visibility = View.GONE
                            }
                        }

                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        })


        viewModel.closed.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            val intent = Intent(this, VehicleDeliveredActivity::class.java)
                            intent.putExtra("leadId", leadId)
//                            intent.putExtra("variants", variants)
                            intent.putExtra("fuelType", fuelType)
                            intent.putExtra("manufacturerName", manufacturerName)
                            intent.putExtra("model", model)
//                            intent.putExtra("registrationNo", registrationNo)
                            intentLauncher.launch(intent)
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding.proceedStatus.tag = 1111


        binding.proceedStatus.setOnClickListener {
            val last_up_date =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date()
                )
            val last_up_time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            if (binding.proceedStatus.tag == SAVE_JOB_CARD) {
                try {
                    leadId?.let { it1 ->
                        viewModel.updateStatus(
                            last_up_date,
                            last_up_time,
                            Constraints.UPDATE_STATUS_INSPECTION,
                            "Inspection & Diagnostic ",
                            it1
                        )
                    }
                } catch (n: java.lang.NullPointerException) {

                }
            } else if (binding.proceedStatus.tag == UPDATE_STATUS_INSPECTION) {
                try {
                    leadId?.let { it1 ->
                        viewModel.updateStatus(
                            last_up_date,
                            last_up_time,
                            Constraints.WORK_IN_PROGRESS,
                            "Work in Progress",
                            it1
                        )
                    }
                } catch (n: java.lang.NullPointerException) {

                }
            } else if (binding.proceedStatus.tag == WORK_IN_PROGRESS) {
                try {
                    leadId?.let { it1 ->
                        viewModel.updateStatus(
                            last_up_date,
                            last_up_time,
                            Constraints.VEHICLE_READY,
                            "Vehicle Ready",
                            it1
                        )
                    }
                } catch (n: java.lang.NullPointerException) {

                }
            } else if (binding.proceedStatus.tag == SCHEDULE_DROP_OFF) {
                try {
                    leadId?.let { it1 ->
                        viewModel.updateStatus(
                            last_up_date,
                            last_up_time,
                            Constraints.OUT_OF_DELIVERY,
                            "Out for Delivery",
                            it1
                        )
                    }
                } catch (n: java.lang.NullPointerException) {

                }
            } else if (binding.proceedStatus.tag == OUT_OF_DELIVERY_OTP_VERIFY) {
                try {
                    leadId?.let { it1 ->
                        viewModel.updateStatus(
                            last_up_date,
                            last_up_time,
                            Constraints.DELIVERY_VEHICLE,
                            "Vehicle Delivered",
                            it1
                        )
                    }
                } catch (n: java.lang.NullPointerException) {

                }
            }
        }

        binding.proceedBtn.setOnClickListener {
            updatetatu()
        }
        binding.proceedBtnScheduleDropOff.setOnClickListener {
            try {
                try {
                    val last_up_date =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date()
                        )
                    val last_up_time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                    val outletsItems =
                        ModelPreferencesManager.get<DriversItem>(Constraints.DRIVER_STORE)
                    if (outletsItems != null) {
                        if (outletsItems.driverId != -1) {
                            viewModel.setAssignDropDriver(
                                outletsItems.driverId!!,
                                leadId!!,
                                "1",
                                last_up_date,
                                last_up_time,
                                Constraints.SCHEDULE_DROP_OFF,
                                "Drop Up Scheduled"
                            )
                        }
                    }
                } catch (n: java.lang.NullPointerException) {

                }
            } catch (n: java.lang.NullPointerException) {

            }
        }

        viewModel.inspectionLive.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            leadId?.let { viewModel.getUpdateStatus(it) }
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.ownerDetailLive.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            if (vItem.result?.firstName != null)
                                binding.name.setText("${vItem.result?.firstName} ${vItem.result?.lastName}")
                        } else {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.paymentInfo.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            if (vItem.result?.payment_status == Constraints.TRUE_INT) {
                                updatetatu()
                            }
                        } else {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.userAddressLive.observe(this, Observer {
            val p1 = "### ###"
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            if (vItem.result?.city != null)
                                binding.city.text =
                                    "${vItem.result?.city}-${
                                        vItem.result?.pincode.toString().toFormattedString(p1)
                                    }"
                        } else {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.chooseAdvisor.setOnClickListener {
            val intent = Intent(this, ServiceAdvisorActivity::class.java)
            intent.putExtra("leadId", leadId)
            intent.putExtra("status", 1)
            startActivity(intent)
        }

        binding.proceedSchedulePickUp.setOnClickListener {
            val intent = Intent(this, SchedulePickUpActivity::class.java)
            intent.putExtra("leadId", leadId)
            intent.putExtra("dropType", PICK_UP)
            startActivity(intent)
        }
        binding.proceedJob.setOnClickListener {
            val intent = Intent(this, JobCardActivity::class.java)
            intent.putExtra("leadId", leadId)
//            intent.putExtra("variants", variants)
            intent.putExtra("fuelType", fuelType)
            intent.putExtra("manufacturerName", manufacturerName)
            intent.putExtra("model", model)
//            intent.putExtra("registrationNo", registrationNo)
//            startActivity(intent)
            intentLauncher.launch(intent)
        }

    }

    private fun updatetatu() {
        try {
            val last_up_date =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date()
                )
            val last_up_time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            leadId?.let { it1 ->
                viewModel.updateStatus(
                    last_up_date,
                    last_up_time,
                    Constraints.PAYMENT_PROCESS,
                    "Payment Successful",
                    it1
                )
            }
        } catch (n: NullPointerException) {

        }
    }

    private fun pager(pos: Int) {
        if (leadId != null) {
            initViewPager(leadId!!, pos)
        }
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Constraints.QR_SCANNER_CODE) {
                val data = result.data?.getStringExtra("data")
                Toast.makeText(this, data, Toast.LENGTH_SHORT)
                    .show()
            } else if (result.resultCode == Constraints.BACK_TO_HOME) {
                leadId?.let { viewModel.getUpdateStatus(it) }
            }
        }

    private fun onDecline(leadId: Int) {
        viewModel.declineLeads(leadId)
        viewModel.onDecline.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(
                        this,
                        "Loading",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            finish()
                        } else {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Response.Failure -> {
//                    Toast.makeText(
//                        this,
//                        it.errorMessage,
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })
    }

    private fun onItemConfirm(leadItem: ResultOnGoing, currentDate: String, currentTime: String) {
        val last_up_date = currentDate
        val last_up_time = currentTime
        val booking_date = leadItem.booking_date
        val booking_time = leadItem.booking_time
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

                                viewModel.liveData.observe(this, Observer {
                                    when (it) {
                                        is Response.Loading -> {
                                            Toast.makeText(
                                                this,
                                                "Loading",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        is Response.Success -> {
                                            val vItem = it.data
                                            if (vItem != null) {
                                                if (vItem.success == Constraints.TRUE_INT) {
                                                    binding.constraintLayout66.visibility =
                                                        View.VISIBLE
                                                    binding.constraintLayout11.visibility =
                                                        View.GONE
                                                } else {
                                                    viewModel.loadUi(leadItem.leadId)
                                                }
                                            }
                                        }
                                        is Response.Failure -> {
//                                        Toast.makeText(
//                                            this,
//                                            it.errorMessage,
//                                            Toast.LENGTH_SHORT
//                                        )
//                                            .show()
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
        leadId?.let { viewModel.getUpdateStatus(it) }

        leadId?.let { viewModel.getOwnerDetails(it) }
        leadId?.let { viewModel.getOwnerAddress(it) }
    }

    private fun initViewPager(leadId: Int, select: Int) {
        val adapter =
            ViewPagerAdapter(fragmentManager = supportFragmentManager, lifecycle, leadId)
        binding.viewPage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPage) { tab, position ->
            tab.text = animalsArray[position]
        }.attach()
        binding.tabLayout.getTabAt(select)?.select()
    }

    class ViewPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        leadId: Int
    ) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        private var leadId: Int? = null

        init {
            this.leadId = leadId
        }

        override fun getItemCount(): Int {
            return NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    val fragment = VehicleInfoFragment()
                    val bundle = Bundle()
                    leadId?.let { bundle.putInt("leadId", it) }
                    fragment.arguments = bundle
                    return fragment
                }
                1 -> {
                    val fragment = ServiceInfoFragment()
                    val bundle = Bundle()
                    leadId?.let { bundle.putInt("leadId", it) }
                    fragment.arguments = bundle
                    return fragment
                }
                2 -> {
                    val fragment = PaymentInfoFragment()
                    val bundle = Bundle()
                    leadId?.let { bundle.putInt("leadId", it) }
                    fragment.arguments = bundle
                    return fragment
                }
            }
            return VehicleInfoFragment()
        }
    }

    companion object {
        private const val TAG = "VehicleDetailsActivity"
    }

    override fun onJobCardListener(boolean: Boolean) {
        Toast.makeText(
            this,
            "Received",
            Toast.LENGTH_SHORT
        )
            .show()
        if (boolean) {
            leadId?.let { viewModel.getUpdateStatus(it) }
        }
    }

    override fun onJobCardButtonListener(boolean: Boolean) {
        Toast.makeText(
            this,
            "Received onJobCardButtonListener",
            Toast.LENGTH_SHORT
        )
            .show()
        if (boolean) {
            val intent = Intent(this, JobCardDetailsActivity::class.java)
            intent.putExtra("leadId", leadId)
//            intent.putExtra("variants", variants)
            intent.putExtra("fuelType", fuelType)
            intent.putExtra("manufacturerName", manufacturerName)
            intent.putExtra("manufacturerName", manufacturerName)
//            intent.putExtra("registrationNo", registrationNo)
//            intent.putExtra("engineNo", engineNo)
//            intent.putExtra("classicNo", classicNo)
            intentLauncher.launch(intent)
        }
    }

}


