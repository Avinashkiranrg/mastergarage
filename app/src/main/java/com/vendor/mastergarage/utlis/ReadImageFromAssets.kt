package com.vendor.mastergarage.utlis

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.io.IOException
import java.io.InputStream


fun Context.readImageFromAssets(context: Context, fileName: String): Bitmap? {
    var d: Bitmap? = null
    val am = context.resources.assets
    try {
        val inn: InputStream = am.open(fileName)
        //set drawable from stream
        d = BitmapFactory.decodeStream(inn)
        inn.close()
    } catch (e: IOException) {

    }
    return d
}

// extension function to get bitmap from assets
fun Context.assetsToBitmapBrands(fileName: String): Bitmap? {
    return try {
        val prefix = "brands/$fileName"
        with(assets.open(prefix)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        null
    }
}
fun Context.assetsToBitmapModel(fileName: String): Bitmap? {
    return try {
        val prefix = "model/$fileName"
        with(assets.open(prefix)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        null
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}