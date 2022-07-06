package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.LayoutOngoingBinding
import com.vendor.mastergarage.model.OnGoingDataItem
import com.vendor.mastergarage.model.ResultOnGoing
import com.vendor.mastergarage.utlis.assetsToBitmapModel
import com.vendor.mastergarage.utlis.calculateMoney


class OnGoingAdapter(
    private val context: Context,
    private var list: List<ResultOnGoing>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<OnGoingAdapter.MyViewHolder>() {
    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    interface OnItemClickListener {
        fun onItemClick(onGoingDataItem: OnGoingDataItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutOngoingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leadItem = list[position]
        holder.itemBinding.carName.text = "${leadItem.manufacturer_name}"
        holder.itemBinding.carFuelType.text = leadItem.fuelType

        holder.itemBinding.estimatedTotal.setText("₹ ${leadItem.totalCost}")

//        val p = "## ## ## ####"
//        holder.itemBinding.registrationNumber.text =
//            leadItem.registrationNo?.toFormattedString(p)

        holder.itemBinding.bKTime.text = "${leadItem.booking_date} at ${leadItem.booking_time}"
        holder.itemBinding.pKTime.text =
            "${leadItem.appointment_date} at ${leadItem.appointment_time}"

       // holder.itemBinding.lastUpdateDate.text = "Last updated on ${leadItem.lastUpDate}"
       // holder.itemBinding.status.text = "${leadItem.update_remarks}"

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
*/
    }

    class MyViewHolder(val itemBinding: LayoutOngoingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size
}