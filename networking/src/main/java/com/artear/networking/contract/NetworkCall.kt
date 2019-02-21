package com.artear.networking.contract

import com.artear.networking.model.Outcome

interface NetworkCall<T> {

    fun urlLog(): String? = null

    fun perform(): Outcome<T?>
}