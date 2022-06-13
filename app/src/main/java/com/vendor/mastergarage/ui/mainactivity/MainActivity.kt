package com.vendor.mastergarage.ui.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints.Companion.OUTLET_STORE
import com.vendor.mastergarage.constraints.Constraints.Companion.VENDOR_STORE
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.Vendors
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.IOnBackPressed
import com.vendor.mastergarage.ui.outerui.fragment_main.booking.BookingFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController: NavController =
            Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)


//        vendorPreference.getVid.asLiveData().observe(this, {
//            Log.e("UId", it.toString())
//            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
//        })
//        vendorPreference.getVendorPhone.asLiveData().observe(this, {
//            Log.e("getVendorPhone", it.toString())
//            Toast.makeText(this, "getVendorPhone ${it.toString()}", Toast.LENGTH_SHORT).show()
//            viewModel.getVendor(it.toString())
//        })
//
//
//
//        viewModel.vendor.observe(this, Observer {
//            when (it) {
//                is Response.Loading -> {
//                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.e("Vendor data Loading", it.errorMessage.toString())
//
//                }
//                is Response.Success -> {
//                    val userItem = it.data
//                    ModelPreferencesManager.put(userItem, VENDOR_STORE)
////                    val bag = ModelPreferencesManager.get<Vendors>(VENDOR_STORE)
////                    Log.e("Vendor bag", bag.toString())
//                }
//                is Response.Failure -> {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
//                        .show()
//                    Log.e("Vendor data Failure", it.errorMessage.toString())
//                }
//            }
//        })
    }

}