package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.adapters.DriverAdapter
import com.vendor.mastergarage.adapters.ServiceAdvisorAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.DeleteDialogBoxBinding
import com.vendor.mastergarage.databinding.FragmentDriverBinding
import com.vendor.mastergarage.databinding.FragmentServiceAdvisorBinding
import com.vendor.mastergarage.model.DriversItem
import com.vendor.mastergarage.model.ServiceAdvisorItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.schedulepickup.AddDriverActivity
import com.vendor.mastergarage.ui.outerui.schedulepickup.EditDriverActivity
import com.vendor.mastergarage.ui.outerui.serviceadvisor.AddServiceAdvisorActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DriverFragment : Fragment(), DriverAdapter.OnItemClickListener {

    lateinit var binding: FragmentDriverBinding

    private val viewModel: DriverFragViewModel by viewModels()
    lateinit var driverAdapter: DriverAdapter

    private var size: Int? = null

    var filterList: ArrayList<DriversItem?>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDriverBinding.inflate(inflater, container, false)

        viewModel.getStoredOutletObject()?.let {
            it.outletId?.let { it1 ->
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getDrivers(it1)
            }
        }

        viewModel.driver.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val vItem = it.data as MutableList<DriversItem>
                    filterList = ArrayList()
                    filterList!!.addAll(vItem)
                    binding.totalDriver.text = "All Drivers (${vItem.size})"
                    driverAdapter = DriverAdapter(requireActivity(), vItem, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = driverAdapter
                        layoutManager =
                            LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.addDriver.setOnClickListener {
            val intent = Intent(requireActivity(), AddDriverActivity::class.java)
            requireActivity().startActivity(intent)
        }

        return binding.root


    }

    companion object {
        private const val TAG = "DriverFragment"
    }

    override fun onItemDeleteClick(driversItem: DriversItem) {
        confirmBox(requireActivity(), driversItem)

    }

    private fun confirmBox(
        context: Context,
        driversItem: DriversItem
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val bindingBox: DeleteDialogBoxBinding = DeleteDialogBoxBinding.inflate(inflater)
        dialogBuilder.setView(bindingBox.root)

        val alertDialog = dialogBuilder.create()
        bindingBox.title.text =
            "Do you want to delete '${driversItem.firstName} ${driversItem.lastName}'"
        bindingBox.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


        bindingBox.deleteBtn.setOnClickListener {
            driversItem.driverId?.let { it1 -> viewModel.deleteDriver(it1) }
            bindingBox.progressBar.visibility = View.VISIBLE
            bindingBox.cancelBtn.visibility = View.INVISIBLE
            bindingBox.deleteBtn.visibility = View.INVISIBLE

            viewModel.delete.observe(requireActivity(), Observer {
                when (it) {
                    is Response.Success -> {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {
                                filterList?.remove(driversItem)
                                filterList?.let { it1 -> driverAdapter.setFilter(it1) }
                            }
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), vItem.message, Toast.LENGTH_SHORT)
                                .show()
                            alertDialog.dismiss()
                        }
                    }
                    is Response.Failure -> {
                        Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, it.errorMessage.toString())
                        binding.progressBar.visibility = View.GONE
                        bindingBox.progressBar.visibility = View.GONE
                        bindingBox.cancelBtn.visibility = View.VISIBLE
                        bindingBox.deleteBtn.visibility = View.VISIBLE
                    }
                }
            })
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUi()
    }

    override fun onItemEditClick(position: Int, driversItem: DriversItem) {
        val intent = Intent(requireActivity(), EditDriverActivity::class.java)
        intent.putExtra("driversItem", driversItem)
        requireActivity().startActivity(intent)
    }

    override fun onDestroyView() {
        parentFragment?.let { viewModel.driver.removeObservers(it.viewLifecycleOwner) }
        super.onDestroyView()
    }

}