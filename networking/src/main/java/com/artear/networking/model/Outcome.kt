package com.artear.networking.model

data class Outcome<T>(val value: T? = null, val error: Throwable? = null)

