package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.LayoutChooseVehicleBinding
import com.vendor.mastergarage.model.VehicleProvideItem
import com.vendor.mastergarage.utlis.assetsToBitmapBrands

class ChooseVehicleAdapter(
    private val context: Context,
    private var list: List<VehicleProvideItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChooseVehicleAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(vehiclesItem: VehicleProvideItem)
        fun onOnOff(vehiclesItem: VehicleProvideItem, flag: String)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutChooseVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vehicleItem = list[position]
        holder.itemBinding.carName.text = vehicleItem.name

//        if (vehicleItem.imageUri != null) {
//            holder.itemBinding.imageView.imageFromUrl(vehicleItem.imageUri)
//        }

        val bitmap = vehicleItem.imageUri?.let { context.assetsToBitmapBrands(it) }

        bitmap?.apply {
            holder.itemBinding.imageView.setImageBitmap(this)
        }

        holder.itemBinding.arrowImage.setOnClickListener {
            onItemClickListener.onItemClick(vehicleItem)
        }
        holder.itemBinding.carName.isChecked = !vehicleItem.status.equals("0")

        holder.itemBinding.carName.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onItemClickListener.onOnOff(vehicleItem, "1")
            } else {
                onItemClickListener.onOnOff(vehicleItem, "0")
            }
        }
    }

    class MyViewHolder(val itemBinding: LayoutChooseVehicleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<VehicleProvideItem?>) {
        list = filterList as ArrayList<VehicleProvideItem>
        notifyDataSetChanged()
    }
}