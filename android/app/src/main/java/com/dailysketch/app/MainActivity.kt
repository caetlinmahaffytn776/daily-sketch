package com.dailysketch.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dailysketch.app.core.di.ServiceLocator
import com.dailysketch.app.core.navigation.Navigator
import com.dailysketch.app.databinding.ActivityMainBinding
import com.dailysketch.app.presentation.splash.SplashFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            Navigator.showRoot(
                supportFragmentManager,
                SplashFragment(),
                Navigator.TAG_SPLASH
            )
        }
    }
}
