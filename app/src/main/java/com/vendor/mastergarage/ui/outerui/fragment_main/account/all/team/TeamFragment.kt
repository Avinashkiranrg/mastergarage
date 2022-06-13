package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.team

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.FragmentTeamBinding
import com.vendor.mastergarage.ui.IOnBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamFragment : Fragment() {
    lateinit var binding: FragmentTeamBinding

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamBinding.inflate(inflater, container, false)

        binding.title.setOnClickListener {
            requireActivity().onBackPressed()
        }

        navHostFragment = childFragmentManager.findFragmentById(R.id.frameLayout)
                as NavHostFragment
        navController = navHostFragment.navController

        binding.advisorLayout.setBackgroundResource(R.drawable.button_elevation)
        binding.advisor.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.blue
            )
        )
        binding.advisorLayout.setOnClickListener {
            binding.advisorLayout.setBackgroundResource(R.drawable.button_elevation)
            binding.driverLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    android.R.color.transparent
                )
            )
            binding.advisor.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.blue
                )
            )
            binding.driverText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.light_color_
                )
            )
            navController.navigate(R.id.serviceAdvisorFragment)
        }

        binding.driverLayout.setOnClickListener {
            binding.driverLayout.setBackgroundResource(R.drawable.button_elevation)
            binding.advisorLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    android.R.color.transparent
                )
            )
            binding.driverText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.blue
                )
            )
            binding.advisor.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.light_color_
                )
            )
            navController.navigate(R.id.driverFragment)
        }

        return binding.root
    }
}