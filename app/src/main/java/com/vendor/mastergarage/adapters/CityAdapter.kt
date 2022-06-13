package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.model.CityItem
import java.lang.ref.WeakReference


class CityAdapter(
    private var list: List<CityItem>,
    private val callback: WeakReference<OnItemClickListener>
) :
    RecyclerView.Adapter<CityAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(countryCodeItem: CityItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.countryCode.text =
            "${list[position].name} (${list[position].state})"


        holder.itemView.setOnClickListener {
            callback.get()?.onItemClick(list[position])
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryCode: TextView = itemView.findViewById(R.id.countryCode)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<CityItem?>) {
        list = filterList as ArrayList<CityItem>
        notifyDataSetChanged()
    }

}