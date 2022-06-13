package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.LayoutServiceRecommendationsBinding
import com.vendor.mastergarage.model.ServiceDetails

class ServiceRecommAdapter(
    private val context: Context,
    private var list: MutableList<ServiceDetails>,
    private val onItemClickListener: OnItemClickListener

) :
    RecyclerView.Adapter<ServiceRecommAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutServiceRecommendationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    interface OnItemClickListener {
        fun onSelect(serviceDetails: ServiceDetails)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val serviceList = list[position]
        holder.itemBinding.serviceNo.text = "Service ${position + 1}"
        holder.itemBinding.ServiceName.text = "${serviceList.name}"
        holder.itemBinding.cost.text = "₹ ${serviceList.cost}"


        if (serviceList.approval_status == 1) {
            holder.itemBinding.approval.text = "Approved"
        } else {
            holder.itemBinding.approval.text = "Approval Pending"
        }
        try {
            Log.e("serviceList.spares", serviceList.spares.toString())
            holder.itemBinding.manufacturerName.text = "${serviceList.spares[0].manufacturer_name}"
            if (serviceList.spares[0].part_name.equals("0")) {
//                holder.itemBinding.partNumber.text =
//                    "${serviceList.additional_details}"
            }else{
                holder.itemBinding.warranty.text =
                    "${serviceList.spares[0].part_name}, ${serviceList.spares[0].warranty}"
            }
            if (serviceList.spares[0].year != 0) {
                holder.itemBinding.warranty.text = "${serviceList.spares[0].year} year Warranty"
            }

            when (serviceList.spares[0].part_type) {
                Constraints.OEM_PART -> {
                    holder.itemBinding.osmAndOem.text = "OEM Parts"
                }
                Constraints.OES_PART -> {
                    holder.itemBinding.osmAndOem.text = "OES Parts"
                }
                else -> {
                    holder.itemBinding.osmAndOem.text = "Other Parts"
                }
            }

        } catch (n: IndexOutOfBoundsException) {

        }
        holder.itemBinding.editBtn.setOnClickListener {
            onItemClickListener.onSelect(serviceList)
        }

    }

    class MyViewHolder(val itemBinding: LayoutServiceRecommendationsBinding) :
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