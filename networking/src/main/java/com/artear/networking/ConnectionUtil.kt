/*
 * Copyright 2018 Artear S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artear.networking

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.net.NetworkInfo
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.M
import android.telephony.TelephonyManager.*
import java.util.*

object ConnectionUtil {

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return is Device Connected
     */
    fun isConnected(context: Context): Boolean {
        context.connectivityManager()?.apply {
            val activeNetwork = activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
        return false
    }

    fun connectionType(context: Context): ConnectionType {
        context.connectivityManager()?.apply {
            return isMarshMallow({
                connectionTypeMarshMallow(it, context)
            }, {
                connectionTypePreviousM(context)
            })
        }
        return ConnectionType.UNKNOWN
    }

    @TargetApi(M)
    private fun connectionTypeMarshMallow(connectivityManager: ConnectivityManager,
                                          context: Context): ConnectionType {
        connectivityManager.activeNetwork?.apply {
            val nc = connectivityManager.getNetworkCapabilities(this)
            val transports = setOf(TRANSPORT_WIFI, TRANSPORT_VPN, TRANSPORT_CELLULAR)
            val transport = transports.first { nc.hasTransport(it) }
            when (transport) {
                TRANSPORT_WIFI, TRANSPORT_VPN -> return ConnectionType.WIFI
                TRANSPORT_CELLULAR -> return connectionMobileType(context)
            }
        }
        return ConnectionType.UNKNOWN
    }

    private fun ConnectivityManager.connectionTypePreviousM(context: Context): ConnectionType {
        return isLollipop({ connectionTypeLollipop(context) }, { connectionTypePreviousL(context) })
    }

    @TargetApi(LOLLIPOP)
    private fun ConnectivityManager.connectionTypeLollipop(context: Context): ConnectionType {
        var connectionType = ConnectionType.UNKNOWN
        val networks = Arrays.asList(*allNetworks)
        for (network in networks) {
            val networkInfo = getNetworkInfo(network)
            if (networkInfo.isWifi())
                connectionType = ConnectionType.WIFI
            else if (networkInfo.isMobile()) {
                connectionType = connectionMobileType(context)
            }
        }
        return connectionType
    }

    /**
     * Just in old version for check connection
     */
    @Suppress("deprecation")
    private fun ConnectivityManager.connectionTypePreviousL(context: Context): ConnectionType {
        var networkInfo = getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (networkInfo.isConnectedOrConnecting)
            return ConnectionType.WIFI
        else {
            networkInfo = getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (networkInfo.isConnectedOrConnecting)
                return connectionMobileType(context)
        }
        return ConnectionType.UNKNOWN
    }

    private fun connectionMobileType(context: Context): ConnectionType {
        context.telephonyManager()?.apply {
            when (networkType) {
                NETWORK_TYPE_GPRS,
                NETWORK_TYPE_EDGE,
                NETWORK_TYPE_CDMA,
                NETWORK_TYPE_1xRTT,
                NETWORK_TYPE_IDEN -> {
                    return ConnectionType._2G
                }

                NETWORK_TYPE_UMTS,
                NETWORK_TYPE_EVDO_0,
                NETWORK_TYPE_EVDO_A,
                NETWORK_TYPE_HSDPA,
                NETWORK_TYPE_HSUPA,
                NETWORK_TYPE_HSPA,
                NETWORK_TYPE_EVDO_B,
                NETWORK_TYPE_EHRPD,
                NETWORK_TYPE_HSPAP -> {
                    return ConnectionType._3G
                }

                NETWORK_TYPE_LTE -> return ConnectionType._4G
            }
        }
        return ConnectionType.UNKNOWN
    }

    enum class ConnectionType {
        UNKNOWN, WIFI, _2G, _3G, _4G
    }
}

/**
 * Just in old version for check connection
 */
@Suppress("deprecation")
private fun NetworkInfo.isMobile(): Boolean {
    return type == ConnectivityManager.TYPE_MOBILE && isConnectedOrConnecting
}

/**
 * Just in old version for check connection
 */
@Suppress("deprecation")
fun NetworkInfo.isWifi(): Boolean {
    return type == ConnectivityManager.TYPE_WIFI && isConnectedOrConnecting
}