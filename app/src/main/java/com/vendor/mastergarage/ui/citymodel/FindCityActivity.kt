package com.vendor.mastergarage.ui.citymodel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.CityAdapter
import com.vendor.mastergarage.constraints.Constraints.Companion.CITY_CODE
import com.vendor.mastergarage.databinding.ActivityFindCityBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.CityItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FindCityActivity : AppCompatActivity(), CityAdapter.OnItemClickListener {

    private val cityViewModel: CityViewModel by viewModels()

    lateinit var binding: ActivityFindCityBinding

    @Inject
    lateinit var vendorPreference: VendorPreference

    lateinit var cityAdapter: CityAdapter
    var userList: ArrayList<CityItem>? = null
    var filteredDataList: List<CityItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindCityBinding.inflate(layoutInflater)
        setContentView(binding.root)



        cityViewModel.cityLive.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this@FindCityActivity, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val userItem = it.data as MutableList<CityItem>

                    val sortedList = userItem.sortedWith(compareBy({ it.name }))

                    userList = ArrayList()
                    userList!!.addAll(sortedList)
                    cityAdapter = CityAdapter(sortedList, WeakReference(this))
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = cityAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@FindCityActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
//                    binding.noData.isVisible = true
                    Toast.makeText(this@FindCityActivity, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filteredDataList = filter(userList!!, newText);
                cityAdapter.setFilter(filteredDataList as List<CityItem?>)
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

    private fun filter(userList: List<CityItem>, queryText: String): List<CityItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        val filteredDataList = ArrayList<CityItem>()
        for (dataFromDataList in userList) {
            text = dataFromDataList.name!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }


    override fun onItemClick(cityItem: CityItem) {
        val data = Intent()
        lifecycleScope.launch {
            cityItem.name?.let { vendorPreference.setCity(it) }
            cityItem.state?.let { vendorPreference.setState(it) }
        }
        data.putExtra("city", cityItem.name)
        setResult(CITY_CODE, data);
        finish();
    }
}