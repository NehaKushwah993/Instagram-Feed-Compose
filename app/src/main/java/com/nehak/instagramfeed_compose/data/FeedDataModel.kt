package com.nehak.instagramfeed_compose.data
data class FeedDataModel(
    val id: String,
    val aspect_ratio: String,
    val media_list: List<MediaItem>
)
