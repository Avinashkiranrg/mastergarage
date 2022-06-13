package com.vendor.mastergarage.ui.outerui.fragment_main.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vendor.mastergarage.adapters.ServiceAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.CITY_CODE
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.FragmentServiceBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.model.ServiceListItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.citymodel.FindCityActivity
import com.vendor.mastergarage.ui.notifications.NotificationUi
import com.vendor.mastergarage.ui.outerui.manageservice.EditServiceActivity
import com.vendor.mastergarage.utlis.enable
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ServiceFragment : Fragment(), ServiceAdapter.OnItemClickListener {

    lateinit var binding: FragmentServiceBinding

    private val viewModel: ServiceVewModel by viewModels()

    private lateinit var serviceAdapter: ServiceAdapter

    private var service1: ServiceListItem? = null
    private var service2: ServiceListItem? = null
    private var service3: ServiceListItem? = null
    private var service4: ServiceListItem? = null
    private var service5: ServiceListItem? = null

    @Inject
    lateinit var vendorPreference: VendorPreference

    var servicesItem: ArrayList<ServiceListItem>? = null
    var servicesItemIntent: ArrayList<ServiceListItem>? = null
    var filteredServiceListItem: List<ServiceListItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceBinding.inflate(inflater, container, false)
//        setHasOptionsMenu(true)
//
//        binding.toolbar.inflateMenu(R.menu.menu_header)

        binding.notification.setOnClickListener {
            val bottomSheetFragment: BottomSheetDialogFragment = NotificationUi()
            bottomSheetFragment.show(childFragmentManager, NotificationUi.TAG)
        }


        servicesItem = ArrayList()
        servicesItemIntent = ArrayList()

        viewModel.service.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val vItem = it.data.result as MutableList<ServiceListItem>

                        Log.e("List ${vItem.size}", vItem.toString())

                        vItem.forEach { it1 ->
                            if (it1.serviceId == 1) {
                                service1 = it1
                                binding.serviceList.text1.isChecked = !service1?.status.equals("0")
                                Log.e("isChecked 2", service1?.status.toString())
                            }
                            if (it1.serviceId == 4) {
                                service2 = it1
                                binding.serviceList.text2.isChecked = !service2?.status.equals("0")
                                Log.e("isChecked 2", service2?.status.toString())
                            }
                            if (it1.serviceId == 6) {
                                service3 = it1
                                binding.serviceList.text3.isChecked = !service3?.status.equals("0")
                                Log.e("isChecked 3", service3?.status.toString())
                            }
                            if (it1.serviceId == 7) {
                                service4 = it1
                                binding.serviceList.text4.isChecked = !service4?.status.equals("0")
                                Log.e("isChecked 4", service4?.status.toString())
                            }
                            if (it1.serviceId == 3) {
                                service5 = it1
                                binding.serviceList.text5.isChecked = !service5?.status.equals("0")
                                Log.e("isChecked 5", service5?.status.toString())
                            }
                        }

                        if (service1 != null) {
                            binding.serviceList.text1.isChecked = !service1?.status.equals("0")
                        } else {
                            binding.serviceList.cardView1.enable(false)
                        }
                        if (service2 != null) {
                            binding.serviceList.text2.isChecked = !service2?.status.equals("0")
                        } else {
                            binding.serviceList.cardView2.enable(false)
                        }
                        if (service3 != null) {
                            binding.serviceList.text3.isChecked = !service3?.status.equals("0")
                        } else {
                            binding.serviceList.cardView3.enable(false)
                        }
                        if (service4 != null) {
                            binding.serviceList.text4.isChecked = !service4?.status.equals("0")
                        } else {
                            binding.serviceList.cardView4.enable(false)
                        }
                        if (service5 != null) {
                            binding.serviceList.text5.isChecked = !service5?.status.equals("0")
                        } else {
                            binding.serviceList.cardView5.enable(false)
                        }

//                        try {
//                            servicesItem!!.addAll(vItem)
//                            servicesItemIntent!!.addAll(vItem)
//                        } catch (e: NullPointerException) {
//
//                        }
//                        vItem.sortBy { it.serviceId }
//                        serviceAdapter = ServiceAdapter(requireActivity(), vItem, this)
//                        binding.recyclerView.apply {
//                            setHasFixedSize(true)
//                            adapter = serviceAdapter
//                            layoutManager =
//                                LinearLayoutManager(
//                                    requireActivity(),
//                                    LinearLayoutManager.VERTICAL,
//                                    false
//                                )
//                        }
                    } else {
                        Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        viewModel.isOnOff.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    try {
                        val vItem = it.data
                        if (vItem != null) {
                            if (vItem.success == Constraints.TRUE_INT) {
                            }
                            binding.progressBar.visibility = View.GONE
                            viewModel.loadUi()

                        }
                        if (vItem != null) {
                            Toast.makeText(requireActivity(), vItem.message, Toast.LENGTH_SHORT)
                                .show()
                        }


                    } catch (e: NullPointerException) {

                    }
                }
                is Response.Failure -> {
//                     Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT) .show()
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

//        binding.searchChip.setOnClickListener {
//            binding.searchBar.visibility = View.VISIBLE
//            binding.searchChip.visibility = View.GONE
//
//        }

//        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(newText: String): Boolean {
//                try {
//                    filteredServiceListItem = filter(servicesItem!!, newText);
//                    serviceAdapter.setFilter(filteredServiceListItem as List<ServiceListItem?>)
//                } catch (e: NullPointerException) {
//
//                }
//                return false
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//        })

//        binding.searchBar.setOnCloseListener {
//            binding.searchBar.visibility = View.GONE
//            binding.searchChip.visibility = View.VISIBLE
//            false
//        }

        vendorPreference.getCity.asLiveData().observe(viewLifecycleOwner, Observer {
            binding.locationChip.text = it
        })

        gotoCitySearch()

        cardClickListeners()


        return binding.root
    }

    fun cardClickListeners() {
        binding.serviceList.cardView1.setOnClickListener {
            if (service1 != null) {
                onItemClickGoto(service1!!)
            }
        }

        binding.serviceList.cardView2.setOnClickListener {
            if (service2 != null) {
                onItemClickGoto(service2!!)
            }
        }

        binding.serviceList.cardView3.setOnClickListener {
            if (service3 != null) {
                onItemClickGoto(service3!!)
            }
        }

        binding.serviceList.cardView4.setOnClickListener {
            if (service4 != null) {
                onItemClickGoto(service4!!)
            }
        }

        binding.serviceList.cardView5.setOnClickListener {
            if (service5 != null) {
                onItemClickGoto(service5!!)
            }
        }

        binding.serviceList.text1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (service1 != null) {
                if (isChecked) {
                    setOnOff(service1!!, "1")
                } else {
                    setOnOff(service1!!, "0")
                }
            }
        }
        binding.serviceList.text2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (service2 != null) {
                if (isChecked) {
                    setOnOff(service2!!, "1")
                } else {
                    setOnOff(service2!!, "0")
                }
            }
        }
        binding.serviceList.text3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (service3 != null) {
                if (isChecked) {
                    setOnOff(service3!!, "1")
                } else {
                    setOnOff(service3!!, "0")
                }
            }
        }
        binding.serviceList.text4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (service4 != null) {
                if (isChecked) {
                    setOnOff(service4!!, "1")
                } else {
                    setOnOff(service4!!, "0")
                }
            }
        }
        binding.serviceList.text5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (service5 != null) {
                if (isChecked) {
                    setOnOff(service5!!, "1")
                } else {
                    setOnOff(service5!!, "0")
                }
            }
        }

    }

    fun onItemClickGoto(serviceListItem: ServiceListItem) {
        Log.e("servicesIt   ItemIntent", servicesItemIntent?.size.toString())

        if (servicesItemIntent != null) {
            Log.e("servicesItemIntent", "not null")

            val intent = Intent(requireActivity(), EditServiceActivity::class.java)
            intent.putExtra("list", servicesItemIntent)
            intent.putExtra("id", serviceListItem.serviceId)
            intent.putExtra("name", serviceListItem.name)
            requireActivity().startActivity(intent)
        } else {
            Log.e("servicesItemIntent", "null")
        }

    }

    fun setOnOff(serviceListItem: ServiceListItem, flag: String) {
        Log.e(
            "Data sss",
            "${serviceListItem.serviceId}, ${serviceListItem.vsId}, ${viewModel.getStoredOutletObject()?.outletId}, $flag"
        )

        binding.progressBar.visibility = View.VISIBLE
        serviceListItem.vsId?.let {
            viewModel.getStoredOutletObject()?.outletId?.let { it1 ->
                serviceListItem.serviceId?.let { it2 ->
                    viewModel.disableService(
                        it,
                        it1,
                        it2,
                        flag
                    )
                }
            }
        }
    }

    private fun gotoCitySearch() {
        binding.locationChip.setOnClickListener {
            intentLauncher.launch(Intent(requireActivity(), FindCityActivity::class.java))
        }
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CITY_CODE) {
                val city = result.data?.getStringExtra("city")
                binding.locationChip.setText(city)
            }
        }


    private fun filter(userList: List<ServiceListItem>, queryText: String): List<ServiceListItem> {
        var newText = queryText
        newText = newText.lowercase(Locale.getDefault())
        var text: String
        val filteredDataList = ArrayList<ServiceListItem>()
        for (dataFromDataList in userList) {
            text = dataFromDataList.name!!.lowercase(Locale.getDefault())
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }

    companion object {
        private const val TAG = "ServiceFragment"
    }

    override fun onItemClick(serviceListItem: ServiceListItem) {
        Log.e("servicesIt   ItemIntent", servicesItemIntent?.size.toString())

        if (servicesItemIntent != null) {
            Log.e("servicesItemIntent", "not null")

            val intent = Intent(requireActivity(), EditServiceActivity::class.java)
            intent.putExtra("list", servicesItemIntent)
            intent.putExtra("id", serviceListItem.serviceId)
            intent.putExtra("name", serviceListItem.name)
            requireActivity().startActivity(intent)
        } else {
            Log.e("servicesItemIntent", "null")
        }

    }

    override fun onOnOff(serviceListItem: ServiceListItem, flag: String) {
        Log.e(
            "Data sss",
            "${serviceListItem.serviceId}, ${serviceListItem.vsId}, ${viewModel.getStoredOutletObject()?.outletId}, $flag"
        )

        binding.progressBar.visibility = View.VISIBLE
        serviceListItem.vsId?.let {
            viewModel.getStoredOutletObject()?.outletId?.let { it1 ->
                serviceListItem.serviceId?.let { it2 ->
                    viewModel.disableService(
                        it,
                        it1,
                        it2,
                        flag
                    )
                }
            }
        }
    }
}