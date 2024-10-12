package com.example.utils

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import java.text.SimpleDateFormat
import java.util.Locale

fun imageLoader(context: Context): ImageLoader {
    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()
    return imageLoader
}

fun formatReleaseDate(
    releaseDate: String,
    inputFormatType: String? = null,
    outputFormatType: String? = null
): String {
    val inputFormatter = SimpleDateFormat(inputFormatType ?: "yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = SimpleDateFormat(outputFormatType ?: "dd MM yyyy", Locale.getDefault())
    val date = inputFormatter.parse(releaseDate)
    return date?.let { outputFormatter.format(it) } ?: "-"
}