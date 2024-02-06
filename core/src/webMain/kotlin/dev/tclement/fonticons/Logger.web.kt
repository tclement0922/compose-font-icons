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
    private fun log(tag: String, level: Char, message: String, logFn: (args: Array<Any?>) -> Unit = console::log) {
        logFn(arrayOf("[$tag] ($level) $message"))
    }
    
    private fun log(tag: String, level: Char, message: String, throwable: Throwable, logFn: (args: Array<Any?>) -> Unit = console::log) {
        log(tag, level, "$message\n${throwable.stackTraceToString()}", logFn)
    }

    actual fun v(tag: String, message: String) {
        log(tag, 'V', message)
    }

    actual fun v(tag: String, message: String, throwable: Throwable) {
        log(tag, 'V', message, throwable)
    }

    actual fun d(tag: String, message: String) {
        log(tag, 'D', message)
    }

    actual fun d(tag: String, message: String, throwable: Throwable) {
        log(tag, 'D', message, throwable)
    }

    actual fun i(tag: String, message: String) {
        log(tag, 'I', message, console::info)
    }

    actual fun i(tag: String, message: String, throwable: Throwable) {
        log(tag, 'I', message, throwable, console::info)
    }

    actual fun w(tag: String, message: String) {
        log(tag, 'W', message, console::warn)
    }

    actual fun w(tag: String, message: String, throwable: Throwable) {
        log(tag, 'W', message, throwable, console::warn)
    }

    actual fun e(tag: String, message: String) {
        log(tag, 'E', message, console::error)
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        log(tag, 'E', message, throwable, console::error)
    }
}