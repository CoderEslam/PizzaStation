package com.doubleclick.pizzastation.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doubleclick.pizzastation.android.databinding.ActivityMainBinding
import com.doubleclick.pizzastation.android.liquidswipe.CustomFragmentPagerAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root);
        binding.viewpager.adapter = CustomFragmentPagerAdapter(supportFragmentManager)
    }
}

