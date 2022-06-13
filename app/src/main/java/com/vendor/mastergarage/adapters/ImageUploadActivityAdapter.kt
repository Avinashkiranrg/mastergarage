package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.GlideException
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.LayoutImageUploadBinding
import com.vendor.mastergarage.model.ImageModel
import com.vendor.mastergarage.ui.outerui.jobcard.ImageUploadActivity
import com.vendor.mastergarage.ui.outerui.jobcard.ImageUploadActivity.Companion.LAST_POS
import com.vendor.mastergarage.utlis.imageFromUrl
import java.io.FileNotFoundException


class ImageUploadActivityAdapter(
    private val context: Context,
    private var list: ArrayList<ImageModel>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ImageUploadActivityAdapter.MyViewHolder>() {
    private var notifyBtn: Int = -1
    private var max: Int = 11


    interface OnItemClickListener {
        fun onItemClick(imageModel: ImageModel, max: Int)
        fun onItemDeleteClick(imageModel: ImageModel)

    }

    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutImageUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.root.post {
            itemBinding.root.layoutParams.height = parent.width / 2
            itemBinding.root.requestLayout()
        }
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageModel = list[position]


        if (imageModel.position == LAST_POS) {
            holder.itemBinding.layout1.visibility = View.VISIBLE
            holder.itemBinding.layout2.visibility = View.GONE
//            Log.e("imageModel.uri", imageModel.uri)
            Log.e("imageModel.booleanboolean", imageModel.position.toString())
        } else {
            holder.itemBinding.layout1.visibility = View.GONE
            holder.itemBinding.layout2.visibility = View.VISIBLE

            imageModel.imageUri?.let { Log.e("imageModel.uri", it) }
            Log.e("imageModel.position", imageModel.position.toString())
            try {
                imageModel.imageUri?.let { holder.itemBinding.imageView.imageFromUrl(it) }
            } catch (a: FileNotFoundException) {
            } catch (c: GlideException) {
            }
        }

        holder.itemBinding.layout2.setOnLongClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.layout_delete)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val imgCloseDlg = dialog.findViewById(R.id.deleteBtn) as ImageView
            imgCloseDlg.setOnClickListener {
                if (!recyclerView.isComputingLayout && recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (imageModel.position == ImageUploadActivity.KIDDLE_POS) {
                        onItemClickListener.onItemDeleteClick(imageModel)
                    }
                    list.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, list.size)
                }

                dialog.dismiss()
            }
            dialog.show()
            false
        }


        holder.itemBinding.addImage.setOnClickListener {
            max = 11 - list.size
            onItemClickListener.onItemClick(imageModel, max)
        }
    }

    class MyViewHolder(val itemBinding: LayoutImageUploadBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    fun setFilter(filterList: ArrayList<ImageModel>) {
        val arrayList = ArrayList<ImageModel>(list)
        list.clear()
        list.addAll(arrayList)
        list = filterList as ArrayList<ImageModel>
        list.sortBy { it.position }
//        list.reverse()
//        if (list[0].boolean) {
//            list.reverse()
//        }
        notifyDataSetChanged()
    }

    fun setNotifyBtn(notify: Int) {
        notifyBtn = notify
        notifyDataSetChanged()
    }

    fun showDlgFurtherDetails(context: Context, position: Int, arrayList: ArrayList<ImageModel>) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imgCloseDlg = dialog.findViewById(R.id.deleteBtn) as ImageView
        imgCloseDlg.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}