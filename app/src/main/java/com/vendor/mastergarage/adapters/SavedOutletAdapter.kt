package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.constraints.Constraints.Companion.OUTLET_STORE
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE
import com.vendor.mastergarage.databinding.SavedOutletItemBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.OutletsItem

class SavedOutletAdapter(
    private val context: Context,
    private var list: List<OutletsItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SavedOutletAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(outletsItem: OutletsItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            SavedOutletItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val outlet = list[position]
        holder.itemBinding.address.text = outlet.address
        holder.itemBinding.city.text = outlet.city
        holder.itemBinding.name.text = outlet.name

//        val id: String =
//            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val outletsItems = ModelPreferencesManager.get<OutletsItem>(OUTLET_STORE)
        if (outletsItems != null) {
            if (outlet.outletId == outletsItems.outletId) {
                holder.itemBinding.imageView.setImageResource(R.drawable.check_circle)
            } else {
                holder.itemBinding.imageView.setImageResource(R.drawable.check_circle_outline)
            }
        }else {
            holder.itemBinding.imageView.setImageResource(R.drawable.check_circle_outline)
        }

        holder.itemBinding.root.setOnClickListener {
            onItemClickListener.onItemClick(outlet)
            ModelPreferencesManager.put(outlet, Constraints.OUTLET_STORE)
            notifyDataSetChanged()
        }
    }

    class MyViewHolder(val itemBinding: SavedOutletItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<OutletsItem?>) {
        list = filterList as ArrayList<OutletsItem>
        notifyDataSetChanged()
    }
}