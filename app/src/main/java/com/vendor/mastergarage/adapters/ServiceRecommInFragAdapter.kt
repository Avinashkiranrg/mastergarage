package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.LayoutServiceRecommendedBinding
import com.vendor.mastergarage.model.ServiceDetails

class ServiceRecommInFragAdapter(
    private val context: Context,
    private var list: MutableList<ServiceDetails>
) :
    RecyclerView.Adapter<ServiceRecommInFragAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutServiceRecommendedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val serviceList = list[position]
        holder.itemBinding.serviceText.text = "Service ${position + 1}"
        holder.itemBinding.ServiceName.text = "${serviceList.name}"
        holder.itemBinding.cost.text = "₹ ${
            addAllCost(
                serviceList.cost,
                serviceList.other_charges
            )
        }"

    }

    private fun addAllCost(
        cost: Float?,
        otherCharges: Float?,
    ): Float? {
        var grandTotal: Float = 0F
        var t1: Float = 0F
        var t2: Float = 0F

        if (cost != null) {
            t1 = cost
        }
        if (otherCharges != null) {
            t2 = otherCharges
        }
        grandTotal = t1 + t2

        return grandTotal
    }

    class MyViewHolder(val itemBinding: LayoutServiceRecommendedBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServiceDetails?>) {
        list = filterList as ArrayList<ServiceDetails>
        notifyDataSetChanged()
    }

}