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
import com.vendor.mastergarage.adapters.OnDeliveredAdapter
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.FragmentDeliveredBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.OnDeliveredItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.VehicleDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeliveredFragment : Fragment(), OnDeliveredAdapter.OnItemClickListener {

    lateinit var binding: FragmentDeliveredBinding

    private val viewModel: OnDeliveredViewModel by viewModels()

    @Inject
    lateinit var vendorPreference: VendorPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveredBinding.inflate(inflater, container, false)

        viewModel.onDelivered.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }

                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                     /*   val vItem = it.data.result as MutableList<OnDeliveredItem>
                        vItem.sortBy { it1 -> it1.deliveredId }
                        vItem.reverse()*/
                        val savedOutletAdapter = OnDeliveredAdapter(requireActivity(), it.data.result!!, this)
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
//                     Toast.makeText(requireActivity(), "DeliveredFragment ${it.errorMessage}", Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })


        return binding.root
    }

    companion object {
        private const val TAG = "DeliveredFragment"

    }

    override fun onResume() {
        super.onResume()
        vendorPreference.getVendorId.asLiveData().observe(requireActivity()) {
            Log.e("UId", it.toString())
            viewModel.loadUi(it!!, "delivered")
        }
    }

    override fun onItemClick(onDeliveredItem: OnDeliveredItem) {
        val intent = Intent(requireActivity(), VehicleDetailsActivity::class.java)
        intent.putExtra("onDeliveredItem", onDeliveredItem)
        requireActivity().startActivity(intent)
    }

}