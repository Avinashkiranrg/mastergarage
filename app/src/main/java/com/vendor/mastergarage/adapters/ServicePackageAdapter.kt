package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutScheduledServicePackageBinding
import com.vendor.mastergarage.model.ServicePackageProvideItem

class ServicePackageAdapter(
    private val context: Context,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ServicePackageAdapter.MyViewHolder>() {
    private var list: List<ServicePackageProvideItem> = mutableListOf<ServicePackageProvideItem>()
    private var notifyBtn = -1

    interface OnItemClickListener {
        //        fun onItemClick(serviceListItem: ServicePackageProvideItem)
        fun onOnOff(serviceListItem: ServicePackageProvideItem, flag: String)
        fun onButtonClick()
    }

    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutScheduledServicePackageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[position]
        if (!serviceList.packageNotation.isNullOrEmpty()) {
            holder.itemBinding.tag.text = serviceList.packageNotation
            holder.itemBinding.tag.visibility = View.VISIBLE
        } else {
            holder.itemBinding.tag.visibility = View.INVISIBLE
        }

        holder.itemBinding.schedueText.text = serviceList.packageType

        // if not first item, check if item above has the same header
        if (position > 0 && list[position - 1].packageType == serviceList.packageType) {
            holder.itemBinding.schedueText.visibility = View.GONE
        } else {
            holder.itemBinding.schedueText.visibility = View.VISIBLE
        }


        if (notifyBtn == 1) {
            val typeface = ResourcesCompat.getFont(context, R.font.opensans_bold)
            holder.itemBinding.selectCarBtn.typeface = typeface
            holder.itemBinding.selectCarBtn.text = "₹ ${serviceList.costs}"
        } else {
            val typeface = ResourcesCompat.getFont(context, R.font.poppins_regular)
            holder.itemBinding.selectCarBtn.typeface = typeface
            holder.itemBinding.selectCarBtn.text = "Select Car"
            holder.itemBinding.selectCarBtn.setOnClickListener {
                onItemClickListener.onButtonClick()
            }
        }
        holder.itemBinding.switch1.text = serviceList.packageName

        val des = "${serviceList.monthsOrKms}"
        var lastIndex = des.indexOf(",")
        if (lastIndex == -1) {
            lastIndex = des.length
        }
        holder.itemBinding.materialTextView7.text = "${des.substring(0, lastIndex)}"
        holder.itemBinding.materialTextView8.text = "${serviceList.points}"

//        holder.itemBinding.materialTextView7.text = serviceList.monthsOrKms
//        holder.itemBinding.materialTextView8.text = "${serviceList.points} Points Service"

        holder.itemBinding.switch1.isChecked = !serviceList.status.equals("0")

        holder.itemBinding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onItemClickListener.onOnOff(serviceList, "1")
            } else {
                onItemClickListener.onOnOff(serviceList, "0")
            }
        }

    }

    class MyViewHolder(val itemBinding: LayoutScheduledServicePackageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServicePackageProvideItem?>) {
        list = filterList as ArrayList<ServicePackageProvideItem>
        notifyDataSetChanged()
    }

    fun setNotifyBtn(notify: Int) {
        notifyBtn = notify as Int
        notifyDataSetChanged()
    }
}