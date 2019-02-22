package com.artear.networking.util

import android.os.Build
import com.artear.networking.BuildConfig
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserAgentTest {

    private lateinit var userAgentExpected: String

    @Before
    fun setUp() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 17)
        userAgentExpected = "testapp/${BuildConfig.VERSION_NAME} (" +
                "${BuildConfig.APPLICATION_ID}; build: ${BuildConfig.VERSION_CODE}; " +
                "Android ${Build.VERSION.SDK_INT})"
    }

    @Test
    fun completeUserAgent() {
        val userAgent = UserAgent.Builder()
                .addAppName("testapp")
                .addVersionName(BuildConfig.VERSION_NAME)
                .addPackageName(BuildConfig.APPLICATION_ID)
                .addVersionCode(BuildConfig.VERSION_CODE)
                .addAndroidApiVersion(Build.VERSION.SDK_INT)
                .build()
        Assert.assertEquals(userAgentExpected, userAgent.toString())
    }

    @Test(expected = IllegalStateException::class)
    fun emptyVersionName() {
        UserAgent.Builder().addAppName("testapp")
                .addPackageName(BuildConfig.APPLICATION_ID)
                .addVersionCode(BuildConfig.VERSION_CODE)
                .addAndroidApiVersion(Build.VERSION.SDK_INT)
                .build()
    }

    @Test(expected = IllegalStateException::class)
    fun emptyVersionCode() {
        UserAgent.Builder().addAppName("testapp")
                .addVersionName(BuildConfig.VERSION_NAME)
                .addPackageName(BuildConfig.APPLICATION_ID)
                .addAndroidApiVersion(Build.VERSION.SDK_INT)
                .build()
    }

    @Test(expected = IllegalStateException::class)
    fun lowAndroidApiVersion() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 16)
        UserAgent.Builder().addAppName("testapp")
                .addVersionName(BuildConfig.VERSION_NAME)
                .addPackageName(BuildConfig.APPLICATION_ID)
                .addVersionCode(BuildConfig.VERSION_CODE)
                .addAndroidApiVersion(Build.VERSION.SDK_INT)
                .build()
    }
}