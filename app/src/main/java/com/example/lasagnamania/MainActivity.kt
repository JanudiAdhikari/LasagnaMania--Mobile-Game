package com.example.lasagnamania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val SPLASH_SCREEN = 5000L

    // Variables
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var image: ImageView
    lateinit var logo: TextView
    lateinit var slogan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Status Bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //Hide Nav Bar
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        setContentView(R.layout.activity_main)

        // Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_splash_anim)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_splash_anim)

        image = findViewById(R.id.splashImg)
        logo = findViewById(R.id.splashname)
        slogan = findViewById(R.id.splashmotto)

        // Assign Animations
        image.setAnimation(topAnim)
        logo.setAnimation(bottomAnim)
        slogan.setAnimation(bottomAnim)

        //Transition to another activity
        Handler().postDelayed({
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN)
    }
}
