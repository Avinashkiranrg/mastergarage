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
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.bumptech.glide.load.engine.GlideException
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.LayoutChooseAdvisorBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.ServiceAdvisorItem
import com.vendor.mastergarage.utlis.imageFromUrl
import java.io.FileNotFoundException

class ChooseServiceAdvisorAdapter(
    private val context: Context,
    private var list: MutableList<ServiceAdvisorItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChooseServiceAdvisorAdapter.MyViewHolder>() {
    private var selectedPosition = -1
    private var notifyBtn = 1
    lateinit var recyclerView: RecyclerView

    interface OnItemClickListener {
        fun onItemDeleteClick(serviceAdvisorItem: ServiceAdvisorItem)
        fun onItemEditClick(position: Int, serviceAdvisorItem: ServiceAdvisorItem)
        fun onSelect(serviceAdvisorItem: ServiceAdvisorItem, flag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutChooseAdvisorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val serviceList = list[holder.adapterPosition]
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
            onItemClickListener.onSelect(serviceList, "1")
        }

        val outletsItems =
            ModelPreferencesManager.get<ServiceAdvisorItem>(Constraints.ADVISOR_STORE)
        if (outletsItems != null) {
            if (serviceList.advisorId == outletsItems.advisorId) {
                holder.itemBinding.checkbox1.isChecked = true
                holder.itemBinding.checkbox1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue2
                    )
                )
                selectedPosition = holder.adapterPosition
            } else if (outletsItems.advisorId == -1) {
                if (outletsItems.mobileNo.equals(serviceList.mobileNo)) {
                    holder.itemBinding.checkbox1.isChecked = true
                    holder.itemBinding.checkbox1.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.blue2
                        )
                    )
                    selectedPosition = holder.adapterPosition
                    ModelPreferencesManager.put(serviceList, Constraints.ADVISOR_STORE)
                }
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
                if (!recyclerView.isComputingLayout && recyclerView.scrollState == SCROLL_STATE_IDLE) {
                    holder.itemBinding.checkbox1.isChecked = true
                    selectedPosition = holder.adapterPosition
                    ModelPreferencesManager.put(serviceList, Constraints.ADVISOR_STORE)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            } else {
                holder.itemBinding.checkbox1.isChecked = false
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

    class MyViewHolder(val itemBinding: LayoutChooseAdvisorBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ServiceAdvisorItem?>) {
        list = filterList as ArrayList<ServiceAdvisorItem>
        notifyDataSetChanged()
    }

    fun setNotifyBtn(notify: Int) {
        notifyBtn = notify as Int
        notifyDataSetChanged()
    }
}