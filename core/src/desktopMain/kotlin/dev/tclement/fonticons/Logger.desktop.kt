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

import java.io.PrintStream

/**
 * The logger implementation for the JVM, using [System.out] and [System.err].
 */
internal actual object Logger {
    private fun sout(tag: String, level: Char, message: String, stream: PrintStream = System.out) {
        stream.println("[$tag] ($level) $message")
    }

    private fun sout(tag: String, level: Char, message: String, throwable: Throwable, stream: PrintStream = System.out) {
        sout(tag, level, "$message\n${throwable.stackTraceToString()}", stream)
    }

    actual fun v(tag: String, message: String) {
        sout(tag, 'V', message)
    }

    actual fun v(tag: String, message: String, throwable: Throwable) {
        sout(tag, 'V', message, throwable)
    }

    actual fun d(tag: String, message: String) {
        sout(tag, 'D', message)
    }

    actual fun d(tag: String, message: String, throwable: Throwable) {
        sout(tag, 'D', message, throwable)
    }

    actual fun i(tag: String, message: String) {
        sout(tag, 'I', message)
    }

    actual fun i(tag: String, message: String, throwable: Throwable) {
        sout(tag, 'I', message, throwable)
    }

    actual fun w(tag: String, message: String) {
        sout(tag, 'W', message)
    }

    actual fun w(tag: String, message: String, throwable: Throwable) {
        sout(tag, 'W', message, throwable)
    }

    actual fun e(tag: String, message: String) {
        sout(tag, 'E', message, System.err)
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        sout(tag, 'E', message, throwable, System.err)
    }
}