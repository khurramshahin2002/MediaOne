package com.example.mediaone


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment

class VideoPlayer : Fragment(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController
    private var currentVideoPosition = 0L // Stores current playback position

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_player, container, false)

        videoView = view.findViewById(R.id.video)
        mediaController = MediaController(requireContext())
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        val vpath = "android.resource://" + requireContext().packageName + "/" + R.raw.sinchan

        videoView.setVideoPath(vpath)
        videoView.setOnPreparedListener(this) // Register for prepared event
        videoView.setOnCompletionListener(this) // Register for completion event

        return view
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Handle orientation change and restore playback
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape mode: Allow full-screen video (optional)
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                // Portrait mode: Reset requested orientation (optional)
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }
        }

        // If video is prepared, resume playback from saved position
        if (videoView.isPlaying) {
            videoView.seekTo(currentVideoPosition.toInt())
            videoView.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save current video position
        outState.putLong("currentPosition", currentVideoPosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        // Restore video position from saved state
        if (savedInstanceState != null) {
            currentVideoPosition = savedInstanceState.getLong("currentPosition", 0L)
        }
    }


    override fun onPrepared(mp: MediaPlayer) {
        // Video is ready to play, potentially restore saved position
        if (currentVideoPosition > 0) {
            videoView.seekTo(currentVideoPosition.toInt())
            videoView.start()
        }
    }

    override fun onCompletion(mp: MediaPlayer) {
        // Video playback completed, show a message
        Toast.makeText(context, "Video playback completed", Toast.LENGTH_SHORT).show()
    }


}
