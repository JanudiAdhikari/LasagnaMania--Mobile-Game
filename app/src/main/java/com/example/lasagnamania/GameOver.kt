package com.example.lasagnamania

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {

    private lateinit var startGameAgain: ImageView
    private lateinit var displayScore: TextView
    private var score: String = ""
    private var highscore: String = ""
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Status Bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_game_over)

        score = intent.getStringExtra("score") ?: "0"

        startGameAgain = findViewById(R.id.startAgainBtn)
        displayScore = findViewById(R.id.score)

        val preferences = getSharedPreferences("PREFS", 0)
        highscore = preferences.getString("highscore", "0") ?: "0"

        startGameAgain.setOnClickListener {
            val intent1 = Intent(this@GameOver, Dashboard::class.java)
            startActivity(intent1)
        }

        // Replace if there is a higher score
        if (score.toInt() > highscore.toInt()) {
            highscore = score
            val editor = preferences.edit()
            editor.putString("highscore", score)
            editor.apply()
        }

        // Set click listener for "cancel" ImageView
        val cancelImageView = findViewById<ImageView>(R.id.closeGame)
        cancelImageView.setOnClickListener {
            finish() // Close the activity
        }

        displayScore.text = "Your Score : $score\n\n\n\n" + "Highest Score : $highscore"

        // Create and start media player for GameOver sound
        mediaPlayer = MediaPlayer.create(this, R.raw.gameover)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
