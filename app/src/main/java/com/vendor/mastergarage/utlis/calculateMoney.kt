package com.vendor.mastergarage.utlis

import com.vendor.mastergarage.model.Payment


fun calculateMoney(it: Payment): String {
    var rupee = "0"
    try {
        val coins = it.mg_coins
        if (coins != null && it.taxes != null) {
            if (coins.toInt() > 0) {
                val cost = (it.costs!!) - (coins / 20)

                rupee = "${calPriceTaxes(cost.toInt(), it.taxes)}"

            } else {
                rupee = "${calPriceTaxes(it.costs!!, it.taxes)}"
            }
        }
    } catch (n: NullPointerException) {

    }
    return rupee
}

fun calPriceTaxes(total: Int, tax: Int): Int {
    var totalWithTax = total
    totalWithTax += tax
    return totalWithTax
}