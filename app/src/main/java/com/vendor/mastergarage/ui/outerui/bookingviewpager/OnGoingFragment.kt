package com.vendor.mastergarage.ui.outerui.bookingviewpager

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
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.OnGoingAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.FragmentOnGoingBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.OnGoingDataItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.VehicleDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnGoingFragment : Fragment(), OnGoingAdapter.OnItemClickListener {

    lateinit var binding: FragmentOnGoingBinding

    private val viewModel: OnGoingViewModel by viewModels()

    @Inject
    lateinit var vendorPreference : VendorPreference

    var vendorId : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnGoingBinding.inflate(inflater, container, false)



        viewModel.onGoing.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }

                is Response.Success -> {
                    if (it.data?.success == Constraints.TRUE_INT) {
                     /*   val vItem = it.data.result as MutableList<OnGoingDataItem>
                        vItem.sortBy { it1 -> it1.ongoingId }
                        vItem.reverse()*/
                        val savedOutletAdapter = OnGoingAdapter(requireActivity(),it.data.result, this)
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
                    } else {
                        Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })




        return binding.root
    }

    companion object {
        private const val TAG = "OnGoingFragment"

    }

    override fun onResume() {
        super.onResume()

        vendorPreference.getVendorId.asLiveData().observe(requireActivity()) {
            Log.e("UId", it.toString())
            vendorId = it
            viewModel.loadUi(vendorId!!,"ongoing")
        }

    }

    override fun onItemClick(onGoingDataItem: OnGoingDataItem) {
        val intent = Intent(requireActivity(), VehicleDetailsActivity::class.java)
        intent.putExtra("onGoingDataItem", onGoingDataItem)
        requireActivity().startActivity(intent)
    }

}