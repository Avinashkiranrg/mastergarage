package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutSpinnerBinding
import com.vendor.mastergarage.model.ServicePackageItem

class AddDiscountServiceAdapter(
    private val context: Context,
    private var list: List<ServicePackageItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<AddDiscountServiceAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(serviceListItem: ServicePackageItem, flag: Boolean)
    }

    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        holder.itemBinding.checkbox1.text = serviceList.packageName


        holder.itemBinding.checkbox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.itemBinding.checkbox1.isChecked = true
                holder.itemBinding.checkbox1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue2
                    )
                )
                holder.itemBinding.checkbox1.buttonTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue2))
            } else {
                holder.itemBinding.checkbox1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                holder.itemBinding.checkbox1.buttonTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue))
            }
            onItemClickListener.onItemClick(serviceList, holder.itemBinding.checkbox1.isChecked)

        }
    }

    class MyViewHolder(val itemBinding: LayoutSpinnerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServicePackageItem?>) {
        list = filterList as ArrayList<ServicePackageItem>
        notifyDataSetChanged()
    }
}