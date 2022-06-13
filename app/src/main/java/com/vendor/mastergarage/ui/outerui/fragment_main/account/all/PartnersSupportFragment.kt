package com.vendor.mastergarage.ui.outerui.fragment_main.account.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.vendor.mastergarage.databinding.FragmentPartnersSupportBinding
import com.vendor.mastergarage.ui.outerui.fragment_main.account.all.partners.FAQsPartnerFragment
import com.vendor.mastergarage.ui.outerui.fragment_main.account.all.partners.MyTicketsFragment

class PartnersSupportFragment : Fragment() {
    lateinit var binding: FragmentPartnersSupportBinding
    val tabArray = arrayOf(
        "My Tickets",
        "FAQs"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPartnersSupportBinding.inflate(inflater, container, false)


        initViewPager()

        return binding.root
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(fragmentManager = childFragmentManager, lifecycle)
        binding.viewPage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPage) { tab, position ->
            tab.text = tabArray[position]
        }.attach()
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return PARTNERS_NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return MyTicketsFragment()
                1 -> return FAQsPartnerFragment()
            }
            return MyTicketsFragment()
        }
    }


    companion object {
        const val PARTNERS_NUM_TABS = 2
    }
}