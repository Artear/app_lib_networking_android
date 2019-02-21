package com.artear.networking.model.retrofit

import com.artear.networking.model.Outcome
import com.artear.networking.contract.NetworkCall
import com.artear.tools.exception.BadResponseException
import retrofit2.Call

class RetrofitNetworkCall<T>(private val call: Call<T>) : NetworkCall<T> {

    override fun urlLog(): String = call.request().url().url().toString()

    override fun perform(): Outcome<T?> {

        val response = call.execute()

        if (!response.isSuccessful) {
            val message: String? = response.getMessage()
            return Outcome(error = BadResponseException(message))
        }

        return Outcome(response.body())
    }

}



