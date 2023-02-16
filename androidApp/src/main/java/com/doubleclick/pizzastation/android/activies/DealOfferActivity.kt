package com.doubleclick.pizzastation.android.activies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.databinding.ActivityDealOfferBinding

class DealOfferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDealOfferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}