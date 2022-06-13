package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.LayoutServiceRequestedBinding
import com.vendor.mastergarage.model.ServiceRequestedItem

class ServiceRequestedAdapter(
    private val context: Context,
    private var list: List<ServiceRequestedItem>,
) :
    RecyclerView.Adapter<ServiceRequestedAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(vehiclesItem: ServiceRequestedItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutServiceRequestedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vehicleItem = list[position]
        holder.itemBinding.ServiceName.text = vehicleItem.packageName

        val des = "${vehicleItem.monthsOrKms}"
        var lastIndex = des.indexOf(",")
        if (lastIndex == -1) {
            lastIndex = des.length
        }
        holder.itemBinding.ServiceNameAll.text = "${des.substring(0, lastIndex)}"

        holder.itemBinding.servicePrice.text = "${vehicleItem.costs}"

        holder.itemBinding.viewDetails.setOnClickListener {

        }
    }

    class MyViewHolder(val itemBinding: LayoutServiceRequestedBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServiceRequestedItem?>) {
        list = filterList as ArrayList<ServiceRequestedItem>
        notifyDataSetChanged()
    }
}