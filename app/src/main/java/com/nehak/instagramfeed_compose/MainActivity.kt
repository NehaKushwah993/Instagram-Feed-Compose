package com.nehak.instagramfeed_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.nehak.instagramfeed_compose.data.FeedDataModel
import com.nehak.instagramfeed_compose.utils.parseFeedDataFromAssets
import com.nehak.instagramfeed_compose.widgets.InstagramFeedScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            
            /** Parse the local json data as feed list **/
            var feedDataList by remember { mutableStateOf<List<FeedDataModel>?>(null) }
            var loading by remember { mutableStateOf(true) }

            LaunchedEffect(context) {
                loading = true
                feedDataList = parseFeedDataFromAssets(context)
                loading = false
            }

            InstagramFeedScreen(feedDataList.orEmpty(), loading)
        }
    }
}
