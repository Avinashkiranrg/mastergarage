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
import com.vendor.mastergarage.constraints.Constraints.Companion.OEM_PART
import com.vendor.mastergarage.constraints.Constraints.Companion.OES_PART
import com.vendor.mastergarage.constraints.Constraints.Companion.OTHER_PART
import com.vendor.mastergarage.databinding.LayoutServiceRecommSpareBinding
import com.vendor.mastergarage.model.SparesDetails
import com.vendor.mastergarage.utlis.enable

class Spare2Adapter(
    private val context: Context,
    private var list: MutableList<SparesDetails>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<Spare2Adapter.MyViewHolder>() {
    private var itemPosition: Int = -1

    interface OnItemClickListener {
        fun onDelete(sparesDetails: SparesDetails, position: Int)
        fun onSave(
            sparesId: Int,
            manufacturer_name: String,
            part_name: String,
            warranty: String,
            year: Int,
            part_type: Int,
            position: Int
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutServiceRecommSpareBinding.inflate(
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
        holder.itemBinding.manufacturerName.setText("${serviceList.manufacturer_name}")
        if (!serviceList.part_name.equals("0")) {
            holder.itemBinding.partNumber.setText("${serviceList.part_name}")
        }
        holder.itemBinding.warranty.setText("${serviceList.warranty}")
        holder.itemBinding.year.setText("${serviceList.year}")

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

        holder.itemBinding.delete.setOnClickListener {
            onItemClickListener.onDelete(serviceList, position)
        }
        holder.itemBinding.manufacturerName.enable(false)
        holder.itemBinding.update.setOnClickListener {
            if (!holder.itemBinding.manufacturerName.isEnabled) {
                holder.itemBinding.update.visibility = View.GONE
                holder.itemBinding.delete.visibility = View.GONE
                holder.itemBinding.save.visibility = View.VISIBLE
                holder.itemBinding.manufacturerName.enable(true)
                holder.itemBinding.partNumber.enable(true)
                holder.itemBinding.warranty.enable(true)
                holder.itemBinding.year.enable(true)
            } else {
                holder.itemBinding.manufacturerName.enable(false)
                holder.itemBinding.partNumber.enable(false)
                holder.itemBinding.warranty.enable(false)
                holder.itemBinding.year.enable(false)
                holder.itemBinding.update.visibility = View.VISIBLE
                holder.itemBinding.delete.visibility = View.VISIBLE
                holder.itemBinding.save.visibility = View.GONE
            }
        }

        holder.itemBinding.save.setOnClickListener {

            var _year: Int = 0
            _year = try {
                holder.itemBinding.year.text.toString().toInt()
            } catch (n: NumberFormatException) {
                0
            }
            var part_type: Int = 0
            if (holder.itemBinding.oemPart.isChecked) {
                part_type = OEM_PART
            } else if (holder.itemBinding.oesPart.isChecked) {
                part_type = OES_PART
            } else {
                part_type = OTHER_PART
            }
            serviceList.spareId?.let { it1 ->
                onItemClickListener.onSave(
                    sparesId = it1,
                    manufacturer_name = holder.itemBinding.manufacturerName.text.toString().trim(),
                    part_name = holder.itemBinding.partNumber.text.toString().trim(),
                    warranty = holder.itemBinding.warranty.text.toString().trim(),
                    year = _year,
                    part_type = part_type,
                    position
                )
            }
        }

        holder.itemBinding.oesPart.setOnCheckedChangeListener { _, b ->
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

    class MyViewHolder(val itemBinding: LayoutServiceRecommSpareBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(itemPosition: Int) {
        this.itemPosition = itemPosition
        notifyDataSetChanged()
    }

    fun addSparesList(filterList: List<SparesDetails?>) {
        list = filterList as ArrayList<SparesDetails>
        notifyDataSetChanged()
    }
}