package com.nehak.instagramfeed_compose.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nehak.instagramfeed_compose.data.FeedDataModel
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(mediaItems: List<FeedDataModel>, loading: Boolean) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Instagram Clone") })
    }, containerColor = Color.White) {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                ListOfFeedItems(mediaItems)
            }
        }
    }
}

@Composable
private fun ListOfFeedItems(mediaItems: List<FeedDataModel>) {
    val listState = rememberLazyListState()
    val scrollThreshold = 10

    /** Track the most visible item index **/
    var mostVisibleMediaItemId by remember { mutableStateOf<String?>(null) }
    // Track the current selected page in pager
    var lastScrollOffset by remember { mutableIntStateOf(scrollThreshold) }

    /** Track visible items, and pick the one tha has more visibility percentage than others in frame**/
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { currentOffset ->

                val delta = currentOffset - lastScrollOffset
                if (delta.absoluteValue >= scrollThreshold) {
                    // Handle crossing 10-pixel threshold

                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    val viewportStart = listState.layoutInfo.viewportStartOffset
                    val viewportEnd = listState.layoutInfo.viewportEndOffset

                    // Calculate visibility percentage for each visible item
                    val visibilityPercentages = visibleItems.map { item ->
                        val itemStart = item.offset
                        val itemEnd = item.offset + item.size

                        // Calculate visible portion of the item within the viewport
                        val visibleStart = max(viewportStart, itemStart)
                        val visibleEnd = min(viewportEnd, itemEnd)
                        val visibleHeight = max(0, visibleEnd - visibleStart)
                        val totalHeight = item.size
                        var visiblePercentage =
                            (visibleHeight / totalHeight.toFloat()) * 100

                        if (visiblePercentage <= 30) {
                            visiblePercentage = 0f
                        }

                        item.index to (visiblePercentage)
                    }

                    /** Determine the most visible item **/
                    val item = visibilityPercentages
                        .sortedWith(compareByDescending<Pair<Int, Float>> { sorted -> sorted.second }
                            .thenBy { index ->
                                val itemOffset =
                                    visibleItems.find { visible -> visible.index == index.first }?.offset
                                        ?: 0
                                itemOffset
                            })
                        .firstOrNull()
                        ?.let { (index, _) -> mediaItems.getOrNull(index) }
                    mostVisibleMediaItemId = item?.id
                }
                lastScrollOffset = currentOffset

            }
    }

    /** list of media **/
    LazyColumn(state = listState) {
        items(mediaItems) { mediaItem ->
            FeedItemView(mediaItem = mediaItem, selectedMediaItemId = mostVisibleMediaItemId)
        }
    }
}
