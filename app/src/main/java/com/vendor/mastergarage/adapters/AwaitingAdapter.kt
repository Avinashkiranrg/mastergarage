package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vendor.mastergarage.databinding.AwaitingAdapterItemBinding
import com.vendor.mastergarage.databinding.LayoutPendingBinding
import com.vendor.mastergarage.model.LeadsItem
import com.vendor.mastergarage.model.ResultOnGoing
import com.vendor.mastergarage.utlis.assetsToBitmapModel
import com.vendor.mastergarage.utlis.calculateMoney
import com.vendor.mastergarage.utlis.isBetweenValidTime
import java.text.SimpleDateFormat
import java.util.*


class AwaitingAdapter(
    private val context: Context,
    private var list: List<ResultOnGoing>,
    private val onItemClickListener: OnItemClickListener,
    private val acceptClicks : AcceptClicks,
    private val declineClicks : DeclineClicks
) :
    RecyclerView.Adapter<AwaitingAdapter.MyViewHolder>() {
    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    interface OnItemClickListener {
        fun onItemClick(leadItem: LeadsItem)
        fun onItemConfirm(leadItem: LeadsItem, currentDate: String, currentTime: String)
        fun onDecline(leadItem: LeadsItem)
    }

    interface AcceptClicks{

        fun onConfirmClick(resultOnGoing: ResultOnGoing,position: Int)

    }

    interface DeclineClicks{

        fun onDeclineClick(resultOnGoing: ResultOnGoing,position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            AwaitingAdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leadItem = list[position]
        holder.itemBinding.carName.text = "${leadItem.manufacturer_name}"
        holder.itemBinding.carFuelType.text = leadItem.fuelType

        holder.itemBinding.confirmBtn.setOnClickListener {

            acceptClicks.onConfirmClick(leadItem,position)

        }


        holder.itemBinding.declineBtn.setOnClickListener {

            declineClicks.onDeclineClick(leadItem,position)

        }

        /*   holder.itemBinding.amount.setText("₹ ${leadItem.paymentInfo?.let { calculateMoney(it) }}")
//        val p = "## ## ## ####"
//        holder.itemBinding.registrationNumber.text =
//            leadItem.registrationNo?.toFormattedString(p)

        holder.itemBinding.bKTime.text = "${leadItem.bookingDate} at ${leadItem.bookingTime}"
        holder.itemBinding.pKTime.text =
            "${leadItem.appointmentDate} at ${leadItem.appointmentTime}"

        val bitmap = leadItem.v_imageUri?.let { context.assetsToBitmapModel(it) }
        bitmap?.apply {
            holder.itemBinding.imageView.setImageBitmap(this)
        }
*/
//        if (leadItem.v_imageUri != null) {
//            try {
//                holder.itemBinding.imageView.imageFromUrl(leadItem.v_imageUri)
//            } catch (a: FileNotFoundException) {
//                Log.e("FileNotFoundException", "FileNotFoundException")
//            } catch (c: GlideException) {
//                Log.e("GlideException", "GlideException")
//            }
//        }

        /* val stringBuffer = StringBuffer("")
        if (leadItem.servicerequest?.isNullOrEmpty() == false) {
            if (leadItem.servicerequest.size <= 2) {
                leadItem.servicerequest.forEach {
                    if (leadItem.servicerequest.size < 2) {
                        stringBuffer.append("${it.package_name}, ")
                    }
                }
                try {
                    holder.itemBinding.serviceRequest.text =
                        "${stringBuffer.replace(stringBuffer.length - 2, stringBuffer.length, "")}"
                } catch (n: StringIndexOutOfBoundsException) {
                    holder.itemBinding.serviceRequest.text =
                        "${stringBuffer}"
                }
            } else {
                var r = 0
                leadItem.servicerequest.forEach {
                    if (r < 2) {
                        r++
                        stringBuffer.append("${it.package_name}, ")
                    }
                }
                val str = StringBuffer(
                    "${
                        stringBuffer.replace(
                            stringBuffer.length - 2,
                            stringBuffer.length,
                            ""
                        )
                    }"
                )
                str.append("...see more")
                holder.itemBinding.serviceRequest.text = "${str}"
            }

        } else {
            holder.itemBinding.serviceRequest.text = "----"
        }


        holder.itemBinding.root.setOnClickListener {
            onItemClickListener.onItemClick(leadItem)
        }

        val mills: Long = calculateMillis(leadItem)
        if (mills == 0L) {
            holder.itemBinding.declineBtn.visibility = View.GONE
            holder.itemBinding.confirmBtn.visibility = View.GONE
            holder.itemBinding.timeLeft.visibility = View.GONE
            holder.itemBinding.timeLeftHead.visibility = View.GONE
        } else {
            holder.itemBinding.declineBtn.visibility = View.VISIBLE
            holder.itemBinding.confirmBtn.visibility = View.VISIBLE
            holder.itemBinding.timeLeft.visibility = View.VISIBLE
            holder.itemBinding.timeLeftHead.visibility = View.VISIBLE
        }
*/
        /* object : CountDownTimer(mills, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24
                val time =
                    "${minutes % 60} min ${seconds % 60} sec"
                holder.itemBinding.timeLeft.text = time
            }

            override fun onFinish() {
                holder.itemBinding.declineBtn.visibility = View.GONE
                holder.itemBinding.confirmBtn.visibility = View.GONE
                holder.itemBinding.timeLeft.visibility = View.GONE
                holder.itemBinding.timeLeftHead.visibility = View.GONE
            }
        }.start()

        holder.itemBinding.declineBtn.setOnClickListener {
            leadItem.leadId?.let { onItemClickListener.onDecline(leadItem) }
        }

        holder.itemBinding.confirmBtn.setOnClickListener {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            holder.itemBinding.declineBtn.visibility = View.GONE
            holder.itemBinding.confirmBtn.visibility = View.GONE
            holder.itemBinding.timeLeft.visibility = View.GONE
            holder.itemBinding.timeLeftHead.visibility = View.GONE

            onItemClickListener.onItemConfirm(leadItem, currentDate, currentTime)
        }
*/
    }

    private fun calculateMillis(leadItem: LeadsItem): Long {

        val calendar: Calendar = Calendar.getInstance()
        val startTime: String = leadItem.bookingTime!!
        val startDate: String = leadItem.bookingDate!!
        val backupTime: Int = leadItem.backupTimer!!
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
        val dStart: Date = getDateData("$startDate $startTime")

        calendar.time = dStart
        calendar.add(Calendar.SECOND, backupTime)

        val newTime = df.format(calendar.time)
        val dEnd: Date = getDateData(newTime)

        val boolean = isBetweenValidTime(dStart, dEnd, Date())
        val milli = if (boolean) {
            dEnd.time - System.currentTimeMillis()
        } else {
            0L
        }
        return milli
    }

    private fun getDateData(toString: String): Date {
        val _myFormat = "yyyy-MM-dd hh:mm:ss a" // mention the format you need
        val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
        val date = _sdf.parse(toString)

        return date
    }

    class MyViewHolder(val itemBinding: AwaitingAdapterItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: List<ResultOnGoing?>) {
        list = filterList as ArrayList<ResultOnGoing>
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return if (list.size > 3)
            3
        else {
            list.size
        }

    }
}
