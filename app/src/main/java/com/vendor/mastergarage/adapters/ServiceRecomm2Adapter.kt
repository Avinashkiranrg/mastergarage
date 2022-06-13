package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.LayoutServiceRecommendationsBinding
import com.vendor.mastergarage.model.ServiceDetails

class ServiceRecomm2Adapter(
    private val context: Context,
    private var list: MutableList<ServiceDetails>,
    private val onItemClickListener: OnItemClickListener

) :
    RecyclerView.Adapter<ServiceRecomm2Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutServiceRecommendationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    interface OnItemClickListener {
        fun onSelect(serviceDetails: ServiceDetails)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val serviceList = list[position]
        holder.itemBinding.serviceNo.text = "Service ${position + 1}"
        holder.itemBinding.ServiceName.text = "${serviceList.name}"
//        val cost= serviceList.cost
//        val cost_other= serviceList.other_charges
//        val total= cost+ cost_other
        holder.itemBinding.cost.text = "₹ ${serviceList.cost}"


        if (serviceList.approval_status == 1) {
            holder.itemBinding.approval.text = "Approved"
        } else {
            holder.itemBinding.approval.text = "Approval Pending"
        }
        try {
            holder.itemBinding.manufacturerName.text = "${serviceList.spares[0].manufacturer_name}"
            if (!serviceList.spares[0].part_name.equals("0"))
                holder.itemBinding.partNumber.text = "${serviceList.spares[0].part_name}"
            if (serviceList.spares[0].year != 0) {
                holder.itemBinding.warranty.text = "${serviceList.spares[0].warranty} year Warranty"
            }
            if (serviceList.spares[0].part_name?.isEmpty() == false) {
                if (serviceList.spares[0].warranty?.isEmpty() == false) {
                    holder.itemBinding.warranty.text =
                        "${serviceList.spares[0].part_name}, ${serviceList.spares[0].warranty}"
                } else {
                    holder.itemBinding.warranty.text = "${serviceList.spares[0].part_name}"
                }
            }



            when (serviceList.spares[0].part_type) {
                Constraints.OEM_PART -> {
                    holder.itemBinding.osmAndOem.text = "OEM Parts"
                }
                Constraints.OES_PART -> {
                    holder.itemBinding.osmAndOem.text = "OES Parts"
                }
                else -> {
                    holder.itemBinding.osmAndOem.text = "Other Parts"
                }
            }

        } catch (n: IndexOutOfBoundsException) {

        }

        holder.itemBinding.root.setOnLongClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.layout_copy)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val imgCloseDlg = dialog.findViewById(R.id.deleteBtn) as ImageView
            imgCloseDlg.setOnClickListener {
                onItemClickListener.onSelect(serviceList)
                dialog.dismiss()
            }
            dialog.show()
            false
        }

    }

    class MyViewHolder(val itemBinding: LayoutServiceRecommendationsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServiceDetails?>) {
        list = filterList as ArrayList<ServiceDetails>
        notifyDataSetChanged()
    }

}