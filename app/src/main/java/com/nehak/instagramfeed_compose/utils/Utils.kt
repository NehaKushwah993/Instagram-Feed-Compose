package com.nehak.instagramfeed_compose.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nehak.instagramfeed_compose.data.FeedDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

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


suspend fun parseFeedDataFromAssets(context: Context): List<FeedDataModel>? = withContext(Dispatchers.IO) {
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