package com.artear.networking

import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.M

fun <H, T> H.isLollipop(validated: (H) -> T, denied: (H) -> T): T = isMinApi(LOLLIPOP, validated, denied)

fun <H, T> H.isMarshMallow(validated: (H) -> T, denied: (H) -> T): T = isMinApi(M, validated, denied)

fun <H, T> H.isMinApi(minApi: Int, validated: (H) -> T, denied: (H) -> T): T {
    return if (Build.VERSION.SDK_INT >= minApi) validated(this) else denied(this)
}