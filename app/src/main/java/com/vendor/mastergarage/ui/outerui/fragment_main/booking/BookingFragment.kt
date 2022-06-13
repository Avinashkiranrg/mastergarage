package com.vendor.mastergarage.ui.outerui.fragment_main.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.vendor.mastergarage.databinding.FragmentBooking4Binding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.notifications.NotificationUi
import com.vendor.mastergarage.ui.outerui.bookingviewpager.DeliveredFragment
import com.vendor.mastergarage.ui.outerui.bookingviewpager.OnGoingFragment
import com.vendor.mastergarage.ui.outerui.bookingviewpager.PendingFragment
import com.vendor.mastergarage.ui.search.SearchOutletsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookingFragment : Fragment() {
    lateinit var binding: FragmentBooking4Binding
    val animalsArray = arrayOf(
        "Ongoing",
        "Pending",
        "Delivered"
    )

    private val viewModel: BookingFragViewModel by viewModels()

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBooking4Binding.inflate(inflater, container, false)

        binding.notification.setOnClickListener {
            val bottomSheetFragment: BottomSheetDialogFragment = NotificationUi()
            bottomSheetFragment.show(childFragmentManager, NotificationUi.TAG)
        }

        binding.locationChip.setOnClickListener {
            val intent = Intent(requireActivity(), SearchOutletsActivity::class.java)
            intent.putExtra("code", 1)
            startActivity(intent)
        }

        viewModel.total.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val userItem = it.data

                    if (userItem != null) {
                        binding.bookingCountOngoing.text = "${userItem.ongoingRows}  Bookings"
                        binding.bookingCountPending.text = "${userItem.leadsRows}  Bookings"
                        binding.bookingCountDelivered.text = "${userItem.deliveredRows}  Bookings"
                    }

                }
                is Response.Failure -> {
                    Log.e("Vendor data Failure", it.errorMessage.toString())
                }
            }
        })

        initViewPager()

        return binding.root
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(fragmentManager = childFragmentManager, lifecycle)
        binding.viewPage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPage) { tab, position ->
            tab.text = animalsArray[position]
        }.attach()
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return OnGoingFragment()
                1 -> return PendingFragment()
                2 -> return DeliveredFragment()
            }
            return OnGoingFragment()
        }
    }


    companion object {
        const val NUM_TABS = 3
    }
}

