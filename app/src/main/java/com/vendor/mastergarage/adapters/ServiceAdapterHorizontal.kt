package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutManageServiceHorizontalBinding
import com.vendor.mastergarage.model.ServiceListItem
import com.vendor.mastergarage.utlis.imageFromUrl

class ServiceAdapterHorizontal(
    private val context: Context,
    private var list: List<ServiceListItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ServiceAdapterHorizontal.MyViewHolder>() {

    private var selectedPosition = -1
    lateinit var recyclerView: RecyclerView
    private var notifyBtn = -1


    interface OnItemClickListener {
        fun onItemClick(serviceListItem: ServiceListItem)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutManageServiceHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        holder.itemBinding.materialTextView4.text = serviceList.name

        if (serviceList.imageUri != null) {
            holder.itemBinding.imageView.imageFromUrl(serviceList.imageUri)
        }

        if (notifyBtn == serviceList.serviceId) {
            holder.itemBinding.main.setBackgroundResource(R.drawable.cardview_back2)
            holder.itemBinding.main.elevation= 10F
        } else {
            holder.itemBinding.main.elevation= 0F
            holder.itemBinding.main.setBackgroundResource(R.drawable.cardview_back1)
        }

        holder.itemBinding.root.setOnClickListener {
            onItemClickListener.onItemClick(serviceList)
        }

    }

    class MyViewHolder(val itemBinding: LayoutManageServiceHorizontalBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServiceListItem?>) {
        list = filterList as ArrayList<ServiceListItem>
        notifyDataSetChanged()
    }

    fun setNotifyBtn(notify: Int) {
        notifyBtn = notify as Int
        notifyDataSetChanged()
    }
}