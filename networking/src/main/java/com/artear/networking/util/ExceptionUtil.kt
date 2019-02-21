package com.artear.networking.util

fun <T : Throwable> checkNullOrThrow(value: T?): Nothing? = value?.let { throw value }
