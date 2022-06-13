package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.discount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uidesign.model.Discounts
import com.example.uidesign.model.DiscountsItem
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.AddDiscountServiceAdapter
import com.vendor.mastergarage.adapters.DiscountServiceAdapter
import com.vendor.mastergarage.adapters.FaqAdapter
import com.vendor.mastergarage.databinding.FragmentDiscountsBinding
import com.vendor.mastergarage.model.FaqItem
import com.vendor.mastergarage.model.ServicePackageItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.fragment_main.account.all.faq.FaqFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DiscountsFragment : Fragment(), DiscountServiceAdapter.OnItemClickListener {

    lateinit var binding: FragmentDiscountsBinding

    private val viewModel: DiscountFragViewModel by viewModels()

    lateinit var discountAdapter: DiscountServiceAdapter
    var userList: ArrayList<DiscountsItem>? = null
    var filteredDataList: List<DiscountsItem?>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscountsBinding.inflate(inflater, container, false)

        binding.submit.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_discountsFragment_to_addDiscountsFragment)
        }

        viewModel.discount.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    try {
                        val vItem = it.data as ArrayList<DiscountsItem>
                        userList = ArrayList()
                        try {
                            userList!!.addAll(vItem)
                        } catch (e: NullPointerException) {

                        }
                        discountAdapter = DiscountServiceAdapter(requireActivity(), vItem, this)
                        binding.recyclerView.apply {
                            setHasFixedSize(true)
                            adapter = discountAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                        }

                    } catch (e: NullPointerException) {

                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.searchChip.setOnClickListener {
            binding.searchBar.visibility = View.VISIBLE
            binding.searchChip.visibility = View.GONE
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filteredDataList = filter(userList!!, newText);
                discountAdapter.setFilter(filteredDataList as List<DiscountsItem?>)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        binding.searchBar.setOnCloseListener {
            true
        }

        return binding.root
    }

    companion object {
        private const val TAG = "DiscountsFragment"
    }

    private fun filter(userList: List<DiscountsItem>, queryText: String): List<DiscountsItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        var body: String
        val filteredDataList = ArrayList<DiscountsItem>()
        for (dataFromDataList in userList) {
            text = dataFromDataList.discountCode.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUi()
    }

    override fun onItemClick(serviceListItem: DiscountsItem) {

    }
}