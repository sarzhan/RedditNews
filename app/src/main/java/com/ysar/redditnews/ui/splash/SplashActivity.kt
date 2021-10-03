package com.ysar.redditnews.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.ysar.redditnews.R
import com.ysar.redditnews.databinding.ActivitySplashBinding
import com.ysar.redditnews.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {


    private val SPLASH_DISPLAY_LENGTH: Long = 1500
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        binding.splashImageView.animation = zoomIn
        binding.splashImageView.startAnimation(zoomIn)


        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            MainActivity.start(this@SplashActivity)
            finish()
        }, SPLASH_DISPLAY_LENGTH)

    }

}
