package com.vendor.mastergarage.utlis

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class EndDatePickerFragment : DialogFragment() {

    override fun onCreateDialog(bundle: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year: Int
        val month: Int
        val day: Int
        if (arguments != null) {
//            var bundle = Bundle()
//            bundle = savedInstanceState
            val getfromdate: String? = requireArguments().getString("start_date")
            val getfrom = getfromdate?.split("-".toRegex())?.toTypedArray()
            Log.e("date", getfromdate.toString())

            year = getfrom?.get(2)!!.toInt()
            month = getfrom[1].toInt()
            day = getfrom[0].toInt()

            val dStart: Date = getDateFormat("$getfromdate")
            c.time = dStart
        } else {
            Log.e("date", "No date selected")

            year = c[Calendar.YEAR]
            month = c[Calendar.MONTH]
            day = c[Calendar.DAY_OF_MONTH]
        }
        val dialog =
            DatePickerDialog(
                requireActivity(),
                parentFragment as OnDateSetListener?,
                year,
                month,
                day
            )

        dialog.datePicker.minDate = c.timeInMillis
        return dialog
    }

    fun getDateFormat(toString: String): Date {
        val _myFormat = "yyyy-MM-dd" // mention the format you need
        val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
        val date = _sdf.parse(toString)

        return date
    }
}