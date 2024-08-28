package com.nehak.instagramfeed_compose.widgets


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nehak.instagramfeed_compose.data.FeedDataModel
import com.nehak.instagramfeed_compose.data.UserDetails
import com.nehak.instagramfeed_compose.player.VideoPlayerView
import com.nehak.instagramfeed_compose.utils.aspectRatioToFloat
import com.nehak.instagramfeed_compose.utils.formatTimeAgo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedItemView(mediaItem: FeedDataModel, selectedMediaItemId: String?) {
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
                    VideoPlayerView(
                        videoUrl = mediaItem.media_list[page].link,
                        isPlaying = isPlaying,
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
            mediaItem.posted_time
        )
    }
}

@Composable
fun InstagramPostBottomSection(
    userDetails: UserDetails?,
    description: String,
    likesCount: String,
    postedTime: Long
) {
    Column(
        modifier = Modifier.padding(
            horizontal = 12.dp
        )
    ) {


        // Action Buttons
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Like Button
            IconButton(
                onClick = { /* Handle Like Action */ },
                modifier = Modifier
                    .padding(0.dp)
                    .background(Color.White),
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = Color.Black
                )
            }

            // Comment Button
            IconButton(onClick = { /* Handle Comment Action */ }) {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Comment",
                    tint = Color.Black
                )
            }

            // Share Button
            IconButton(onClick = { /* Handle Share Action */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Black
                )
            }
        }

        // Description
        Spacer(modifier = Modifier.height(8.dp))

        userDetails?.let { user ->
            val descriptionWithUsernameAndTags = buildAnnotatedString {
                // Username in Bold
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${user.display_name} ")
                }

                // Process description for hashtags and mentions
                val regex = "(#\\w+|@\\w+)".toRegex()
                var lastEndIndex = 0

                regex.findAll(description).forEach { match ->
                    val startIndex = match.range.first
                    val endIndex = match.range.last + 1
                    val tag = match.value

                    append(description.substring(lastEndIndex, startIndex))

                    withStyle(style = SpanStyle(color = Color(0xFF3348B8))) {
                        append(tag)
                    }

                    lastEndIndex = endIndex
                }

                // Append the remaining part of the description
                append(description.substring(lastEndIndex))
            }

            Text(
                text = descriptionWithUsernameAndTags,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        // Like and Comment Counts
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$likesCount likes",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        // Posted At
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatTimeAgo(postedTime),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
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