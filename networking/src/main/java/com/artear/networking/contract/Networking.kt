package com.artear.networking.contract

import com.artear.networking.model.Outcome
import com.artear.networking.util.checkNullOrThrow
import com.artear.tools.android.log.logD
import com.artear.tools.exception.NoInternetConnectionException

/**
 * Repository for execute a [NetworkCall]
 */
interface Networking {

    fun isNetworkConnected(): Boolean

    fun <T : Any> execute(networkCall: NetworkCall<T>): T {
        logD { "- Repository - ServerCall - url = ${networkCall.urlLog()}" }
        if (!isNetworkConnected()) throw NoInternetConnectionException("The network data is unavailable")
        val outcome = networkCall.perform()
        return validResponse(outcome)
    }

    private fun <T : Any> validResponse(outcome: Outcome<T?>): T {
        checkNullOrThrow(outcome.error)
        return checkNotNull(outcome.value) { "Invalid outcome - response body is null!" }
    }
}