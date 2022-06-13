package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.discount

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.FaqAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.FOR_ALL
import com.vendor.mastergarage.constraints.Constraints.Companion.FOR_FIRST_TIME
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.FragmentAddDiscountsBinding
import com.vendor.mastergarage.model.ServicePackageItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.utlis.EndDatePickerFragment
import com.vendor.mastergarage.utlis.StartDatePickerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddDiscountsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var binding: FragmentAddDiscountsBinding

    private val viewModel: AddDiscountFragViewModel by viewModels()

    lateinit var faqAdapter: FaqAdapter
    var userList: ArrayList<ServicePackageItem>? = null
    var filteredDataList: ArrayList<ServicePackageItem?>? = null

    lateinit var typeDiscountSpinner: Spinner
    var typeDiscount = arrayOf("Flat", "Discount")
    private var first_time_user: String? = null
    private var all_user: String? = null
    private var apply_users: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDiscountsBinding.inflate(inflater, container, false)

        binding.logo.setOnClickListener {
            requireActivity().onBackPressed()
        }
        typeDiscountSpinner = Spinner(requireActivity())
        userList = ArrayList()
        filteredDataList = ArrayList()


        // type discount
        typeDiscountSpinner.adapter =
            ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                typeDiscount
            )
        typeDiscountSpinner.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            typeDiscountSpinner.background.colorFilter =
                BlendModeColorFilter(Color.TRANSPARENT, BlendMode.CLEAR)
        } else {
            typeDiscountSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        binding.layout1.addView(typeDiscountSpinner)

        binding.typeOfDiscount.setOnClickListener {
            typeDiscountSpinner.performClick()
        }
        typeDiscountSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long) {
                binding.typeOfDiscount.setText(typeDiscount[arg2])

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        // applicable on


        binding.startDate.tag = 0
        binding.endDate.tag = 0
        binding.startDate.setOnClickListener {
            binding.startDate.tag = 1
            val datePicker: DialogFragment = StartDatePickerFragment()
            datePicker.show(childFragmentManager, "date picker")
        }

        binding.endDate.setOnClickListener {
            if (binding.startDate.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please select Start Date", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.endDate.tag = 2
                val datePicker: DialogFragment = EndDatePickerFragment()
                val bundle = Bundle()
                bundle.putString("start_date", binding.startDate.text.toString())
                datePicker.arguments = bundle
                datePicker.show(childFragmentManager, "date picker")
            }
        }

        binding.applicableOn.setOnClickListener {
            intentLauncher.launch(
                Intent(
                    requireActivity(),
                    AddServiceInDiscountActivity::class.java
                )
            )
        }

        binding.submit.setOnClickListener {
            if (binding.typeOfDiscount.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please select discount type", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.startDate.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please start date", Toast.LENGTH_SHORT).show()
            } else if (binding.endDate.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please end date", Toast.LENGTH_SHORT).show()

            } else if (binding.applicableOn.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please select applicable on", Toast.LENGTH_SHORT)
                    .show()

            } else if (binding.minimumOrderValue.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter minimum order value",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (binding.discountValue.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please enter discount value", Toast.LENGTH_SHORT)
                    .show()

            } else if (binding.maximumDiscount.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter maximum discount",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (binding.couponCode.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please enter coupon code", Toast.LENGTH_SHORT)
                    .show()

            } else if (apply_users == null) {
                Toast.makeText(requireActivity(), "Please select Beneficiaries", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.submit.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
                sendData()
            }
        }
        checkBox3(requireActivity())

        viewModel.response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == TRUE_INT) {
                            requireActivity().onBackPressed()
                        } else {
                            binding.submit.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                        vItem.message?.let { it1 -> Log.e("vItem.message.toString() ", it1) }
                        Toast.makeText(requireActivity(), vItem.message, Toast.LENGTH_SHORT).show()
                    }

                }
                is Response.Failure -> {
                    binding.submit.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        return binding.root
    }

    private fun sendData() {
        val gson = Gson()
        val applicable_on = gson.toJson(filteredDataList)

        val end_date: String = binding.startDate.text.toString()
        val start_date: String = binding.startDate.text.toString()
        val type_discount: String = binding.typeOfDiscount.text.toString()
        val min_order_value: Int = binding.minimumOrderValue.text.toString().toInt()
        val discount_code: String = binding.couponCode.text.toString()
        val outletId: Int = 1
        val max_discount: Int = binding.maximumDiscount.text.toString().toInt()
        val discount_value: Int = binding.discountValue.text.toString().toInt()
        val apply_users: String? = apply_users

//        Log.e(
//            "Data", "$applicable_on, $end_date, $start_date, $type_discount, $min_order_value, " +
//                    "$discount_code, $outletId, $max_discount, $discount_value, $apply_users"
//        )

        if (apply_users != null) {
            try {
                viewModel.getStoredOutletObject()!!.outletId?.let {
                    viewModel.addDiscounts(
                        applicable_on,
                        end_date,
                        start_date,
                        type_discount,
                        min_order_value,
                        discount_code,
                        it,
                        max_discount,
                        discount_value,
                        apply_users
                    )
                }
            } catch (e: NullPointerException) {

            }
        }

    }

    private fun checkBox3(context: Context) {
        binding.forAll.buttonTintList =
            ContextCompat.getColorStateList(context, R.color.blue2)
        binding.forAll.isChecked = true
        apply_users = FOR_FIRST_TIME

        binding.forFirstTime.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                apply_users = FOR_FIRST_TIME
                binding.forFirstTime.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                binding.forAll.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.forAll.isChecked = false
            } else {
                binding.forFirstTime.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

        binding.forAll.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                apply_users = FOR_ALL
                binding.forAll.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                binding.forFirstTime.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.forFirstTime.isChecked = false
            } else {
                binding.forAll.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

    }

    private fun checkBox2(context: Context) {
        binding.forAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.forAll.isChecked = true
                binding.forAll.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.blue2
                    )
                )
                binding.forAll.buttonTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue2))
            } else {
                binding.forAll.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                binding.forAll.buttonTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue))
            }
        }
    }

    private fun checkBox1(context: Context) {
        binding.forFirstTime.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.forFirstTime.isChecked = true
                binding.forFirstTime.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue2
                    )
                )
                binding.forFirstTime.buttonTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue2))
                first_time_user = "1"
            } else {
                binding.forFirstTime.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                binding.forFirstTime.buttonTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue))

            }
        }
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Constraints.DATA_CODE) {
                val list = result.data?.getParcelableArrayListExtra<ServicePackageItem>("list")

                if (list != null) {
                    Log.e("list", list.size.toString())
                    filteredDataList?.addAll(list)
                }
                Log.e("list", list.toString())
                if (list != null) {
                    val str = StringBuilder("")
                    list.forEach {
                        str.append("${it.packageName}, ")
                    }
                    binding.applicableOn.setText(str)
                }
            }
        }


    companion object {
        private const val TAG = "AddDiscountsFragment"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date: String = "$year-${month + 1}-$dayOfMonth"
        Log.e("date", date)
        when {
            binding.startDate.tag == 1 -> {
                binding.startDate.tag = 0
                binding.startDate.setText(date)
            }
            binding.endDate.tag == 2 -> {
                binding.endDate.tag = 0
                binding.endDate.setText(date)
            }
            else -> {
                Log.e("binding.endDate.tag", binding.endDate.tag.toString())
                Log.e("binding.startDate.tag", binding.startDate.tag.toString())
            }
        }
    }


}