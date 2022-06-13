package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.di.AppModule.Companion.FLAG_URL
import com.vendor.mastergarage.model.CountryCodeItem
import com.vendor.mastergarage.utlis.loadSvg
import java.lang.ref.WeakReference


class CountryAdapter(
    private var list: List<CountryCodeItem>,
    private val callback: WeakReference<OnItemClickListener>
) :
    RecyclerView.Adapter<CountryAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(countryCodeItem: CountryCodeItem, url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.countryCode.text =
            "${list[position].name} (${list[position].dialCode})"
        val imageUri =
            "$FLAG_URL${list[position].code}.svg"

        holder.imageView.loadSvg(imageUri)

        holder.itemView.setOnClickListener {
            callback.get()?.onItemClick(list[position], imageUri)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryCode: MaterialTextView = itemView.findViewById(R.id.countryCode)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<CountryCodeItem?>) {
        list = filterList as ArrayList<CountryCodeItem>
        notifyDataSetChanged()
    }

}