package com.artear.networking.model.retrofit

import com.artear.networking.contract.Networking
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

fun <H : Any> executeWith(networking: Networking, call: () -> Call<H>): H {
    val networkCall = RetrofitNetworkCall(call())
    return networking.execute(networkCall)
}

fun Response<*>.getMessage(): String? {
    var message: String? = null
    if (errorBody() != null) {
        message = try {
            errorBody()!!.string()
        } catch (ignored: IOException) {
            val error = "Error while parsing error response"
            Timber.d(ignored, error)
            return error
        }
    }

    if (message == null || message.trim().isEmpty()) {
        message = message()
    }
    return message
}
