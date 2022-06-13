package com.vendor.mastergarage.utlis

import java.text.SimpleDateFormat
import java.util.*



fun getSelectedDate(toString: String): Date {
    val _myFormat = "yyyy-MM-dd" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    return date
}

fun getSelectedTime(toString: String): Date {
    val _myFormat = "hh:mm:ss" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    return date
}

fun getSelectedDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today: String = formatter.format(date)

    return today
}

fun getSelectedTime(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val today: String = formatter.format(date)

    return today
}

fun getFormattedDate(toString: String): String {
    val _myFormat = "yyyy-MM-dd" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)


    val cal = Calendar.getInstance();
    cal.setTime(date);
    //2nd of march 2015
    val day: Int = cal.get(Calendar.DATE);

//    return when (day % 10) {
//        1 ->
//            SimpleDateFormat("MMMM d'st', yyyy").format(date)
//        2 ->
//            SimpleDateFormat("MMMM d'nd', yyyy").format(date)
//        3 ->
//            SimpleDateFormat("MMMM d'rd', yyyy").format(date)
//        else ->
//            SimpleDateFormat("MMMM d'th', yyyy").format(date)
//    }
    return SimpleDateFormat("MMMM d", Locale.getDefault()).format(date)
}

fun getFormattedInToFullDate(toString: String): String {
    val _myFormat = "yyyy-MM-dd" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    val cal = Calendar.getInstance();
    cal.setTime(date);
    //2nd of march 2015
    val day: Int = cal.get(Calendar.DATE);

//    return when (day % 10) {
//        1 ->
//            SimpleDateFormat("MMMM d'st', yyyy").format(date)
//        2 ->
//            SimpleDateFormat("MMMM d'nd', yyyy").format(date)
//        3 ->
//            SimpleDateFormat("MMMM d'rd', yyyy").format(date)
//        else ->
//            SimpleDateFormat("MMMM d'th', yyyy").format(date)
//    }
    return SimpleDateFormat("d, MMM yyyy", Locale.getDefault()).format(date)
}

fun getFormattedInToFullDate2(toString: String): String {
    val _myFormat = "yyyy-MM-dd" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    val cal = Calendar.getInstance();
    cal.setTime(date);
    return SimpleDateFormat("EEEE ,d MMMM", Locale.getDefault()).format(date)
}

fun getFormattedTime(toString: String): String {
    val _myFormat = "hh:mm:ss" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    val cal = Calendar.getInstance();
    cal.setTime(date);
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
}

fun getFormattedInToFullDate3(toString: String): String {
    val _myFormat = "yyyy-MM-dd" // mention the format you need
    val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
    val date = _sdf.parse(toString)

    val cal = Calendar.getInstance();
    cal.setTime(date);
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
}