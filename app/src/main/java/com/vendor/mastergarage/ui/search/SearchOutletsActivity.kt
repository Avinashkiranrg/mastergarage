package com.vendor.mastergarage.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.adapters.SavedOutletAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.OUTLET_STORE
import com.vendor.mastergarage.databinding.ActivitySearchOutletsBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchOutletsActivity : AppCompatActivity(), TextWatcher,
    SavedOutletAdapter.OnItemClickListener {

    lateinit var binding: ActivitySearchOutletsBinding

    private val viewModel: SearchOutletViewModel by viewModels()

    lateinit var savedOutletAdapter: SavedOutletAdapter

    var outletsItem: ArrayList<OutletsItem>? = null
    var filteredOutletsItem: List<OutletsItem?>? = null
    private var phone_no: String? = ""

    private var code = -1

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchOutletsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        vendorPreference.getVendorPhone.asLiveData().observe(this, {
            Log.e(TAG, it.toString())
            phone_no = it.toString()
            viewModel.getVendor(it.toString())
        })

        code = intent.getIntExtra("code", -1)


        viewModel.vendor.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
//                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
//                        .show()
                }
                is Response.Success -> {
                    val userItem = it.data
                    ModelPreferencesManager.put(userItem, Constraints.VENDOR_STORE)
                    if (userItem != null) {
                        userItem.vendorId?.let { it1 ->
                            viewModel.getOutlet(it1)
                        }
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        viewModel.outlets.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
//                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
//                        .show()
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val vItem = it.data as MutableList<OutletsItem>
                    outletsItem = ArrayList()
                    outletsItem!!.addAll(vItem)
                    savedOutletAdapter = SavedOutletAdapter(this, vItem, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = savedOutletAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@SearchOutletsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.searchBar.addTextChangedListener(this)

        binding.getGpsLoc.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.closest.setOnClickListener {
            if (outletsItem != null) {
                if (outletsItem!!.size > 0) {
                    ModelPreferencesManager.put(outletsItem!![0], OUTLET_STORE)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    companion object {
        private const val TAG: String = "SearchOutletsActivity"
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        try {
            val str: String = s.toString()
            filteredOutletsItem = filter(outletsItem!!, str)
            savedOutletAdapter.setFilter(filteredOutletsItem as List<OutletsItem?>)
        } catch (e: NullPointerException) {

        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun filter(outletsItem: List<OutletsItem>, queryText: String): List<OutletsItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        val filteredDataList = ArrayList<OutletsItem>()
        for (dataFromDataList in outletsItem) {
            text = dataFromDataList.name!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }

    @SuppressLint("HardwareIds")
    override fun onItemClick(outletsItem: OutletsItem) {
        Log.e("Selected Outlet", outletsItem.toString())
        ModelPreferencesManager.put(outletsItem, OUTLET_STORE)
//        viewModel.storeOutletObject(outletsItem)
//        val id: String = Secure.getString(contentResolver, Secure.ANDROID_ID)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        if (code == -1) {
            val outletsItems = ModelPreferencesManager.get<OutletsItem>(OUTLET_STORE)
            if (outletsItems != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Log.e("outletsItems bag", outletsItems.toString())
            }
        }
    }
}