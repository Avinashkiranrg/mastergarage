package com.vendor.mastergarage.ui.outerui.manageservice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.*
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.constraints.Constraints.Companion.VEHICLE_CODE
import com.vendor.mastergarage.databinding.ActivityEditServiceBinding
import com.vendor.mastergarage.model.ServiceListItem
import com.vendor.mastergarage.model.ServicePackageProvideItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.notifications.NotificationUi
import com.vendor.mastergarage.utlis.assetsToBitmapBrands
import com.vendor.mastergarage.utlis.enable
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class EditServiceActivity : AppCompatActivity(), ServiceAdapterHorizontal.OnItemClickListener,
    ServicePackageAdapter.OnItemClickListener {

    lateinit var binding: ActivityEditServiceBinding
    private var serviceId: Int? = null
    private var service_name: String? = null
    var servicesItemIntent: ArrayList<ServiceListItem>? = null
    private lateinit var serviceAdaptor: ServiceAdapterHorizontal
    private lateinit var servicePackageAdapter: ServicePackageAdapter

    var servicePackageProvideItem: ArrayList<ServicePackageProvideItem>? = null
    var filterServicePackageProvideItem: List<ServicePackageProvideItem>? = null
    private val viewModel: EditServicePackageViewModel by viewModels()

    private var service1: ServiceListItem? = null
    private var service2: ServiceListItem? = null
    private var service3: ServiceListItem? = null
    private var service4: ServiceListItem? = null
    private var service5: ServiceListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        servicePackageProvideItem = ArrayList()

        binding.title.setOnClickListener {
            onBackPressed()
        }


        binding.notification.setOnClickListener {
            val bottomSheetFragment: BottomSheetDialogFragment = NotificationUi()
            bottomSheetFragment.show(supportFragmentManager, NotificationUi.TAG)
        }



        try {
            val intent = intent
            serviceId = intent.getIntExtra("id", -1)
            service_name = intent.getStringExtra("name")
            servicesItemIntent = intent.getParcelableArrayListExtra("list")

            viewModel.getStoredOutletObject()?.outletId?.let { viewModel.getServiceProvide(it) }

            serviceId?.let { it1 -> viewModel.loadUi(it1) }

//            Log.e("Size", servicesItemIntent?.size.toString())
//            try {
//                serviceAdaptor = ServiceAdapterHorizontal(this, servicesItemIntent!!, this)
//                binding.recyclerView.apply {
//                    setHasFixedSize(true)
//                    adapter = serviceAdaptor
//                    layoutManager =
//                        LinearLayoutManager(
//                            this@EditServiceActivity,
//                            LinearLayoutManager.HORIZONTAL,
//                            false
//                        )
//                }
//
//                Log.e("servicesItemIntent servicesItemIntent", servicesItemIntent?.size.toString())
//
//                var position = -1
//                for (i in 0 until servicesItemIntent!!.size) {
//                    if (servicesItemIntent?.get(i)?.serviceId === serviceId) {
//                        position = i
//                        // break;  // uncomment to get the first instance
//                    }
//                }
//                binding.recyclerView.scrollToPosition(position)
//
//            } catch (e: NullPointerException) {
//
//            }
            binding.title.text = service_name
        } catch (e: NullPointerException) {

        }

        initilizeAdapter()

        viewModel.service.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val vItem = it.data.result as MutableList<ServiceListItem>

                        Log.e("List ${vItem.size}", vItem.toString())
                        Log.e("Size", vItem.size.toString())

                        vItem.forEach { it1 ->
                            if (it1.serviceId == 1) {
                                service1 = it1
                            }
                            if (it1.serviceId == 4) {
                                service2 = it1
                            }
                            if (it1.serviceId == 6) {
                                service3 = it1
                            }
                            if (it1.serviceId == 7) {
                                service4 = it1
                            }
                            if (it1.serviceId == 3) {
                                service5 = it1
                            }
                        }
                        if (service1?.serviceId == serviceId) {
                            binding.serviceList.horizontalScrollView.scrollTo(
                                0, binding.serviceList.cardView1.left.toInt()
                            )
                            setBackgroundViews1()
                        } else if (service2?.serviceId == serviceId) {
                            binding.serviceList.horizontalScrollView.scrollTo(
                                0, binding.serviceList.cardView2.left.toInt()
                            )
                            setBackgroundViews2()
                        } else if (service3?.serviceId == serviceId) {
                            binding.serviceList.horizontalScrollView.scrollTo(
                                0, binding.serviceList.cardView3.left.toInt()
                            )
                            setBackgroundViews3()
                        } else if (service4?.serviceId == serviceId) {
                            binding.serviceList.horizontalScrollView.scrollTo(
                                0, binding.serviceList.cardView4.left.toInt()
                            )
                            setBackgroundViews4()
                        } else if (service5?.serviceId == serviceId) {
                            binding.serviceList.horizontalScrollView.scrollTo(
                                0, binding.serviceList.cardView5.left.toInt()
                            )
                            setBackgroundViews5()
                        }

                        if (service1 == null) {
                            binding.serviceList.cardView1.enable(false)
                        }
                        if (service2 == null) {
                            binding.serviceList.cardView2.enable(false)
                        }
                        if (service3 == null) {
                            binding.serviceList.cardView3.enable(false)
                        }
                        if (service4 == null) {
                            binding.serviceList.cardView4.enable(false)
                        }
                        if (service5 == null) {
                            binding.serviceList.cardView5.enable(false)
                        }

//                        try {
//                            serviceAdaptor =
//                                ServiceAdapterHorizontal(this, vItem, this)
//                            binding.recyclerView.apply {
//                                setHasFixedSize(true)
//                                adapter = serviceAdaptor
//                                layoutManager =
//                                    LinearLayoutManager(
//                                        this@EditServiceActivity,
//                                        LinearLayoutManager.HORIZONTAL,
//                                        false
//                                    )
//                            }
//
//                            var position = -1
//                            for (i in 0 until servicesItemIntent!!.size) {
//                                if (servicesItemIntent?.get(i)?.serviceId === serviceId) {
//                                    position = i
//                                }
//                            }
//                            binding.recyclerView.scrollToPosition(position)
//
//                            try {
//                                serviceAdaptor.setNotifyBtn(serviceId!!)
//
//                            } catch (n: NullPointerException) {
//
//                            }
//                        } catch (e: NullPointerException) {
//
//                        }
                    } else {
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.selectCar.setOnClickListener {
            intentLauncher.launch(Intent(this, ChooseVehicleActivity::class.java))
        }

        binding.reset.setOnClickListener {
            binding.selectedCarCard.visibility = View.GONE
            binding.selectCar.visibility = View.VISIBLE
        }
        binding.change.setOnClickListener {
            intentLauncher.launch(Intent(this, ChooseVehicleActivity::class.java))
        }

        viewModel.packageProvideItem.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    try {
                        if (it.data?.success == TRUE_INT) {
                            val vItem = it.data.result as ArrayList<ServicePackageProvideItem>
                            servicePackageProvideItem?.clear()
                            servicePackageProvideItem!!.addAll(vItem)
                            servicePackageProvideItem!!.sortBy { it1 ->
                                it1.packageType
                            }
                            servicePackageProvideItem!!.reverse()
                            servicePackageAdapter.setFilter(servicePackageProvideItem!!)

                        } else {
                            servicePackageProvideItem?.clear()
                            servicePackageAdapter.notifyDataSetChanged()
                            Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: NullPointerException) {

                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })
        viewModel.isOnOff.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    try {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == TRUE_INT) {

                            }
                            binding.progressBar.visibility = View.GONE
                            serviceId?.let { it1 -> viewModel.loadUi(it1) }

                        }
                        if (vItem != null) {
                            Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                        }


                    } catch (e: NullPointerException) {

                    }
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
//                     Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT) .show()
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
                try {
                    filterServicePackageProvideItem = filter(servicePackageProvideItem!!, newText);
                    servicePackageAdapter.setFilter(filterServicePackageProvideItem as List<ServicePackageProvideItem?>)
                } catch (e: NullPointerException) {

                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })

        binding.searchBar.setOnCloseListener {
            binding.searchBar.visibility = View.GONE
            binding.searchChip.visibility = View.VISIBLE
            false
        }

        cardClickListeners()
    }

    private fun initilizeAdapter() {
        servicePackageAdapter = ServicePackageAdapter(this, this)
        binding.recyclerView2.apply {
            setHasFixedSize(true)
            adapter = servicePackageAdapter
            layoutManager =
                LinearLayoutManager(
                    this@EditServiceActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
    }

    fun cardClickListeners() {
        binding.serviceList.cardView1.setOnClickListener {
            if (service1 != null) {
                setBackgroundViews1()
                onItemClickUppar(service1!!)
            }
        }

        binding.serviceList.cardView2.setOnClickListener {
            if (service2 != null) {
                setBackgroundViews2()
                onItemClickUppar(service2!!)
            }
        }

        binding.serviceList.cardView3.setOnClickListener {
            if (service3 != null) {
                setBackgroundViews3()
                onItemClickUppar(service3!!)
            }
        }

        binding.serviceList.cardView4.setOnClickListener {
            if (service4 != null) {
                setBackgroundViews4()
                onItemClickUppar(service4!!)
            }
        }

        binding.serviceList.cardView5.setOnClickListener {
            if (service5 != null) {
                setBackgroundViews5()
                onItemClickUppar(service5!!)
            }
        }
    }

    private fun setBackgroundViews1() {
        binding.serviceList.cardView1.setBackgroundResource(R.drawable.cardview_back2)
        binding.serviceList.cardView1.elevation = 10F
        binding.serviceList.cardView2.elevation = 0F
        binding.serviceList.cardView2.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView3.elevation = 0F
        binding.serviceList.cardView3.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView4.elevation = 0F
        binding.serviceList.cardView4.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView5.elevation = 0F
        binding.serviceList.cardView5.setBackgroundResource(R.drawable.cardview_back1)
    }

    private fun setBackgroundViews2() {
        binding.serviceList.cardView2.setBackgroundResource(R.drawable.cardview_back2)
        binding.serviceList.cardView2.elevation = 10F
        binding.serviceList.cardView1.elevation = 0F
        binding.serviceList.cardView1.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView3.elevation = 0F
        binding.serviceList.cardView3.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView4.elevation = 0F
        binding.serviceList.cardView4.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView5.elevation = 0F
        binding.serviceList.cardView5.setBackgroundResource(R.drawable.cardview_back1)
    }

    private fun setBackgroundViews3() {
        binding.serviceList.cardView3.setBackgroundResource(R.drawable.cardview_back2)
        binding.serviceList.cardView3.elevation = 10F
        binding.serviceList.cardView2.elevation = 0F
        binding.serviceList.cardView2.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView1.elevation = 0F
        binding.serviceList.cardView1.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView4.elevation = 0F
        binding.serviceList.cardView4.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView5.elevation = 0F
        binding.serviceList.cardView5.setBackgroundResource(R.drawable.cardview_back1)
    }

    private fun setBackgroundViews4() {
        binding.serviceList.cardView4.setBackgroundResource(R.drawable.cardview_back2)
        binding.serviceList.cardView4.elevation = 10F
        binding.serviceList.cardView2.elevation = 0F
        binding.serviceList.cardView2.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView3.elevation = 0F
        binding.serviceList.cardView3.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView1.elevation = 0F
        binding.serviceList.cardView1.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView5.elevation = 0F
        binding.serviceList.cardView5.setBackgroundResource(R.drawable.cardview_back1)
    }

    private fun setBackgroundViews5() {
        binding.serviceList.cardView5.setBackgroundResource(R.drawable.cardview_back2)
        binding.serviceList.cardView5.elevation = 10F
        binding.serviceList.cardView2.elevation = 0F
        binding.serviceList.cardView2.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView3.elevation = 0F
        binding.serviceList.cardView3.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView4.elevation = 0F
        binding.serviceList.cardView4.setBackgroundResource(R.drawable.cardview_back1)
        binding.serviceList.cardView1.elevation = 0F
        binding.serviceList.cardView1.setBackgroundResource(R.drawable.cardview_back1)
    }


    companion object {
        private const val TAG = "EditServiceActivity"
    }

    private fun filter(
        userList: List<ServicePackageProvideItem>,
        queryText: String
    ): List<ServicePackageProvideItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        var text2: String
        val filteredDataList = ArrayList<ServicePackageProvideItem>()
        for (dataFromDataList in userList) {
            text = dataFromDataList.packageName!!.lowercase(Locale.getDefault())
            text2 = dataFromDataList.packageType!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            } else if (text2.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }


    @SuppressLint("SetTextI18n")
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == VEHICLE_CODE) {
                val vehicleId = result.data?.getStringExtra("vehicleId")
                val vehicle_name = result.data?.getStringExtra("vehicle_name")
                val vehicle_imageUri = result.data?.getStringExtra("vehicle_imageUri")
                val modelId = result.data?.getStringExtra("modelId")
                val model_name = result.data?.getStringExtra("model_name")
                val variantId = result.data?.getStringExtra("variantId")
                val variant_name = result.data?.getStringExtra("variant_name")
                val capacity = result.data?.getStringExtra("capacity")
                val fuel = result.data?.getStringExtra("fuel")
                val brand_name = result.data?.getStringExtra("brand_name")
                val brand_imageUri = result.data?.getStringExtra("brand_imageUri")

                Log.e("result.data", result.data.toString())
                if (vehicle_name != null) {
                    binding.selectedCarCard.visibility = View.VISIBLE
                    binding.selectCar.visibility = View.GONE

                    if (brand_imageUri != null) {

                        val bitmap = brand_imageUri.let { assetsToBitmapBrands(it) }

                        bitmap?.apply {
                            binding.carImage.setImageBitmap(this)
                        }
                        binding.carName.text = "$brand_name $model_name"
//                        binding.carVariant.text = variant_name
                        binding.carFuelType.text = "$fuel"
                    }
                    servicePackageAdapter.setNotifyBtn(1)
                }
            }
        }

    fun onItemClickUppar(serviceListItem: ServiceListItem) {
        binding.title.text = serviceListItem.name
        serviceId = serviceListItem.serviceId
        service_name = serviceListItem.name
        viewModel.getStoredOutletObject()?.outletId?.let {
            serviceId?.let { it1 -> viewModel.getServicePackageProvide(it, it1) }
        }
    }

    override fun onItemClick(serviceListItem: ServiceListItem) {
        binding.title.text = serviceListItem.name
        serviceId = serviceListItem.serviceId
        service_name = serviceListItem.name
        serviceId?.let { serviceAdaptor.setNotifyBtn(it) }
    }

    override fun onButtonClick() {
        // package provider
        intentLauncher.launch(Intent(this, ChooseVehicleActivity::class.java))
    }

    override fun onOnOff(serviceListItem: ServicePackageProvideItem, flag: String) {
        binding.progressBar.visibility = View.VISIBLE
        serviceListItem.vpId?.let {
            viewModel.getStoredOutletObject()?.outletId?.let { it1 ->
                serviceListItem.packageId?.let { it2 ->
                    viewModel.disablePackage(
                        it,
                        it1, it2, flag
                    )
                }
            }
        }
    }
}