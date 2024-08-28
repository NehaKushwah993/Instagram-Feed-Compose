package com.nehak.instagramfeed_compose.widgets


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nehak.instagramfeed_compose.data.FeedDataModel
import com.nehak.instagramfeed_compose.player.InstagramVideoPlayer
import com.nehak.instagramfeed_compose.utils.aspectRatioToFloat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstagramFeedItemView(mediaItem: FeedDataModel, selectedMediaItemId: String?) {
    val context = LocalContext.current
    val aspectRatio = aspectRatioToFloat(mediaItem.aspect_ratio)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        mediaItem.media_list.size
    }

    Column {
        /** Profile **/
        InstagramProfile(
            storyAvailable = true,
            userDetails = mediaItem.poster_details
        )

        /** Center media item **/
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio) // Maintain the aspect ratio
        ) { page ->
            when (mediaItem.media_list[page].type) {
                "image" -> CachedImage(
                    url = mediaItem.media_list[page].link,
                    contentDescription = "post_image"
                )

                "video" -> {
                    val isPlaying =
                        mediaItem.id == selectedMediaItemId
                                && page == pagerState.currentPage
                    InstagramVideoPlayer(
                        videoUrl = mediaItem.media_list[page].link,
                        isPlaying = isPlaying,
                        thumbnailUrl = mediaItem.media_list[page].thumbnail
                    )
                }
            }
        }

        /** Dot selector**/
        if (mediaItem.media_list.size > 1) {
            DotSelector(
                currentPage = pagerState.currentPage,
                totalPages = mediaItem.media_list.size
            )
        }

        /** Bottom view**/
        InstagramPostBottomSection(
            mediaItem.poster_details,
            mediaItem.description,
            mediaItem.likes_count,
            mediaItem.posted_time,
            context,
        )
    }
}