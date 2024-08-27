package com.nehak.instagramfeed_compose.player

import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Composable function that displays an ExoPlayer to play a video using Jetpack Compose.
 *
 * @OptIn annotation to UnstableApi is used to indicate that the API is still experimental and may
 * undergo changes in the future.
 *
 * @see videoUrl Replace with the actual URI of the video to be played.
 */
@Composable
fun VideoPlayerView(
    videoUrl: String,
    isPlaying: Boolean,
    shouldUseController: Boolean = false,
    repeatMode: RepeatMode = RepeatMode.Restart
) {
    val localContext = LocalContext.current

    /**
     * Initialise ExoPlayer
     */
    val exoPlayer = remember { ExoPlayer.Builder(localContext).build() }

    /**
     * Prepare for play
     */
    DisposableEffect(videoUrl) {
        exoPlayer.setMediaItem(MediaItem.fromUri(videoUrl))
        exoPlayer.prepare()

        // Start playback if isPlaying is true
        exoPlayer.playWhenReady = isPlaying
        exoPlayer.repeatMode = repeatMode.ordinal

        onDispose {
            exoPlayer.release()
        }
    }

    /**
     * Play/Pause based of isPlaying value change
     */
    LaunchedEffect(isPlaying) {
        if (exoPlayer.playWhenReady != isPlaying) {
            exoPlayer.playWhenReady = isPlaying
        }
    }

    /**
     * View for player
     */
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = shouldUseController
                // Enable controls if needed
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}