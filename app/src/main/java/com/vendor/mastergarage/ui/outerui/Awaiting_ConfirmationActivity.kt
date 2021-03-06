package com.vendor.mastergarage.ui.outerui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.AwaitingAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityAwaitingConfirmationBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.AcceptLeadsReq
import com.vendor.mastergarage.model.LeadsItem
import com.vendor.mastergarage.model.OnGoingRespo
import com.vendor.mastergarage.model.ResultOnGoing
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.mainactivity.MainActivity
import com.vendor.mastergarage.ui.outerui.bookingviewpager.ConfirmActivity
import com.vendor.mastergarage.ui.outerui.bookingviewpager.DeclineActivity
import com.vendor.mastergarage.ui.outerui.bookingviewpager.PendingViewModel
import com.vendor.mastergarage.utlis.goToActivitiesFinish
import com.vendor.mastergarage.utlis.goToActivityFinish
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class Awaiting_ConfirmationActivity : AppCompatActivity(), AwaitingAdapter.OnItemClickListener,
    AwaitingAdapter.AcceptClicks, AwaitingAdapter.DeclineClicks {
    @Inject
    lateinit var vendorPreference: VendorPreference
    lateinit var binding: ActivityAwaitingConfirmationBinding
    private val viewModel: PendingViewModel by viewModels()
    private val acceptLeadsviewModel: AcceptLeadsViewModel by viewModels()
    var filterList: ArrayList<LeadsItem?>? = null
    var manager: LinearLayoutManager? = null
    var position: Int = 0
    var resultOngoingConfirm=ResultOnGoing()
    private lateinit var awaitingAdapter: AwaitingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAwaitingConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClicks()

        viewModel.pendingData.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == Constraints.TRUE_INT) {
                        /*  val vItem = it.data.result as MutableList<LeadsItem>
                          size = vItem.size
                          vItem.sortBy { it1 -> it1.leadId }
                          vItem.reverse()*/
//                        filterList = ArrayList()
//                        filterList!!.addAll(vItem)

                        initializeAwaitingSlider(it.data)
                    } else {
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e("Awaiting_ConfirmationActivity.TAG", it.errorMessage.toString())
                }
            }
        })



        acceptLeadsviewModel._aceptLeadsData.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == Constraints.TRUE_INT) {
                        val intent = Intent(this, ConfirmActivity::class.java)
                        intent.putExtra("leadItem", resultOngoingConfirm)
                        Log.e("resultOngoingConfirm",resultOngoingConfirm.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e("Awaiting_ConfirmationActivity.TAG", it.errorMessage.toString())
                }
            }
        })


        acceptLeadsviewModel._declineLeadsData.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == Constraints.TRUE_INT) {
                        val intent = Intent(this, DeclineActivity::class.java)
                        intent.putExtra("leadItem", resultOngoingConfirm)
                        Log.e("resultOngoingConfirm",resultOngoingConfirm.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e("Awaiting_ConfirmationActivity.TAG", it.errorMessage.toString())
                }
            }
        })
    }

    private fun setOnClicks() {
        binding.homeBtn.setOnClickListener {
            goToActivitiesFinish(this@Awaiting_ConfirmationActivity, MainActivity::class.java)
        }
    }

    private fun initializeAwaitingSlider(data: OnGoingRespo) {
        awaitingAdapter = AwaitingAdapter(this, data.result, this, this, this)

        val dots: Array<ImageView?>
        manager = LinearLayoutManager(
            this@Awaiting_ConfirmationActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = awaitingAdapter
            layoutManager =
                manager
        }

        awaitingAdapter.setFilter(data?.result!!)


        var dotscount: Int
        if (data.result.size > 3) {
            dotscount = 3
        } else {
            dotscount = data.result.size
        }
        dots = arrayOfNulls(dotscount)

        binding.sliderDotsComm.removeAllViews()
        for (i in 0 until dotscount) {

            dots[i] = ImageView(this@Awaiting_ConfirmationActivity)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this@Awaiting_ConfirmationActivity,
                    R.drawable.non_active_dot_2
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.sliderDotsComm.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                this@Awaiting_ConfirmationActivity,
                R.drawable.active_dot_3
            )
        )

        binding.recyclerView.setOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var currentCompletelyVisibleLab =
                    (manager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val snapHelper: SnapHelper = PagerSnapHelper()
                recyclerView.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(recyclerView)

                try {
                    for (i in 0 until dotscount) {
                        dots[i]?.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@Awaiting_ConfirmationActivity,
                                R.drawable.non_active_dot_2
                            )
                        )

                    }
                    dots[abs(currentCompletelyVisibleLab)]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@Awaiting_ConfirmationActivity,
                            R.drawable.active_dot_3
                        )
                    )

                } catch (n: ArrayIndexOutOfBoundsException) {
                    //
                }
            }
        })


    }


    override fun onResume() {
        super.onResume()

        vendorPreference.getVendorId.asLiveData().observe(this) {
            Log.e("UId", it.toString())
            viewModel.loadUi(it!!, "pending")
        }

    }

    override fun onItemClick(leadItem: ResultOnGoing) {
        val intent = Intent(this, VehicleDetailsActivity::class.java)
        intent.putExtra("leadItem", leadItem)
        startActivity(intent)
    }

    override fun onItemConfirm(leadItem: LeadsItem, currentDate: String, currentTime: String) {
    }

    override fun onDecline(leadItem: LeadsItem) {

    }

    override fun onConfirmClick(resultOnGoing: ResultOnGoing, position: Int) {

        resultOngoingConfirm=resultOnGoing

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val sdfTime = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        sdfTime.timeZone = TimeZone.getTimeZone("UTC")
        sdfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"))

        var acceptLeadsReq = AcceptLeadsReq(
            resultOnGoing.leadId!!,
            sdf.format(Date().time),
            sdfTime.format(Date().time),
            resultOnGoing.booking_date!!,
            resultOnGoing.booking_time!!,
            "${resultOnGoing.outletId}",
            "${resultOnGoing.vehicleId}",
            "${resultOnGoing.addressId}"
        )



        Log.e("acceptLeadsReq", acceptLeadsReq.toString())
        acceptLeadsviewModel.acceptLeads(acceptLeadsReq!!)
    }

    override fun onDeclineClick(resultOnGoing: ResultOnGoing, position: Int) {
        resultOngoingConfirm=resultOnGoing
        acceptLeadsviewModel.declineLeads(resultOnGoing.leadId!!)
    }


}