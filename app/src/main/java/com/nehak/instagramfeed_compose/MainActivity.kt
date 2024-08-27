package com.nehak.instagramfeed_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.nehak.instagramfeed_compose.data.FeedDataModel
import com.nehak.instagramfeed_compose.utils.parseFeedDataFromAssets
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            var feedDataList by remember { mutableStateOf<List<FeedDataModel>?>(null) }
            var loading by remember { mutableStateOf(true) }

            LaunchedEffect(context) {
                loading = true
                feedDataList = parseFeedDataFromAssets(context)
                loading = false
            }

            VideoListScreen(feedDataList.orEmpty(), loading)
        }
    }
}
