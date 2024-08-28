package com.nehak.instagramfeed_compose.data

data class FeedDataModel(
    val id: String,
    val poster_details: UserDetails?,
    val aspect_ratio: String,
    val likes_count: String,
    val description: String,
    val posted_time: Long,
    val media_list: List<MediaItem>
)

data class UserDetails(
    val id: String,
    val display_name: String,
    val profile_pic_link: String,
)

data class MediaItem(
    val link: String,
    val id: String,
    val type: String,
    val thumbnail: String
)