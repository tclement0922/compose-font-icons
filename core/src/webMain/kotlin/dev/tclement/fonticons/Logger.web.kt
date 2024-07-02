/*
 * Copyright 2024 T. Clément (@tclement0922)
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

internal actual object Logger {
    private fun format(tag: String, message: String) = "[$tag] $message"

    private fun format(tag: String, message: String, throwable: Throwable) =
        format(tag, "$message\n${throwable.stackTraceToString()}")

    actual fun v(tag: String, message: String) {
        console.log(format(tag, message))
    }

    actual fun v(tag: String, message: String, throwable: Throwable) {
        console.log(format(tag, message, throwable))
    }

    actual fun d(tag: String, message: String) {
        console.debug(format(tag, message))
    }

    actual fun d(tag: String, message: String, throwable: Throwable) {
        console.debug(format(tag, message, throwable))
    }

    actual fun i(tag: String, message: String) {
        console.info(format(tag, message))
    }

    actual fun i(tag: String, message: String, throwable: Throwable) {
        console.info(format(tag, message, throwable))
    }

    actual fun w(tag: String, message: String) {
        console.warn(format(tag, message))
    }

    actual fun w(tag: String, message: String, throwable: Throwable) {
        console.warn(format(tag, message, throwable))
    }

    actual fun e(tag: String, message: String) {
        console.error(format(tag, message))
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        console.error(format(tag, message, throwable))
    }
}