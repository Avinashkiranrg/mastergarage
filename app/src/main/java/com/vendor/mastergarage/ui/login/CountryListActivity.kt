package com.vendor.mastergarage.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.CountryAdapter
import com.vendor.mastergarage.databinding.ActivityCountryListBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.CountryCodeItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CountryListActivity : AppCompatActivity(), CountryAdapter.OnItemClickListener {
    lateinit var binding: ActivityCountryListBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var vendorPreference: VendorPreference

    lateinit var countryAdapter: CountryAdapter
    var userList: ArrayList<CountryCodeItem>? = null
    var filteredDataList: List<CountryCodeItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.countrys.observe(this, Observer { it ->
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this@CountryListActivity, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val userItem = it.data as MutableList<CountryCodeItem>
                    userList = ArrayList()
                    userList!!.addAll(userItem)
                    countryAdapter = CountryAdapter(userItem, WeakReference(this))
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = countryAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@CountryListActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this@CountryListActivity, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filteredDataList = filter(userList!!, newText);
                countryAdapter.setFilter(filteredDataList as List<CountryCodeItem?>)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        binding.searchBar.setOnCloseListener {
            true
        }
    }

    private fun filter(userList: List<CountryCodeItem>, queryText: String): List<CountryCodeItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        val filteredDataList = ArrayList<CountryCodeItem>()
        for (dataFromDataList in userList) {
            text = dataFromDataList.name!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }


    override fun onItemClick(countryCodeItem: CountryCodeItem, url: String) {
        val data = Intent()
        lifecycleScope.launch {
            countryCodeItem.dialCode?.let { vendorPreference.setCountryCode(it) }
            vendorPreference.setCountryFlag(url)
            vendorPreference.setCountryName("${countryCodeItem.name} (${countryCodeItem.dialCode})")
        }
        data.putExtra("name", countryCodeItem.name)
        data.putExtra("dialCode", countryCodeItem.dialCode)
        data.putExtra("code", countryCodeItem.code)
        setResult(101, data);
        finish();
    }
}
