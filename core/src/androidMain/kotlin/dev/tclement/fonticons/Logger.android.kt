/*
 * Copyright 2023 T. Clément (@tclement0922)
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

package dev.tclement.fonticons

import android.util.Log as AndroidLog

internal actual object Logger {
    actual fun v(tag: String, message: String) {
        AndroidLog.v(tag, message)
    }

    actual fun v(tag: String, message: String, throwable: Throwable) {
        AndroidLog.v(tag, message, throwable)
    }

    actual fun d(tag: String, message: String) {
        AndroidLog.d(tag, message)
    }

    actual fun d(tag: String, message: String, throwable: Throwable) {
        AndroidLog.d(tag, message, throwable)
    }

    actual fun i(tag: String, message: String) {
        AndroidLog.i(tag, message)
    }

    actual fun i(tag: String, message: String, throwable: Throwable) {
        AndroidLog.i(tag, message, throwable)
    }

    actual fun w(tag: String, message: String) {
        AndroidLog.w(tag, message)
    }

    actual fun w(tag: String, message: String, throwable: Throwable) {
        AndroidLog.w(tag, message, throwable)
    }

    actual fun e(tag: String, message: String) {
        AndroidLog.e(tag, message)
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        AndroidLog.e(tag, message, throwable)
    }
}