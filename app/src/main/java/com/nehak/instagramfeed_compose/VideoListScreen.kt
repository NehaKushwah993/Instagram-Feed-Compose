package com.nehak.instagramfeed_compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.nehak.instagramfeed_compose.data.FeedDataModel
import com.nehak.instagramfeed_compose.player.VideoPlayerView
import com.nehak.instagramfeed_compose.utils.aspectRatioToFloat
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(mediaItems: List<FeedDataModel>, loading: Boolean) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Instagram Clone") })

    }) {

        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
        ) {

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                val listState = rememberLazyListState()
                val scrollThreshold = 10

                // Track the most visible item index
                var visibleMediaItemId by remember { mutableStateOf<String?>(null) }
                // Track the current selected page in pager
                var lastScrollOffset by remember { mutableIntStateOf(scrollThreshold) }

                // Track visible items
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

                                // Determine the most visible item
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
                                visibleMediaItemId = item?.id
                            }
                            lastScrollOffset = currentOffset

                        }
                }

                // list of media
                LazyColumn(state = listState) {
                    items(mediaItems) { mediaItem ->

                        val aspectRatio = aspectRatioToFloat(mediaItem.aspect_ratio)

                        UserInfoView()

                        val pagerState = rememberPagerState(
                            initialPage = 0,
                            initialPageOffsetFraction = 0f
                        ) {
                            mediaItem.media_list.size
                        }

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(aspectRatio) // Maintain the aspect ratio
                        ) { page ->
                            when (mediaItem.media_list[page].type) {
                                "image" -> CachedImage(
                                    url = mediaItem.media_list[page].link,
                                    contentDescription = "image"
                                )

                                "video" -> {
                                    val isPlaying =
                                        mediaItem.id == visibleMediaItemId
                                                && page == pagerState.currentPage
                                    VideoPlayerView(
                                        videoUrl = mediaItem.media_list[page].link,
                                        isPlaying = isPlaying,
                                    )
                                }
                            }
                        }
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .aspectRatio(aspectRatio)
//                        ) {
//                            when (mediaItem.media_list.first().type) {
//                                "image" -> CachedImage(
//                                    url = mediaItem.media_list.first().link,
//                                    contentDescription = "image"
//                                )
//
//                                "video" -> {
//                                    val isPlaying =
//                                        mediaItem.id == visibleMediaItemId
////                                                && mediaItem.media_list[page].id == visibleVideoId
//                                    VideoPlayerView(
//                                        videoUrl = mediaItem.media_list.first().link,
//                                        isPlaying = isPlaying,
//                                    )
//                                }
//                            }
//                        }

                        if (mediaItem.media_list.size > 1) {
                            DotSelector(
                                currentPage = pagerState.currentPage,
                                totalPages = mediaItem.media_list.size
                            )
                        }
                        FeedActionAndCommentsView()
                    }
                }
            }
        }
    }
}


@Composable
fun UserInfoView() {
    Image(
        painter = painterResource(id = R.drawable.dummy_top_view),
        contentDescription = "Sample Image",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(680 / 84f)
    )
}

@Composable
fun FeedActionAndCommentsView() {
    Image(
        painter = painterResource(id = R.drawable.dummy_bottom_view),
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(680 / 259f)
    )
}

@Composable
fun DotSelector(currentPage: Int, totalPages: Int) {
    Box(
        modifier = Modifier.fillMaxWidth(), // Fill the available space
        contentAlignment = Alignment.Center // Center the content inside the Box
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalPages) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (index == currentPage) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun CachedImage(url: String, contentDescription: String?) {
    val painter: Painter = // Smooth transition when loading
        rememberAsyncImagePainter(
            ImageRequest // Optional: Apply transformations
                .Builder(LocalContext.current).data(data = url)
                .apply(
                    block = fun ImageRequest.Builder.() {
                        crossfade(true) // Smooth transition when loading
                    },
                ).build()
        )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.fillMaxSize(), // Adjust as needed
        colorFilter = null // Optional: Apply color filter if needed
    )
}
