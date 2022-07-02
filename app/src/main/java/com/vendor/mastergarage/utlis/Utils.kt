package com.vendor.mastergarage.utlis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.vendor.mastergarage.model.LeadsItem
import java.text.SimpleDateFormat
import java.util.*

class NetworkUtil {
    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
                return this.getNetworkCapabilities(this.activeNetwork)?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) ?: false
            }
        }
    }
}

fun ImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .target(this)
        .transformations(CircleCropTransformation())
        .scale(Scale.FILL)
        .build()

    imageLoader.enqueue(request)
}

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url: String) {
    Glide.with(this.context).load(url).into(this)

}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enable: Boolean) {
    isEnabled = enable
    alpha = if (enable) 1f else 0.5f
}

fun String.toFormattedString(format: String): String {
    // get the Long as String in order to be able to iterate it
    val s = this.toString()
    // provide a variable for the index
    var i = 0
    // create an empty result String
    var sb: String = ""

    // go through the pattern String
    for (c in format) {
        // replace # with the current cipher
        if (c == '#') {
            sb += s[i]
            // increment the counter
            i++
        } else {
            // for every other char in the pattern, just add that char
            sb += c
        }
    }

    return sb
}

fun isBetweenValidTime(startTime: Date?, endTime: Date, validateTime: Date): Boolean {
    var validTimeFlag = false
    if (endTime <= startTime) {
        if (validateTime < endTime || validateTime >= startTime) {
            validTimeFlag = true
        }
    } else if (validateTime < endTime && validateTime >= startTime) {
        validTimeFlag = true
    }
    return validTimeFlag
}
fun isBetweenValidTimeCombined(startTime: Date?, endTime: Date, validateTime: Date): Boolean {
    var validTimeFlag = false
    if (endTime <= startTime) {
        if (validateTime < endTime || validateTime > startTime) {
            validTimeFlag = true
        }
    } else if (validateTime < endTime && validateTime > startTime) {
        validTimeFlag = true
    }
    return validTimeFlag
}

fun calculateMillis(leadItem: LeadsItem): Long {

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

fun getDateData(toString: String): Date {
    val _myFormat = "yyyy-MM-dd hh:mm:ss a" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    return date
}

fun Context.goToActivityFinish(activity: Activity, javaClass: Class<*>?) {
    val intent = Intent(activity, javaClass)
    startActivity(intent)
    activity.finish()
}

fun Context.goToActivitiesFinish(activity: Activity, javaClass: Class<*>?) {
    val intent = Intent(activity, javaClass)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    activity.finish()
}

fun getOnlyDate(toString: String): Date {
    val _myFormat = "dd/MM/yyyy" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    return date
}

fun Context.goToActivity(activity: Activity, javaClass: Class<*>?) {
    val intent = Intent(activity, javaClass)
    startActivity(intent)
}