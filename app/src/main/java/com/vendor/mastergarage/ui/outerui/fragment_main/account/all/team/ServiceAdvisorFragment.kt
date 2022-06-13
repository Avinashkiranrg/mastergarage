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
import com.vendor.mastergarage.adapters.ServiceAdvisorAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.DeleteDialogBoxBinding
import com.vendor.mastergarage.databinding.FragmentServiceAdvisorBinding
import com.vendor.mastergarage.model.ServiceAdvisorItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.serviceadvisor.AddServiceAdvisorActivity
import com.vendor.mastergarage.ui.outerui.serviceadvisor.EditServiceAdvisorActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceAdvisorFragment : Fragment(), ServiceAdvisorAdapter.OnItemClickListener {

    lateinit var binding: FragmentServiceAdvisorBinding

    private lateinit var serviceAdaptor: ServiceAdvisorAdapter

    private val viewModel: ServiceAdvisorViewModel by viewModels()

    private var size: Int? = null

    var filterList: ArrayList<ServiceAdvisorItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceAdvisorBinding.inflate(inflater, container, false)



        viewModel.getStoredOutletObject()?.let {
            it.outletId?.let { it1 ->
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getServiceAdvisor(it1)
            }
        }
        viewModel.serviceAdvisor.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data != null) {
                        if (it.data.success == TRUE_INT) {
                            val vItem = it.data.result as MutableList<ServiceAdvisorItem>
                            binding.totalAdvisor.text = "All Advisors (${vItem.size})"
                            size = vItem.size
                            filterList = ArrayList()
                            filterList!!.addAll(vItem)
                            serviceAdaptor =
                                ServiceAdvisorAdapter(requireActivity(), vItem, this)
                            binding.recyclerView.apply {
                                setHasFixedSize(true)
                                adapter = serviceAdaptor
                                layoutManager =
                                    LinearLayoutManager(
                                        requireActivity(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                            }
                        } else {
                            Toast.makeText(requireActivity(), it.data.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.addAdvisor.setOnClickListener {
            val intent = Intent(requireActivity(), AddServiceAdvisorActivity::class.java)
            requireActivity().startActivity(intent)
        }
        return binding.root
    }


    companion object {
        private const val TAG = "ServiceAdvisorFragment"
    }

    override fun onItemDeleteClick(serviceAdvisorItem: ServiceAdvisorItem) {
        confirmBox(requireActivity(), serviceAdvisorItem)
    }

    private fun confirmBox(
        context: Context,
        serviceAdvisorItem: ServiceAdvisorItem
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val bindingBox: DeleteDialogBoxBinding = DeleteDialogBoxBinding.inflate(inflater)
        dialogBuilder.setView(bindingBox.root)

        val alertDialog = dialogBuilder.create()
        bindingBox.title.text =
            "Do you want to delete '${serviceAdvisorItem.firstName} ${serviceAdvisorItem.lastName}'"
        bindingBox.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


        bindingBox.deleteBtn.setOnClickListener {
            serviceAdvisorItem.advisorId?.let { it1 -> viewModel.deleteAdvisor(it1) }
            bindingBox.progressBar.visibility = View.VISIBLE
            bindingBox.cancelBtn.visibility = View.INVISIBLE
            bindingBox.deleteBtn.visibility = View.INVISIBLE

            viewModel.delete.observe(requireActivity(), Observer {
                when (it) {
                    is Response.Success -> {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {
                                filterList?.remove(serviceAdvisorItem)
                                filterList?.let { it1 -> serviceAdaptor.setFilter(it1) }
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

    override fun onItemEditClick(position: Int, serviceAdvisorItem: ServiceAdvisorItem) {
        val intent = Intent(requireActivity(), EditServiceAdvisorActivity::class.java)
        intent.putExtra("serviceAdvisorItem", serviceAdvisorItem)
        requireActivity().startActivity(intent)

    }


//    override fun onDestroy() {
//        parentFragment?.let { viewModel.serviceAdvisor.removeObservers(it.viewLifecycleOwner) }
//        super.onDestroy()
//    }

    override fun onDestroyView() {
        parentFragment?.let { viewModel.serviceAdvisor.removeObservers(it.viewLifecycleOwner) }
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUi()
    }
}