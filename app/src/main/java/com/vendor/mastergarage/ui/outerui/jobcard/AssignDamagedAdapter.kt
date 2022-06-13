package com.vendor.mastergarage.ui.outerui.jobcard

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vendor.mastergarage.R
import com.vendor.mastergarage.model.JobCardDamagedItem

class AssignDamagedAdapter(
    private val context: Activity,
    private val list: MutableList<JobCardDamagedItem>,
) : ArrayAdapter<JobCardDamagedItem>(context, R.layout.layout_damaged_assign, list) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.layout_damaged_assign, null, true)

        val titleText = rowView.findViewById(R.id.text) as TextView
        rowView.minimumHeight = parent.height / count;
        titleText.text = list[position].name

        return rowView
    }

    override fun getCount(): Int {
        return list.size
    }
}