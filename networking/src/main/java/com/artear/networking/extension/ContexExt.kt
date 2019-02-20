package com.artear.networking.extension

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

fun Context.connectivityManager(): ConnectivityManager? {
    return getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
}

fun Context.telephonyManager(): TelephonyManager? =
        getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
