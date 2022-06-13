package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutChooseModelVersionVehicleBinding
import com.vendor.mastergarage.model.VehicleVariantsItem

class ChooseVehicleModelVariantsAdapter(
    private val context: Context,
    private var list: List<VehicleVariantsItem>
//    ,
//    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChooseVehicleModelVariantsAdapter.MyViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(vehicleVariantsItem: VehicleVariantsItem, boolean: Boolean)
        fun onOnOffVariants(vehicleVariantsItem: VehicleVariantsItem, flag: String)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        this.onItemClickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutChooseModelVersionVehicleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vehicleItem = list[position]
        holder.itemBinding.variants.text = vehicleItem.fuel
//        holder.itemBinding.carFuelType.text = "${vehicleItem.capacity}, ${vehicleItem.fuel}"

        holder.itemBinding.checkbox.buttonTintList =
            ContextCompat.getColorStateList(context, R.color.black)

        holder.itemBinding.checkbox.setOnCheckedChangeListener { _, b ->
            if (b) {
                onItemClickListener.onItemClick(vehicleItem, b)
                holder.itemBinding.checkbox.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
//            } else {
//                onItemClickListener.onItemClick(vehicleItem, b)
//                holder.itemBinding.checkbox.buttonTintList =
//                    ContextCompat.getColorStateList(context, R.color.black)
            }
        }

        holder.itemBinding.carName.isChecked = !vehicleItem.status.equals("0")

        holder.itemBinding.carName.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.e(
                "$1 vehicleVariantsItem $vehicleItem",
                vehicleItem.toString()
            )
            val str: String
            if (holder.itemBinding.carName.isChecked) {
                str="1"
                onItemClickListener.onOnOffVariants(vehicleItem, "1")
            } else {
                str="0"
                onItemClickListener.onOnOffVariants(vehicleItem, "0")
            }
            vehicleItem.status = str

        }
    }

    class MyViewHolder(val itemBinding: LayoutChooseModelVersionVehicleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<VehicleVariantsItem?>) {
        list = filterList as ArrayList<VehicleVariantsItem>
        notifyDataSetChanged()
    }
}