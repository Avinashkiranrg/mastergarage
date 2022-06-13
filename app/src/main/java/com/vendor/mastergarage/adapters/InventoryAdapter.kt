package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutInventoryChecklistBinding
import com.vendor.mastergarage.model.InventoryItem

class InventoryAdapter(
    private val context: Context,
    private var list: List<InventoryItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<InventoryAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemInventory(inventoryItem: InventoryItem, boolean: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutInventoryChecklistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val inventoryItem = list[position]
        holder.itemBinding.checkbox1.text = inventoryItem.name
        holder.itemBinding.checkbox1.buttonTintList =
            ContextCompat.getColorStateList(context, R.color.black)

        var count: Int = 1
        holder.itemBinding.counter.text = "$count"

        Log.e("Count", count.toString())

        if (count == 1) {
            holder.itemBinding.remove.isEnabled = false
        }

        holder.itemBinding.add.setOnClickListener {
            count += 1
            holder.itemBinding.remove.isEnabled = true
            holder.itemBinding.counter.text = "$count"
            inventoryItem.counter = count
        }
        holder.itemBinding.remove.setOnClickListener {
            if (count == 1) {
                holder.itemBinding.remove.isEnabled = false
            } else if (count > 1) {
                count -= 1
                holder.itemBinding.counter.text = "$count"
            }
            inventoryItem.counter = count
        }
        holder.itemBinding.checkbox1.setOnCheckedChangeListener { _, b ->
            if (b) {
                onItemClickListener.onItemInventory(inventoryItem, b)
                holder.itemBinding.checkbox1.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                inventoryItem.boolean = true
            } else {
                onItemClickListener.onItemInventory(inventoryItem, b)
                holder.itemBinding.checkbox1.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.black)
                inventoryItem.boolean = false
            }
        }


    }

    class MyViewHolder(val itemBinding: LayoutInventoryChecklistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<InventoryItem?>) {
        list = filterList as ArrayList<InventoryItem>
        notifyDataSetChanged()
    }
}