package dev.tclement.fonticons

import platform.Foundation.NSLog

/**
 * Internal logger class, each target has its own implementation (Log for Android, System.err/out for desktop, console for JS/WASM)
 */
internal actual object Logger {

    actual fun v(tag: String, message: String) = NSLog("$tag: $message")
    actual fun v(tag: String, message: String, throwable: Throwable) = NSLog("$tag: $message\n$throwable")
    actual fun d(tag: String, message: String) = NSLog("$tag: $message")
    actual fun d(tag: String, message: String, throwable: Throwable) = NSLog("$tag: $message\n$throwable")
    actual fun i(tag: String, message: String) = NSLog("$tag: $message")
    actual fun i(tag: String, message: String, throwable: Throwable) = NSLog("$tag: $message\n$throwable")
    actual fun w(tag: String, message: String) = NSLog("$tag: $message")
    actual fun w(tag: String, message: String, throwable: Throwable) = NSLog("$tag: $message\n$throwable")
    actual fun e(tag: String, message: String) = NSLog("$tag: $message")
    actual fun e(tag: String, message: String, throwable: Throwable) = NSLog("$tag: $message\n$throwable")
}
