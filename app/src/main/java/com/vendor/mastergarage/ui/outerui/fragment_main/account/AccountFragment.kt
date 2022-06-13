package com.vendor.mastergarage.ui.outerui.fragment_main.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.FALSE
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE
import com.vendor.mastergarage.databinding.FragmentAccountBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.ui.citymodel.FindCityActivity
import com.vendor.mastergarage.ui.notifications.NotificationUi
import com.vendor.mastergarage.utlis.imageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

    private val accountViewModel: AccountViewModel by viewModels()

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        binding.viewmodel = accountViewModel
        binding.lifecycleOwner = this

        binding.notification.setOnClickListener {
            val bottomSheetFragment: BottomSheetDialogFragment = NotificationUi()
            bottomSheetFragment.show(childFragmentManager, NotificationUi.TAG)
        }

        accountViewModel._outlet.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.switch1.isChecked = it.isOperating == TRUE
//                binding.greetings.text = it.name
                if (it.imageUri != null) {
                    binding.imageView.imageFromUrl(it.imageUri)
                }

            }
        })

        val outletsItem = ModelPreferencesManager.get<OutletsItem>(Constraints.OUTLET_STORE)
        if (outletsItem != null) {
            binding.switch1.isChecked = outletsItem.isOperating == TRUE
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            val outletsItem = ModelPreferencesManager.get<OutletsItem>(Constraints.OUTLET_STORE)
            if (isChecked) {
                if (outletsItem != null) {
                    outletsItem.outletId?.let { accountViewModel.getIsOperating(TRUE, it) }
                    outletsItem.isOperating = TRUE
                    ModelPreferencesManager.put(outletsItem, Constraints.OUTLET_STORE)

                }
            } else {
                if (outletsItem != null) {
                    outletsItem.outletId?.let { accountViewModel.getIsOperating(FALSE, it) }
                    outletsItem.isOperating = FALSE
                    ModelPreferencesManager.put(outletsItem, Constraints.OUTLET_STORE)
                }
            }
        }

        binding.profile.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_accountFragment_to_profileFragment)
        }

        binding.team.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_accountFragment_to_teamFragment)
        }

        binding.fAQs.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_accountFragment_to_FAQsFragment)
        }

        binding.discounts.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_accountFragment_to_discountsFragment)
        }

        binding.contactUs.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_accountFragment_to_contactUsFragment)
        }

        binding.finance.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_accountFragment_to_financeFragment)
        }

        vendorPreference.getCity.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.locationChip.text = it
        })

        gotoCitySearch()

        return binding.root
    }

    private fun gotoCitySearch() {
        binding.locationChip.setOnClickListener {
            intentLauncher.launch(Intent(requireActivity(), FindCityActivity::class.java))

        }

    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Constraints.CITY_CODE) {
                val city = result.data?.getStringExtra("city")
                binding.locationChip.setText(city)
            }
        }


    override fun onResume() {
        super.onResume()
        Log.e("onResume", "onResume")
        accountViewModel.updateUi()
    }

    companion object {
        private const val TAG = "AccountFragment"
    }

}