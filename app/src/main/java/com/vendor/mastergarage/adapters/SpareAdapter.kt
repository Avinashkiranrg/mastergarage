package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.LayoutSpareBinding
import com.vendor.mastergarage.model.SparesDetails

class SpareAdapter(
    private val context: Context,
    private var list: MutableList<SparesDetails>
) :
    RecyclerView.Adapter<SpareAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutSpareBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val serviceList = list[position]
        holder.itemBinding.title.text = "Item ${position + 1}"
        holder.itemBinding.imageView.setImageResource(R.drawable.ic_arrow_down)
        holder.itemBinding.constraintLayout11.setOnClickListener {
            if (holder.itemBinding.constraintLayout.visibility != View.VISIBLE) {
                holder.itemBinding.imageView.setImageResource(R.drawable.ic_arrow_up)
                holder.itemBinding.constraintLayout.visibility = View.VISIBLE
            } else {
                holder.itemBinding.imageView.setImageResource(R.drawable.ic_arrow_down)
                holder.itemBinding.constraintLayout.visibility = View.GONE
            }
        }
        holder.itemBinding.manufacturerName.setText("${serviceList.manufacturer_name}")
        holder.itemBinding.partNumber.setText("${serviceList.part_name}")
        holder.itemBinding.warranty.setText("${serviceList.warranty}")
        when (serviceList.part_type) {
            Constraints.OEM_PART -> {
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                holder.itemBinding.oemPart.isChecked = true
            }
            Constraints.OES_PART -> {
                holder.itemBinding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                holder.itemBinding.oesPart.isChecked = true
            }
            Constraints.OTHER_PART -> {
                holder.itemBinding.otherPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                holder.itemBinding.otherPart.isChecked = true
            }
        }

        holder.itemBinding.deleteBtn.setOnClickListener {
            if (!recyclerView.isComputingLayout && recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                list.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                notifyItemRangeChanged(holder.adapterPosition, list.size)
            }
        }



        holder.itemBinding.oesPart.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                serviceList.part_type = Constraints.OES_PART
                holder.itemBinding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                holder.itemBinding.oemPart.isChecked = false
                ContextCompat.getColorStateList(context, R.color.blue)
                holder.itemBinding.otherPart.isChecked = false
            } else {
                holder.itemBinding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

        holder.itemBinding.oemPart.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                serviceList.part_type = Constraints.OEM_PART
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                holder.itemBinding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                holder.itemBinding.oesPart.isChecked = false
                ContextCompat.getColorStateList(context, R.color.blue)
                holder.itemBinding.otherPart.isChecked = false
            } else {
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

        holder.itemBinding.otherPart.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                serviceList.part_type = Constraints.OTHER_PART
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                holder.itemBinding.oemPart.isChecked = false
                holder.itemBinding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                holder.itemBinding.oesPart.isChecked = false

            } else {
                holder.itemBinding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }
    }

    class MyViewHolder(val itemBinding: LayoutSpareBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<SparesDetails?>) {
        list = filterList as ArrayList<SparesDetails>
        notifyDataSetChanged()
    }

    fun addSparesList(filterList: List<SparesDetails?>) {
        list = filterList as ArrayList<SparesDetails>
        notifyDataSetChanged()
    }
}