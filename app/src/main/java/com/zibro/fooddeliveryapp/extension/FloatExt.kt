package com.zibro.fooddeliveryapp.extension

import android.content.res.Resources

fun Float.fromDpToPx() : Int{
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}
