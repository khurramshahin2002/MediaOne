package com.example.mediaone
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment

class MusicPlayer : Fragment() {
    lateinit var mediaPlayer: MediaPlayer
    var totalTime: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_player, container, false)

        val btnPlay = view.findViewById<ImageView>(R.id.play)
        val btnPause = view.findViewById<ImageView>(R.id.pause)
        val btnResume = view.findViewById<ImageView>(R.id.resume)
        val seekBar = view.findViewById<SeekBar>(R.id.seekBar)

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.music1)
        mediaPlayer.setVolume(1f, 1f)
        totalTime = mediaPlayer.duration

        btnPlay.setOnClickListener {
            mediaPlayer.start()
        }
        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }
        btnResume.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
        seekBar.max = mediaPlayer.duration

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                seekBar.progress = mediaPlayer.currentPosition
                handler.postDelayed(this, 1000)
            }
        }, 0)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed for now, you can leave it as is
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed for now, you can leave it as is
            }
        })
        // Connect the button to launch the new activity
        val nextActivityButton = view.findViewById<Button>(R.id.nextActivityButton)
        nextActivityButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity2::class.java)
            startActivity(intent)
        }

        return view
    }
}
