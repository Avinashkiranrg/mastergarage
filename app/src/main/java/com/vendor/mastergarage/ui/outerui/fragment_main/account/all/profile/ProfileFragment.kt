package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.profile

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.SavedOutletAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.CITY_CODE
import com.vendor.mastergarage.databinding.FragmentProfileBinding
import com.vendor.mastergarage.databinding.RatingDialogBoxBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.citymodel.FindCityActivity
import com.vendor.mastergarage.ui.login.LoginActivity
import com.vendor.mastergarage.utlis.imageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), SavedOutletAdapter.OnItemClickListener {

    lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileVewModel by viewModels()

    @Inject
    lateinit var vendorPreference: VendorPreference

    private var phone: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

//        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
//        binding.toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.title.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel._outlet.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.imageUri != null) {
                    binding.imageView.imageFromUrl(it.imageUri)
                }
            }
        })

        viewModel.outlets.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<OutletsItem>
                    val savedOutletAdapter = SavedOutletAdapter(requireActivity(), vItem, this)
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
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        viewModel._outlet.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                phone = it.phone
            }
        })
        binding.contact.setOnClickListener {
            phone?.let { it1 -> dialNumber(it1) }
        }
        binding.talkToUs.setOnClickListener {
            phone?.let { it1 -> dialNumber(it1) }
        }

        vendorPreference.getCity.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.locationChip.setText(it)
        })
        vendorPreference.getAlerts.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.switch1.isChecked = it
        })
        vendorPreference.getNotifyMeOffers.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.switch2.isChecked = it
        })
        vendorPreference.getNotifyViaEmail.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.switch3.isChecked = it
        })
        vendorPreference.getWhatsappNotify.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.switch4.isChecked = it
        })

        vendorPreference.getRating.asLiveData().observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.ratingBar.visibility = View.GONE
                binding.review.visibility = View.GONE
                binding.text3.setText("Thank your for your review")
            }
        })
        binding.switch1.setOnCheckedChangeListener { p0, p1 ->
            lifecycleScope.launch {
                vendorPreference.setAlerts(p1)
            }
        }
        binding.switch2.setOnCheckedChangeListener { p0, p1 ->
            lifecycleScope.launch {
                vendorPreference.setNotifyMeOffers(p1)
            }
        }
        binding.switch3.setOnCheckedChangeListener { p0, p1 ->
            lifecycleScope.launch {
                vendorPreference.setNotifyViaEmail(p1)
            }
        }
        binding.switch4.setOnCheckedChangeListener { p0, p1 ->
            lifecycleScope.launch {
                vendorPreference.setWhatsappNotify(p1)
            }
        }
        binding.constraintLayout8.setOnClickListener {
            inviteFriend()
        }
        gotoCitySearch()

        binding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                rateDialogBox(requireActivity(), rating.toFloat())
            }

        binding.review.setOnClickListener {
            rateDialogBox(requireActivity(), binding.ratingBar.rating.toFloat())
        }

        binding.constraintLayout2.setOnClickListener {
            ModelPreferencesManager.clear()
            lifecycleScope.launch {
                vendorPreference.clear()
            }
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    private fun inviteFriend() {
        val text: String = "Take advantage of our exciting referral program! Refer a friend," +
                " Family Member, or Co-Worker and when they take vehicle service from Master Garage" +
                ", you earn $50. Hurray☺☺☺ ${requireActivity().packageName}"
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }

    }

    private fun dialNumber(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun gotoCitySearch() {
        binding.locationChip.setOnClickListener {
            intentLauncher.launch(Intent(requireActivity(), FindCityActivity::class.java))

        }

    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CITY_CODE) {
                val city = result.data?.getStringExtra("city")
                binding.locationChip.setText(city)
            }
        }

    companion object {
        private const val TAG = "ProfileFragment"
    }

    private fun rateDialogBox(context: Context, toInt: Float) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val binding: RatingDialogBoxBinding = RatingDialogBoxBinding.inflate(inflater)
        dialogBuilder.setView(binding.root)

        val alertDialog = dialogBuilder.create()
        binding.ratingBar.rating = toInt.toFloat()
        binding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                Toast.makeText(
                    requireActivity(), "Rate: " +
                            rating.toInt(), Toast.LENGTH_SHORT
                ).show()
            }

        binding.submit.setOnClickListener {
            when {
                binding.review.text.isNullOrEmpty() -> {
                    Toast.makeText(
                        requireActivity(), "Write your review", Toast.LENGTH_SHORT
                    ).show()
                }
                binding.ratingBar.rating < 0.5 -> {
                    Toast.makeText(
                        requireActivity(), "Rate us ", Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    viewModel.getStoredVendorObject()?.let { it1 ->
                        it1.vendorId?.let { it2 ->
                            viewModel.addRating(
                                binding.ratingBar.rating.toFloat(),
                                binding.review.getText().trim().toString(),
                                it2
                            )
                        }
                    }
                    viewModel.added.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is Response.Loading -> {

                            }
                            is Response.Success -> {
                                val vItem = it.data
                                if (vItem != null) {
                                    if (vItem.success == Constraints.TRUE_INT) {
                                        lifecycleScope.launch {
                                            vendorPreference.setRating(true)
                                        }
                                        alertDialog.dismiss()
                                    }
                                    Toast.makeText(
                                        requireActivity(),
                                        vItem.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                            is Response.Failure -> {
                                Toast.makeText(
                                    requireActivity(),
                                    it.errorMessage,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    })
                }
            }
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    override fun onItemClick(outletsItem: OutletsItem) {
        Log.e("Selected Outlet", outletsItem.toString())
        viewModel.storeOutletObject(outletsItem)
        viewModel.updateUi()
    }
}