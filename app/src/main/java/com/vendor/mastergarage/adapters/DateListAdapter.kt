package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutDateBinding
import com.vendor.mastergarage.utlis.enable
import com.vendor.mastergarage.utlis.isBetweenValidTime
import com.vendor.mastergarage.utlis.isBetweenValidTimeCombined
import java.text.SimpleDateFormat
import java.util.*


class DateListAdapter(
    private val context: Context,
    private var list: List<Date>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DateListAdapter.MyViewHolder>() {
    private var notifyBtn: Date = Date()

    interface OnItemClickListener {
        fun onItemClick(date: Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.root.post {
            itemBinding.root.layoutParams.height = parent.width / 4
            itemBinding.root.requestLayout()
        }
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        var formatter = SimpleDateFormat("MMM", Locale.getDefault())
        holder.itemBinding.month.text = "${formatter.format(serviceList)}"
        formatter = SimpleDateFormat("EEEE", Locale.getDefault())
        holder.itemBinding.day.text = "${formatter.format(serviceList)}"
        formatter = SimpleDateFormat("dd", Locale.getDefault())
        holder.itemBinding.date.text = "${formatter.format(serviceList)}"


        val formatter2 = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val date = "${formatter2.format(Date())}"
        val formatter22 = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

        val startDate = formatter22.parse("$date");
        val calendarStart = Calendar.getInstance()
        calendarStart.time = startDate
        calendarStart.add(Calendar.DATE, 2)
        val endDate = calendarStart.time
//
        val todaystartDate: String = formatter2.format(serviceList)
        val todayendDate: String = formatter2.format(endDate)

        Log.e("todaystartDate", todaystartDate)
        Log.e("todayendDate", todayendDate)

        val boolean = isBetweenValidTimeCombined(startDate, endDate, serviceList)

        if (!boolean) {
//        if (serviceList.after(dEnd)) {
            Log.e("@@@@@@@@------", todaystartDate)
            holder.itemBinding.main.enable(false)
            holder.itemBinding.main.setBackgroundResource(R.drawable.date_card_back3)
            holder.itemBinding.main.elevation = 0F
            holder.itemBinding.month.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_color_
                )
            )
            holder.itemBinding.day.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_color_
                )
            )
            holder.itemBinding.date.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_color_
                )
            )
        } else {
            holder.itemBinding.main.enable(true)
            holder.itemBinding.main.setBackgroundResource(R.drawable.date_card_back1)
            holder.itemBinding.main.elevation = 0F
            holder.itemBinding.month.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
            holder.itemBinding.day.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
            holder.itemBinding.date.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
            Log.e("-------------", todayendDate)

        }

        if (notifyBtn == serviceList) {
            holder.itemBinding.main.setBackgroundResource(R.drawable.date_card_back2)
            holder.itemBinding.main.elevation = 10F
        }


        holder.itemBinding.main.setOnClickListener {
            onItemClickListener.onItemClick(serviceList)
        }
    }

    class MyViewHolder(val itemBinding: LayoutDateBinding) :
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