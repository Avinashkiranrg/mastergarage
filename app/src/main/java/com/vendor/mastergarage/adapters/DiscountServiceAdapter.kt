package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uidesign.model.DiscountsItem
import com.vendor.mastergarage.constraints.Constraints.Companion.type_discount
import com.vendor.mastergarage.databinding.LayoutDiscountBinding

class DiscountServiceAdapter(
    private val context: Context,
    private var list: List<DiscountsItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DiscountServiceAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(serviceListItem: DiscountsItem)
    }

    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        holder.itemBinding.StartDate.text = serviceList.startDate
        holder.itemBinding.EndDate.text = serviceList.endDate
        holder.itemBinding.DiscountCode.text = serviceList.discountCode
        holder.itemBinding.MaximumDiscount.text = "₹ ${serviceList.maxDiscount}"
        holder.itemBinding.MinimumOrderValue.text = "₹ ${serviceList.minOrderValue}"

        val str = StringBuilder("")
        when {
            serviceList.applicableOn.size > 3 -> {
                str.append(
                    "${serviceList.applicableOn[0].packageName}, " +
                            "${serviceList.applicableOn[1].packageName}, " +
                            "${serviceList.applicableOn[2].packageName}, and ${(serviceList.applicableOn.size - 3)}"
                )
            }
            serviceList.applicableOn.size == 3 -> {
                str.append(
                    "${serviceList.applicableOn[0].packageName}, " +
                            "${serviceList.applicableOn[1].packageName}, " +
                            serviceList.applicableOn[2].packageName
                )
            }
            serviceList.applicableOn.size < 3 -> {
                str.append(
                    "${serviceList.applicableOn[0].packageName}, " +
                            serviceList.applicableOn[1].packageName
                )
            }
            else -> {
                str.append(
                    serviceList.applicableOn[0].packageName
                )
            }
        }
        holder.itemBinding.ApplicableOn.text = "$str"
        if (serviceList.typeDiscount == type_discount) {
            holder.itemBinding.Discount.text = "${serviceList.discountValue} %"
            holder.itemBinding.title.text = "Use Code: ${serviceList.discountCode} and ${serviceList.discountValue} % off upto ₹${serviceList.maxDiscount} on order above ₹${serviceList.minOrderValue}"
        }else {
            holder.itemBinding.Discount.text = "FLAT ${serviceList.discountValue}"
            holder.itemBinding.title.text = "Use Code: ${serviceList.discountCode} and get Flat " +
                    "₹${serviceList.discountValue} on order above ₹${serviceList.minOrderValue}"
        }

        holder.itemBinding.root.setOnClickListener {
            onItemClickListener.onItemClick(serviceList)
        }
    }

    class MyViewHolder(val itemBinding: LayoutDiscountBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<DiscountsItem?>) {
        list = filterList as ArrayList<DiscountsItem>
        notifyDataSetChanged()
    }
}