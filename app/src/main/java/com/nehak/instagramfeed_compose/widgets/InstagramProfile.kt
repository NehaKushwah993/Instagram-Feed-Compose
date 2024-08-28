package com.nehak.instagramfeed_compose.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nehak.instagramfeed_compose.R
import com.nehak.instagramfeed_compose.data.UserDetails

@Composable
fun InstagramProfile(userDetails: UserDetails?, storyAvailable: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(IntrinsicSize.Max),

        ) {

//        Spacer(modifier = Modifier.width(12.dp))

        // Profile Picture with Story Available Marker
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                CachedImage(
                    url = userDetails?.profile_pic_link ?: "",
                    contentDescription = "image",

                )
            }
            if (storyAvailable) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            border = BorderStroke(2.dp, color = Color.Magenta),
                            shape = CircleShape
                        )
                        .align(Alignment.BottomEnd)
                        .padding(1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Username and Options
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = userDetails?.display_name ?: "",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W600
                ),
                overflow = TextOverflow.Ellipsis
            )
        }

        // Three Dots (Options)
        Image(
            painter = painterResource(id = R.drawable.feed_more),
            contentDescription = "more",
            modifier = Modifier
        )

        Spacer(modifier = Modifier.width(8.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInstagramProfile() {
    InstagramProfile(
        userDetails = UserDetails(
            id = "user1",
            display_name = "john_doe",
            profile_pic_link = "https://example.com/profile.jpg"
        ),
        storyAvailable = true
    )
}