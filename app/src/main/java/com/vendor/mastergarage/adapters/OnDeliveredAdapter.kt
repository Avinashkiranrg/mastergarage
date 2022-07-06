package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.LayoutDeliveredBinding
import com.vendor.mastergarage.model.OnDeliveredItem
import com.vendor.mastergarage.model.ResultOnGoing
import com.vendor.mastergarage.utlis.assetsToBitmapModel
import com.vendor.mastergarage.utlis.calculateMoney


class OnDeliveredAdapter(
    private val context: Context,
    private var list: List<ResultOnGoing>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<OnDeliveredAdapter.MyViewHolder>() {
    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    interface OnItemClickListener {
        fun onItemClick(onDeliveredItem: OnDeliveredItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutDeliveredBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leadItem = list[position]
        holder.itemBinding.carName.text = "${leadItem.manufacturer_name}"
        holder.itemBinding.carFuelType.text = leadItem.fuelType

        holder.itemBinding.amount.setText("₹ ${leadItem.totalCost}")

//        val p = "## ## ## ####"
//        holder.itemBinding.registrationNumber.text =
//            leadItem.registrationNo?.toFormattedString(p)

//        holder.itemBinding.deliveryTime.text =
//            "${leadItem.boo} at ${leadItem.deliveryTime}"
//        holder.itemBinding.pKTime.text =
//            "${leadItem.pickupDate} at ${leadItem.pickupTime}"

        val bitmap = leadItem.v_imageUri?.let { context.assetsToBitmapModel(it) }
        bitmap?.apply {
            holder.itemBinding.imageView.setImageBitmap(this)
        }

//        if (leadItem.vImageUri != null) {
//            try {
//                holder.itemBinding.imageView.imageFromUrl(leadItem.vImageUri)
//            } catch (a: FileNotFoundException) {
//                Log.e("FileNotFoundException", "FileNotFoundException")
//            } catch (c: GlideException) {
//                Log.e("GlideException", "GlideException")
//            }
//        }
     /*   holder.itemBinding.root.setOnClickListener {
            onItemClickListener.onItemClick(leadItem)
        }

        if (leadItem.rate != null)
            holder.itemBinding.ratingBar.rating = leadItem.rate*/

    }

    class MyViewHolder(val itemBinding: LayoutDeliveredBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size
}