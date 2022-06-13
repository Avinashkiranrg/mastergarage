package com.vendor.mastergarage.ui.outerui.bookingviewpager

import android.os.Handler
import com.google.android.material.textview.MaterialTextView
import com.vendor.mastergarage.databinding.LayoutPendingBinding


class CountdownRunnable(handler: Handler, holder: MaterialTextView, millisUntilFinished: Long) :
    Runnable {

    var millisUntilFinished: Long = 40000
    var holder: MaterialTextView? = null
    var handler: Handler? = null

    init {
        this.millisUntilFinished = millisUntilFinished
        this.holder = holder
        this.handler = handler
    }

    override fun run() {
        /* do what you need to do */
        /* do what you need to do */
        val seconds: Long = millisUntilFinished / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val time =
            "${minutes % 60} min : ${seconds % 60} sec"
//            days.toString() + " " + "days" + " :" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60
        holder?.text = time

        millisUntilFinished -= 1000

        /* and here comes the "trick" */
        /* and here comes the "trick" */
        handler?.postDelayed(this, 1000)

    }
}