package com.vendor.mastergarage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.GlideException
import com.vendor.mastergarage.databinding.LayoutHorizontalImageBinding
import com.vendor.mastergarage.model.ImageModel
import com.vendor.mastergarage.utlis.imageFromUrl
import java.io.FileNotFoundException


class HorizontalImageAdapter(
    private val context: Context,
    private var list: List<ImageModel>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<HorizontalImageAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(imageModel: ImageModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            LayoutHorizontalImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.root.post {
            itemBinding.root.layoutParams.height = parent.width / 2
            itemBinding.root.requestLayout()
        }
        return MyViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageModel = list[position]


        try {
            imageModel.imageUri?.let { holder.itemBinding.imageView.imageFromUrl(it) }
        } catch (a: FileNotFoundException) {
        } catch (c: GlideException) {
        }

        holder.itemBinding.imageView.setOnClickListener {
            onItemClickListener.onItemClick(imageModel)
        }
    }

    class MyViewHolder(val itemBinding: LayoutHorizontalImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemCount(): Int = list.size
}