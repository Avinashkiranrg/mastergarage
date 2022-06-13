package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutFaqsBinding
import com.vendor.mastergarage.model.FaqItem

class FaqAdapter(
    private val context: Context,
    private var list: MutableList<FaqItem>
) :
    RecyclerView.Adapter<FaqAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutFaqsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        holder.itemBinding.title.text = serviceList.title
        holder.itemBinding.body.text = serviceList.body

        holder.itemBinding.imageView.setImageResource(R.drawable.ic_arrow_down)
        holder.itemBinding.constraintLayout11.setOnClickListener {
            if (holder.itemBinding.body.visibility != View.VISIBLE) {
                holder.itemBinding.imageView.setImageResource(R.drawable.ic_arrow_up)
                holder.itemBinding.body.visibility = View.VISIBLE
            } else {
                holder.itemBinding.imageView.setImageResource(R.drawable.ic_arrow_down)
                holder.itemBinding.body.visibility = View.GONE
            }
        }
    }

    class MyViewHolder(val itemBinding: LayoutFaqsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<FaqItem?>) {
        list = filterList as ArrayList<FaqItem>
        notifyDataSetChanged()
    }
}