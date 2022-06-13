package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.faq

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.FaqAdapter
import com.vendor.mastergarage.databinding.FragmentFAQsBinding
import com.vendor.mastergarage.model.FaqItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FAQsFragment : Fragment() {
    lateinit var binding: FragmentFAQsBinding
    private val viewModel: FaqFragViewModel by viewModels()

    lateinit var faqAdapter: FaqAdapter
    var userList: ArrayList<FaqItem>? = null
    var filteredDataList: List<FaqItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFAQsBinding.inflate(inflater, container, false)

        binding.title.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.faq.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data as MutableList<FaqItem>
                    userList = ArrayList()
                    try {
                        userList!!.addAll(vItem)
                    } catch (e: NullPointerException) {

                    }
                    faqAdapter = FaqAdapter(requireActivity(), vItem)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = faqAdapter
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

        binding.searchChip.setOnClickListener {
            binding.searchBar.visibility = View.VISIBLE
            binding.searchChip.visibility = View.GONE
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filteredDataList = filter(userList!!, newText);
                faqAdapter.setFilter(filteredDataList as List<FaqItem?>)
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

    private fun filter(userList: List<FaqItem>, queryText: String): List<FaqItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        var body: String
        val filteredDataList = ArrayList<FaqItem>()
        for (dataFromDataList in userList) {
            text = dataFromDataList.title!!.lowercase(Locale.getDefault())
            body = dataFromDataList.body!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            } else if (body.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }

    companion object {
        private const val TAG = "FAQsFragment"
    }

}