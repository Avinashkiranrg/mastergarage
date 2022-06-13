package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.LayoutManageServiceBinding
import com.vendor.mastergarage.model.ServiceListItem
import com.vendor.mastergarage.utlis.imageFromUrl

class ServiceAdapter(
    private val context: Context,
    private var list: List<ServiceListItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(serviceListItem: ServiceListItem)
        fun onOnOff(serviceListItem: ServiceListItem, flag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutManageServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        holder.itemBinding.serviceName.text = serviceList.name

        if (serviceList.imageUri != null) {
            holder.itemBinding.imageView.imageFromUrl(serviceList.imageUri)
        }

        holder.itemBinding.arrowImage.setOnClickListener {
            onItemClickListener.onItemClick(serviceList)
        }
        holder.itemBinding.serviceName.isChecked = !serviceList.status.equals("0")


        holder.itemBinding.serviceName.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onItemClickListener.onOnOff(serviceList, "1")
            } else {
                onItemClickListener.onOnOff(serviceList, "0")
            }
        }
    }

    class MyViewHolder(val itemBinding: LayoutManageServiceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServiceListItem?>) {
        list = filterList as ArrayList<ServiceListItem>
        notifyDataSetChanged()
    }
}