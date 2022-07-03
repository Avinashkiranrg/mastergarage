package com.vendor.mastergarage.ui.outerui

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
import com.vendor.mastergarage.model.LeadsItem
import com.vendor.mastergarage.model.OnGoingRespo
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.mainactivity.MainActivity
import com.vendor.mastergarage.ui.outerui.bookingviewpager.PendingViewModel
import com.vendor.mastergarage.utlis.goToActivitiesFinish
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class Awaiting_ConfirmationActivity : AppCompatActivity(), AwaitingAdapter.OnItemClickListener {
    @Inject
    lateinit var vendorPreference: VendorPreference
    lateinit var binding: ActivityAwaitingConfirmationBinding
    private val viewModel: PendingViewModel by viewModels()
    var filterList: ArrayList<LeadsItem?>? = null
    var manager:LinearLayoutManager?=null
     var position: Int=0
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
    }

    private fun setOnClicks() {
        binding.homeBtn.setOnClickListener{
            goToActivitiesFinish(this@Awaiting_ConfirmationActivity, MainActivity::class.java)
        }
    }

    private fun initializeAwaitingSlider(data: OnGoingRespo) {
        awaitingAdapter = AwaitingAdapter(this, data.result, this)

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

                }catch (n: ArrayIndexOutOfBoundsException) {
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

    override fun onItemClick(leadItem: LeadsItem) {
        TODO("Not yet implemented")
    }

    override fun onItemConfirm(leadItem: LeadsItem, currentDate: String, currentTime: String) {
        TODO("Not yet implemented")
    }

    override fun onDecline(leadItem: LeadsItem) {
        TODO("Not yet implemented")
    }


}