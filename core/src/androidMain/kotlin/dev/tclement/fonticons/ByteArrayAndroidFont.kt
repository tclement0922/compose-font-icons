/*
 * Copyright 2024-2025 T. Clément (@tclement0922)
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

import android.content.Context
import android.graphics.Typeface
import android.graphics.fonts.Font
import android.graphics.fonts.FontFamily
import android.graphics.fonts.FontVariationAxis
import android.os.Build
import android.system.ErrnoException
import android.system.Os
import android.system.OsConstants
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastMap
import java.io.*
import java.nio.channels.FileChannel

/**
 * For Android 11 and higher (memfd_create was introduced in Android 11) :
 * Creates a temporary file descriptor containing the byte array
 */
@RequiresApi(Build.VERSION_CODES.R)
@ExperimentalFontIconsApi
private fun createTempFd(bytes: ByteArray, fdName: String): FileDescriptor {
    try {
        val fd = Os.memfd_create(fdName, 0)
        FileOutputStream(fd).use { it.write(bytes) }
        return fd
    } catch (e: ErrnoException) {
        Log.e("VariableIconFont", "Error creating memory file descriptor, falling back to temp file", e)
    }
    try {
        return Os.open(
            createTempFile(bytes, fdName).path,
            OsConstants.O_RDONLY,
            OsConstants.S_IRWXU or OsConstants.S_IRWXG
        )
    } catch (e: ErrnoException) {
        throw IOException("Error opening temp file", e)
    }
}

/**
 * For Android 10 and lower :
 * Creates a temporary file containing the byte array
 */
@ExperimentalFontIconsApi
private fun createTempFile(bytes: ByteArray, fileName: String): File {
    val tempFile = File.createTempFile(fileName, null)
    tempFile.writeBytes(bytes)
    return tempFile
}

/**
 * There is a [Typeface.Builder] constructor that takes a [FileDescriptor], but unfortunately the file descriptor gets
 * duplicated under the hood without any way to close it, resulting in a possible memory leak and a warning in the
 * logcat.
 * This function does the same thing as the [Typeface.Builder] constructor, except the duplication of the file
 * descriptor and some useless (in this use case) steps are avoided.
 */
@RequiresApi(Build.VERSION_CODES.Q)
@ExperimentalFontIconsApi
private fun typefaceFromFileDescriptor(
    fd: FileDescriptor,
    weight: FontWeight,
    style: FontStyle,
    variationSettings: FontVariation.Settings,
    density: Density
): Typeface {
    val buffer = FileInputStream(fd).use {
        it.channel.map(FileChannel.MapMode.READ_ONLY, 0, it.channel.size())
    }
    val slant =
        if (style == FontStyle.Italic) android.graphics.fonts.FontStyle.FONT_SLANT_ITALIC else android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT
    val font = Font.Builder(buffer)
        .setFontVariationSettings(
            variationSettings.settings.fastMap {
                FontVariationAxis(
                    it.axisName,
                    it.toVariationValue(density)
                )
            }.toTypedArray()
        )
        .setWeight(weight.weight)
        .setSlant(slant)
        .build()
    buffer.clear()
    val family = FontFamily.Builder(font).build()
    return Typeface.CustomFallbackBuilder(family)
        .setStyle(android.graphics.fonts.FontStyle(weight.weight, slant))
        .build()
}

/**
 * A font loaded from a byte array containing the font data.
 * There is no native way to do this in Android, instead we use some workarounds:
 * - On Android 11 and higher this will shortly create an in-memory file descriptor (using Os.memfd_create).
 * - On Android 8 and higher this will create a temporary file (this file will be deleted after the font is loaded).
 * - On Android 7 and lower this will also create a temporary file, but the variation settings will be ignored.
 * The typeface will be cached after the first load, so the file descriptor or temporary file will be created only once.
 */
@ExperimentalFontIconsApi
internal class ByteArrayAndroidFont(
    private val array: ByteArray,
    override val weight: FontWeight,
    override val style: FontStyle,
    variationSettings: FontVariation.Settings
) : AndroidFont(
    loadingStrategy = FontLoadingStrategy.Blocking,
    typefaceLoader = Companion,
    variationSettings = variationSettings
) {
    private val lock = Any()

    private companion object : TypefaceLoader {
        override suspend fun awaitLoad(context: Context, font: AndroidFont): Typeface? {
            return loadBlocking(context, font)
        }

        override fun loadBlocking(context: Context, font: AndroidFont): Typeface? {
            if (font !is ByteArrayAndroidFont) {
                return null
            }
            return font.loadTypeface(context)
        }
    }

    private lateinit var typeface: Typeface

    private fun loadTypeface(context: Context): Typeface = synchronized(lock) {
        if (::typeface.isInitialized)
            return typeface
        val density = Density(context)
        val name = array.hashCode().toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val fd = createTempFd(bytes = array, fdName = name)
            typeface = typefaceFromFileDescriptor(fd, weight, style, variationSettings, density)
            try {
                Os.close(fd)
            } catch (e: ErrnoException) {
                Log.e("VariableIconFont", "Error closing memory file descriptor", e)
            }
        } else {
            val file = createTempFile(bytes = array, fileName = name)
            typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Typeface.Builder(file)
                    .setFontVariationSettings(
                        variationSettings.settings.fastMap {
                            FontVariationAxis(
                                it.axisName,
                                it.toVariationValue(density)
                            )
                        }.toTypedArray()
                    )
                    .setWeight(weight.weight)
                    .setItalic(style == FontStyle.Italic)
                    .build()
            } else {
                // Same behavior as Compose fonts, we just ignore the variation settings as they are not supported
                Typeface.create(
                    Typeface.createFromFile(file),
                    when {
                        style == FontStyle.Italic && weight >= FontWeight.W700 -> Typeface.BOLD_ITALIC
                        style == FontStyle.Italic -> Typeface.ITALIC
                        weight >= FontWeight.W700 -> Typeface.BOLD
                        else -> Typeface.NORMAL
                    }
                )
            }
            try {
                file.delete()
            } catch (e: SecurityException) {
                Log.e("VariableIconFont", "Error deleting temp file", e)
            }
        }
        return typeface
    }
}