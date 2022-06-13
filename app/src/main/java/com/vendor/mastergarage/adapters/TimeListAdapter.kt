package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutTimeBinding
import com.vendor.mastergarage.utlis.enable
import java.text.SimpleDateFormat
import java.util.*

class TimeListAdapter(
    private val context: Context,
    private var list: List<Date>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<TimeListAdapter.MyViewHolder>() {
    private var notifyBtn: Date = Date()

    interface OnItemClickListener {
        fun onItemTimeClick(date: Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        var formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        holder.itemBinding.time.text = "${formatter.format(serviceList)}"

        if (serviceList.before(Date())) {
            holder.itemBinding.main.enable(false)
            holder.itemBinding.main.elevation = 0F
            holder.itemBinding.main.setBackgroundResource(R.drawable.time_card_back3)
            holder.itemBinding.time.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_color_
                )
            )
        } else {
            holder.itemBinding.root.enable(true)
            holder.itemBinding.time.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
        }

        if (notifyBtn == serviceList) {
            holder.itemBinding.main.setBackgroundResource(R.drawable.time_card_back2)
            holder.itemBinding.main.elevation = 10F
        } else {
            if (serviceList.before(Date())) {
                holder.itemBinding.main.enable(false)
                holder.itemBinding.main.elevation = 0F
                holder.itemBinding.main.setBackgroundResource(R.drawable.time_card_back3)
                holder.itemBinding.time.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light_color_
                    )
                )
            } else {
                holder.itemBinding.root.enable(true)
                holder.itemBinding.main.setBackgroundResource(R.drawable.date_card_back1)
                holder.itemBinding.main.elevation = 0F
                holder.itemBinding.time.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
            }
        }

        holder.itemBinding.root.setOnClickListener {
            onItemClickListener.onItemTimeClick(serviceList)
        }
    }

    class MyViewHolder(val itemBinding: LayoutTimeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<Date?>) {
        list = filterList as ArrayList<Date>
        notifyDataSetChanged()
    }

    fun setNotifyBtn(notify: Date) {
        notifyBtn = notify
        notifyDataSetChanged()
    }
}