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
package com.artear.networking.util

import android.os.Build

class UserAgent {

    private var description: String? = null

    var appName: String? = null
        private set
    var versionName: String? = null
        private set
    var packageName: String? = null
        private set
    var versionCode: Int = 0
        private set
    var androidApiVersion: Int = 0
        private set

    override fun toString(): String {
        return description ?: ""
    }

    class Builder {

        private val userAgent = UserAgent()

        fun addAppName(appName: String) = apply { userAgent.appName = appName }

        fun addVersionName(versionName: String) = apply { userAgent.versionName = versionName }

        fun addPackageName(packageName: String) = apply { userAgent.packageName = packageName }

        fun addVersionCode(versionCode: Int) = apply { userAgent.versionCode = versionCode }

        fun addAndroidApiVersion(androidApiVersion: Int) = apply {
            userAgent.androidApiVersion = androidApiVersion
        }

        private fun buildUrl(): String {
            val result = StringBuilder()
                    .append(userAgent.appName)
                    .append("/")
                    .append(userAgent.versionName)
                    .append(" (")
                    .append(userAgent.packageName)
                    .append("; build: ")
                    .append(userAgent.versionCode)
                    .append("; Android ")
                    .append(userAgent.androidApiVersion)
                    .append(")")
            return result.toString()
        }

        private fun canNotBeNull(target: String) = "The $target name can not be null"

        fun build(): UserAgent {
            checkNotNull(userAgent.appName) { canNotBeNull("application") }
            checkNotNull(userAgent.versionName) { canNotBeNull("version") }
            checkNotNull(userAgent.packageName) { canNotBeNull("package") }
            check(userAgent.versionCode > 0) { "You must set an incremental version code" }
            check(userAgent.androidApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                "Android version = ${userAgent.androidApiVersion} You should use at least " +
                        "jelly bean mr1 (17) android version"
            }
            userAgent.description = buildUrl()
            return userAgent
        }

    }

}
