package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.finance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.FragmentFinanceBinding
import com.vendor.mastergarage.databinding.FragmentTeamBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinanceFragment : Fragment() {

    lateinit var binding: FragmentFinanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFinanceBinding.inflate(inflater, container, false)

        binding.title.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val navHostFragment = childFragmentManager.findFragmentById(R.id.frameLayout)
                as NavHostFragment
        val navController = navHostFragment.navController

        binding.paymentLayout.setBackgroundResource(R.drawable.button_elevation)
        binding.paymentText.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.blue
            )
        )

        binding.paymentLayout.setOnClickListener {
            binding.paymentLayout.setBackgroundResource(R.drawable.button_elevation)
            binding.driverLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    android.R.color.transparent
                )
            )
            binding.paymentText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.blue
                )
            )
            binding.settlmentText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.light_color_
                )
            )
            navController.navigate(R.id.paymentDateWiseFragment)
        }

        binding.driverLayout.setOnClickListener {
            binding.driverLayout.setBackgroundResource(R.drawable.button_elevation)
            binding.paymentLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    android.R.color.transparent
                )
            )
            binding.settlmentText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.blue
                )
            )
            binding.paymentText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.light_color_
                )
            )
            navController.navigate(R.id.settleDateWiseFragment)

        }

        return binding.root
    }
}