package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.GlideException
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.LayoutChooseDriverBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.DriversItem
import com.vendor.mastergarage.utlis.imageFromUrl
import java.io.FileNotFoundException

class PickUpDriverAdapter(
    private val context: Context,
    private var list: MutableList<DriversItem>,
    private val onItemClickListener: OnItemClickListener

) :
    RecyclerView.Adapter<PickUpDriverAdapter.MyViewHolder>() {
    private var selectedPosition = -1
    lateinit var recyclerView: RecyclerView
    private var notifyBtn = 1

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView

    }

    interface OnItemClickListener {
        fun onItemDeleteClick(driversItem: DriversItem)
        fun onItemEditClick(position: Int, driversItem: DriversItem)
        fun onSelect(driversItem: DriversItem, flag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutChooseDriverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        holder.itemBinding.advisorName.text = "${serviceList.firstName} ${serviceList.lastName}"

        if (serviceList.imageUri != null) {
            try {
                holder.itemBinding.imageView.imageFromUrl(serviceList.imageUri)
            } catch (a: FileNotFoundException) {
                Log.e("FileNotFoundException", "FileNotFoundException")
            } catch (c: GlideException) {
                Log.e("GlideException", "GlideException")
            }
        }

        if (notifyBtn == 1) {
            holder.itemBinding.update.visibility = View.GONE
            holder.itemBinding.delete.visibility = View.GONE
            holder.itemBinding.select.visibility = View.VISIBLE
            holder.itemBinding.imageView.visibility = View.VISIBLE
            holder.itemBinding.checkbox1.visibility = View.VISIBLE

        } else {
            holder.itemBinding.update.visibility = View.VISIBLE
            holder.itemBinding.delete.visibility = View.VISIBLE
            holder.itemBinding.select.visibility = View.GONE
            holder.itemBinding.imageView.visibility = View.GONE
            holder.itemBinding.checkbox1.visibility = View.INVISIBLE

        }

        holder.itemBinding.select.setOnClickListener {
            ModelPreferencesManager.put(serviceList, Constraints.DRIVER_STORE)
            onItemClickListener.onSelect(serviceList, "1")
        }

        val outletsItems =
            ModelPreferencesManager.get<DriversItem>(Constraints.DRIVER_STORE)
        if (outletsItems != null) {
            if (serviceList.driverId == outletsItems.driverId) {
                holder.itemBinding.checkbox1.isChecked = true
                holder.itemBinding.checkbox1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue2
                    )
                )
                selectedPosition = holder.adapterPosition
            }
        }
        if (selectedPosition == holder.adapterPosition) {
            holder.itemBinding.checkbox1.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue2
                )
            )
            holder.itemBinding.checkbox1.isChecked = true

            holder.itemBinding.checkbox1.buttonTintList =
                ColorStateList.valueOf(context.getColor(R.color.blue2))
        } else {
            holder.itemBinding.checkbox1.isChecked = false
            holder.itemBinding.checkbox1.setTextColor(ContextCompat.getColor(context, R.color.blue))
            holder.itemBinding.checkbox1.buttonTintList =
                ColorStateList.valueOf(context.getColor(R.color.blue))
        }


        holder.itemBinding.checkbox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!recyclerView.isComputingLayout && recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    selectedPosition = holder.adapterPosition
                    ModelPreferencesManager.put(serviceList, Constraints.DRIVER_STORE)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
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
        }



        holder.itemBinding.delete.setOnClickListener {
            onItemClickListener.onItemDeleteClick(serviceList)
        }

        holder.itemBinding.update.setOnClickListener {
            onItemClickListener.onItemEditClick(position, serviceList)
        }

    }

    class MyViewHolder(val itemBinding: LayoutChooseDriverBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<DriversItem?>) {
        list = filterList as ArrayList<DriversItem>
        notifyDataSetChanged()
    }

    fun setNotifyBtn(notify: Int) {
        notifyBtn = notify as Int
        notifyDataSetChanged()
    }

}