package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutBodyDamageBinding
import com.vendor.mastergarage.model.DamagedItem

class BodyDamagedAdapter(
    private val context: Context,
    private var list: List<DamagedItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<BodyDamagedAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemDamaged(bodyDamagedItem: DamagedItem, boolean: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutBodyDamageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bodyDamagedItem = list[position]
        holder.itemBinding.checkbox1.text = bodyDamagedItem.name
        holder.itemBinding.checkbox1.buttonTintList =
            ContextCompat.getColorStateList(context, R.color.black)

        holder.itemBinding.checkbox1.setOnCheckedChangeListener { _, b ->
            if (b) {
                onItemClickListener.onItemDamaged(bodyDamagedItem, b)
                holder.itemBinding.checkbox1.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
            } else {
                onItemClickListener.onItemDamaged(bodyDamagedItem, b)
                holder.itemBinding.checkbox1.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.black)
            }
        }
    }

    class MyViewHolder(val itemBinding: LayoutBodyDamageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<DamagedItem?>) {
        list = filterList as ArrayList<DamagedItem>
        notifyDataSetChanged()
    }
}