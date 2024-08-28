package com.nehak.instagramfeed_compose.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nehak.instagramfeed_compose.data.FeedDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

fun aspectRatioToFloat(aspectRatio: String): Float {
    // Split the aspect ratio string into numerator and denominator
    val parts = aspectRatio.split(":")
    if (parts.size != 2) {
        throw IllegalArgumentException("Invalid aspect ratio format")
    }

    // Convert the parts to integers
    val numerator = parts[0].toFloatOrNull() ?: throw NumberFormatException("Invalid numerator")
    val denominator = parts[1].toFloatOrNull() ?: throw NumberFormatException("Invalid denominator")

    // Calculate and return the ratio as a double
    return numerator / denominator
}


suspend fun parseFeedDataFromAssets(context: Context): List<FeedDataModel>? =
    withContext(Dispatchers.IO) {
        return@withContext try {
            // Open the JSON file from assets
            val inputStream = context.assets.open("feed_data.json")
            val reader = InputStreamReader(inputStream)

            // Create a Gson instance
            val gson = Gson()

            // Define the type of the data you expect (a list of FeedDataModel)
            val feedDataType = object : TypeToken<List<FeedDataModel>>() {}.type

            // Parse JSON into a list of FeedDataModel
            gson.fromJson(reader, feedDataType) as List<FeedDataModel>?
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


fun formatTimeAgo(postedTimeMillis: Long): String {
    val currentTimeMillis = System.currentTimeMillis()
    val diffMillis = currentTimeMillis - postedTimeMillis

    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        days > (365 * 2) -> "${days / 365} years ago"
        days > 365 -> "${days / 365} year ago"
        days > 0 -> "$days days ago"
        hours > 0 -> "$hours hours ago"
        minutes > 0 -> "$minutes minutes ago"
        else -> "$seconds seconds ago"
    }
}