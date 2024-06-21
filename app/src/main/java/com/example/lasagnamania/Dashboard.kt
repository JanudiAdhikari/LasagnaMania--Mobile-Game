package com.example.lasagnamania

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Dashboard : AppCompatActivity() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    private var isSoundEnabled = true // Flag to track sound state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Status Bar and Navigation bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        setContentView(R.layout.activity_dashboard)

        // Set click listener for "cancel" ImageView
        val cancelImageView = findViewById<ImageView>(R.id.cancel)
        cancelImageView.setOnClickListener {
            finish()
        }

        // Set click listener for "sound" ImageView
        val soundImageView = findViewById<ImageView>(R.id.sound)
        soundImageView.setOnClickListener {
            isSoundEnabled = !isSoundEnabled
            if (isSoundEnabled) {
                soundImageView.setImageResource(R.drawable.menu)
                mediaPlayer?.start()
            } else {
                soundImageView.setImageResource(R.drawable.mute)
                mediaPlayer?.pause()
            }
        }

        // Set click listener for startBtn
        val startBtn = findViewById<ImageButton>(R.id.startBtn)
        startBtn.setOnClickListener {
            val intent = Intent(this@Dashboard, GamePlay::class.java)
            startActivity(intent)
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.mainaudio)
        mediaPlayer?.isLooping = true

        // Start playing audio initially, considering you might want sound on by default
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
