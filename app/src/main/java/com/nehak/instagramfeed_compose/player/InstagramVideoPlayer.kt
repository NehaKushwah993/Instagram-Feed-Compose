package com.nehak.instagramfeed_compose.player

import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter

/**
 * Composable function that displays an ExoPlayer to play a video using Jetpack Compose.
 *
 * @OptIn annotation to UnstableApi is used to indicate that the API is still experimental and may
 * undergo changes in the future.
 *
 * @see videoUrl Replace with the actual URI of the video to be played.
 */
@Composable
fun InstagramVideoPlayer(
    videoUrl: String,
    isPlaying: Boolean,
    thumbnailUrl: String,
    shouldUseController: Boolean = false,
    repeatMode: RepeatMode = RepeatMode.Restart
) {
    val localContext = LocalContext.current

    /**
     * Initialise ExoPlayer
     */
    val exoPlayer = remember { ExoPlayer.Builder(localContext).build() }

    var isVideoPlaying by remember { mutableStateOf(isPlaying) }

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
     * Play/Pause based on isPlaying value change
     */
    LaunchedEffect(isPlaying) {
        if (exoPlayer.playWhenReady != isPlaying) {
            exoPlayer.playWhenReady = isPlaying
        }
    }

    /**
     * Box layout to layer the thumbnail and the video player
     */
    Box(modifier = Modifier.fillMaxSize()) {
        if (!isVideoPlaying) {
            Image(
                painter = rememberAsyncImagePainter(thumbnailUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = shouldUseController
                }
            }, update = { playerView ->
                playerView.player?.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        isVideoPlaying = isPlaying
                    }
                })
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
