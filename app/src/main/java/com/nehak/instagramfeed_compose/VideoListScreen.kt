package com.nehak.instagramfeed_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nehak.instagramfeed_compose.data.FeedDataModel
import com.nehak.instagramfeed_compose.utils.aspectRatioToFloat
import kotlin.math.max
import kotlin.math.min

@Composable
fun VideoListScreen(mediaItems: List<FeedDataModel>, loading: Boolean) {
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        val listState = rememberLazyListState()

        // Track the most visible item index
        var visibleItemId by remember { mutableStateOf<String?>(null) }

        // Track visible items
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
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
                        val visiblePercentage = (visibleHeight / totalHeight.toFloat()) * 100

                        item.index to visiblePercentage
                    }

                    // Determine the most visible item
                    visibleItemId = visibilityPercentages
                        .sortedWith(compareByDescending<Pair<Int, Float>> { it.second }
                            .thenBy { index ->
                                val itemOffset =
                                    visibleItems.find { it.index == index.first }?.offset ?: 0
                                itemOffset
                            })
                        .firstOrNull()
                        ?.let { (index, _) -> mediaItems.getOrNull(index) }?.id
                }
        }

        // list of media
        LazyColumn(state = listState) {
            items(mediaItems) { mediaItem ->

                val aspectRatio = aspectRatioToFloat(mediaItem.aspect_ratio)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.Red)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio) // Maintain the aspect ratio
                        .background(if (visibleItemId == mediaItem.id) Color.Gray else Color.Black) // Placeholder for content
                ) {

                    when (mediaItem.media_list.first().type) {
                        "image" -> Box {}
                        "video" -> {
                            val isPlaying = mediaItem.id == visibleItemId
                            VideoPlayerView(
                                videoUrl = mediaItem.media_list.first().link,
                                isPlaying = isPlaying,
                            )
                        }
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color.Blue)
                )
            }
        }
    }
}
