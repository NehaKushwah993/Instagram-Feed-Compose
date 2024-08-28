package com.nehak.instagramfeed_compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

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
