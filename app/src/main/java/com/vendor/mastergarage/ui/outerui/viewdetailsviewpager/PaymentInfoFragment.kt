package com.vendor.mastergarage.ui.outerui.viewdetailsviewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.FragmentPaymentInfoBinding
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentInfoFragment : Fragment() {

    lateinit var binding: FragmentPaymentInfoBinding

    private val viewModel: PaymentInfoViewModel by viewModels()
    private var leadId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentInfoBinding.inflate(inflater, container, false)

        try {
            if (arguments != null) {
                leadId = requireArguments().getInt("leadId", -1)
                if (leadId != -1) {
                    Log.e("leadId", leadId.toString())
                    viewModel.getPaymentInfo(leadId!!)
                }
            }
        } catch (e: NullPointerException) {

        }
        viewModel.paymentInfo.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    if (it.data?.success == TRUE_INT) {
                        val payment = it.data.result

                        if (payment != null) {

                            binding.amount.text = "${payment.costs}"
                            if (payment.payment_status == 1) {
                                binding.paymentStatus.text = "Paid"
                                binding.paymentMethod.text = "${payment.payment_remarks}"
                            } else {
                                binding.paymentStatus.text = "Pending"
                                binding.paymentMethod.text = "${payment.payment_remarks}"
                            }
                            binding.paymentTime.text =
                                "${payment.payment_date} at ${payment.payment_time}"
                            binding.paymentId.text = "${payment.payment_transaction_id}"
                            binding.totalAmount.text = "${payment.costs}"
                            if (payment.coupon_code != null) {
                                binding.promoCode.text = "PROMO - (${payment.coupon_code})"
                                binding.promoCodeCost.text = "${payment.coupon_rupee}"
                                binding.codeLayout.visibility = View.VISIBLE
                            } else {
                                binding.codeLayout.visibility = View.GONE
                            }
                            binding.otherCharges.text = "${payment.other_charges}"
                            binding.taxes.text = "${payment.taxes}"

                            var coins = payment.mg_coins
                            if (coins == null) {
                                coins = 0
                            }
                            val cost = (payment.costs!!) - (coins / 20)

                            binding.grandTotal.text = "${
                                addAllCost(
                                    cost.toDouble(),
                                    payment.other_charges?.toDouble(),
                                    payment.coupon_rupee?.toDouble(),
                                    payment.taxes?.toDouble()
                                )
                            }"
                        }
                    } else {
                        Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    Log.e(TAG, it.errorMessage.toString())
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                }
            }

        })


        return binding.root
    }

    companion object {
        private const val TAG = "PaymentInfoFragment"
    }

    private fun addAllCost(
        cost: Double?,
        otherCharges: Double?,
        promocodeCost: Double?,
        taxes: Double?
    ): Double? {
        var grandTotal: Double = 0.0
        var t1: Double = 0.0
        var t2: Double = 0.0
        var t3: Double = 0.0
        var t4: Double = 0.0

        if (cost != null) {
            t1 = cost
        }
        if (otherCharges != null) {
            t2 = otherCharges
        }
        if (promocodeCost != null) {
            t3 = promocodeCost
        }
        if (taxes != null) {
            t4 = taxes
        }

        grandTotal = t1 + t2 + t3 + t4

        return grandTotal
    }

}
