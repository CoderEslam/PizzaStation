package com.doubleclick.pizzastation.android.activies

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.onboarder.AhoyOnboarderActivity
import com.doubleclick.pizzastation.android.views.onboarder.AhoyOnboarderCard

class GradientBackgroundActivity : AhoyOnboarderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_background)

        val ahoyOnboarderCard1 = AhoyOnboarderCard(
            "City Guide",
            "Detailed guides to help you plan your trip.",
            R.raw.d3_mobile_payment
        )
        val ahoyOnboarderCard2 = AhoyOnboarderCard(
            "Travel Blog",
            "Share your travel experiences with a vast network of fellow travellers.",
            R.raw.delivery_food_splash
        )
        val ahoyOnboarderCard3 = AhoyOnboarderCard(
            "Chat",
            "Connect with like minded people and exchange your travel stories.",
            R.raw.delivery_guy
        )

        ahoyOnboarderCard1.backgroundColor = R.color.black_transparent
        ahoyOnboarderCard2.backgroundColor = R.color.black_transparent
        ahoyOnboarderCard3.backgroundColor = R.color.black_transparent

        val pages: MutableList<AhoyOnboarderCard> = ArrayList()

        pages.add(ahoyOnboarderCard1)
        pages.add(ahoyOnboarderCard2)
        pages.add(ahoyOnboarderCard3)

        for (page in pages) {
            page.titleColor = R.color.white
            page.descriptionColor = R.color.grey_200
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish")
        showNavigationControls(true)
        setGradientBackground()

        //set the button style you created

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button))
        }

        val face = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
        setFont(face)

        setOnboardPages(pages)
    }

    override fun onFinishButtonPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Finish Pressed", Toast.LENGTH_SHORT).show()
    }
}