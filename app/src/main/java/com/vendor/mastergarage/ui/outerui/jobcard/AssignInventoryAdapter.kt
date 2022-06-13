package com.vendor.mastergarage.ui.outerui.jobcard

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.model.JobCardInventoryItem

class AssignInventoryAdapter(
    private val context: Activity,
    private val list: MutableList<JobCardInventoryItem>,
) : ArrayAdapter<JobCardInventoryItem>(context, R.layout.layout_inventory_checklist_show, list) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.layout_inventory_checklist_show, null, true)

        val titleText = rowView.findViewById(R.id.checkListName) as TextView
        val noOfCount = rowView.findViewById(R.id.noOfCount) as TextView
        rowView.minimumHeight = parent.height / count;
        titleText.text = list[position].name
        noOfCount.text = "${list[position].noOfInventory} Nos."


        return rowView
    }

    override fun getCount(): Int {
        return list.size
    }
}