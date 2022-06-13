package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutChooseModelVehicleBinding
import com.vendor.mastergarage.model.VehicleModelItem
import com.vendor.mastergarage.model.VehicleVariantsItem

class ChooseVehicleModelAdapter(
    private val context: Context,
    private var list: List<VehicleModelItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChooseVehicleModelAdapter.MyViewHolder>() {
    private var selectedPosition = -1


//    private var variantsItem: VehicleVariantsItem? = null
//    private var vehicleModelItem: VehicleModelItem? = null

    interface OnItemClickListener {
        fun onItemClick(variantsItem: VehicleVariantsItem, vehicleModelItem: VehicleModelItem)
        fun onOnOff(modelItem: VehicleModelItem, flag: String)
        fun onOnOffVariants(vehicleVariantsItem: VehicleVariantsItem, flag: String)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutChooseModelVehicleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vehicleItem = list[position]
        holder.itemBinding.model.text = vehicleItem.name

        holder.itemBinding.header.text = vehicleItem.name?.substring(0, 1) ?: "0"

        // if not first item, check if item above has the same header
        if (position > 0 && list[position - 1].name?.substring(
                0,
                1
            ) ?: "0" == vehicleItem.name?.substring(0, 1) ?: "0"
        ) {
            holder.itemBinding.header.visibility = View.GONE
        } else {
            holder.itemBinding.header.visibility = View.VISIBLE
        }

        holder.itemBinding.arrowImage.setImageResource(R.drawable.ic_arrow_down)
        holder.itemBinding.arrowImage.setOnClickListener {
            if (holder.itemBinding.recyclerView.visibility != View.VISIBLE) {
                holder.itemBinding.recyclerView.visibility = View.VISIBLE
                holder.itemBinding.arrowImage.setImageResource(R.drawable.ic_arrow_up)
                Log.e("vehicleItem", vehicleItem.variant.toString())
                val vItem = vehicleItem.variant
                if (vItem != null) {
                    if (vItem.isNotEmpty()) {
                        val vAdapter =
                            ChooseVehicleModelVariantsAdapter(
                                context,
                                vItem
                            )
                        holder.itemBinding.recyclerView.apply {
                            setHasFixedSize(true)
                            adapter = vAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                        }
                        vAdapter.setOnItemClickListener(object :
                            ChooseVehicleModelVariantsAdapter.OnItemClickListener {
                            override fun onItemClick(
                                vehicleVariantsItem: VehicleVariantsItem,
                                boolean: Boolean
                            ) {

                                onItemClickListener.onItemClick(vehicleVariantsItem, vehicleItem)

                            }

                            override fun onOnOffVariants(
                                vehicleVariantsItem: VehicleVariantsItem,
                                flag: String
                            ) {
                                Log.e(
                                    "vehicleVariantsItem $flag",
                                    vehicleVariantsItem.toString()
                                )
                                onItemClickListener.onOnOffVariants(vehicleVariantsItem, flag)
                            }

                        })

                    }
                }
            } else {
                holder.itemBinding.arrowImage.setImageResource(R.drawable.ic_arrow_down)
                holder.itemBinding.recyclerView.visibility = View.GONE
            }
        }
        holder.itemBinding.model.isChecked = !vehicleItem.status.equals("0")


        holder.itemBinding.model.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onItemClickListener.onOnOff(vehicleItem, "1")
            } else {
                onItemClickListener.onOnOff(vehicleItem, "0")
            }
        }
    }

    class MyViewHolder(val itemBinding: LayoutChooseModelVehicleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<VehicleModelItem?>) {
        list = filterList as ArrayList<VehicleModelItem>
        notifyDataSetChanged()
    }
}