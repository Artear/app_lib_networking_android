package com.artear.networking.model

import android.content.Context
import com.artear.networking.contract.Networking
import com.artear.networking.util.ConnectionUtil


open class AndroidNetworking(val context: Context) : Networking {

    override fun isNetworkConnected(): Boolean {
        return ConnectionUtil.isConnected(context)
    }
}