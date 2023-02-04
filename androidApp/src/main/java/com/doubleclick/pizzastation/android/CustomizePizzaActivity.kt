package com.doubleclick.pizzastation.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doubleclick.pizzastation.android.databinding.ActivityCustomizePizzaBinding


class CustomizePizzaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePizzaBinding
    private var uri1: Uri? = null;
    private var uri2: Uri? = null;
    private var uri3: Uri? = null;
    private var uri4: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.quarter1.setOnClickListener {
            openGellry()
        }
        binding.quarter2.setOnClickListener {
            openGellry()
        }
        binding.quarter3.setOnClickListener {
            openGellry()
        }
        binding.quarter4.setOnClickListener {
            openGellry()
        }

    }

    fun openGellry() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1000)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            if (uri1 == null) {
                uri1 = data.data!!
                binding.quarter1.setImageURI(uri1)
            }
            if (uri2 == null) {
                uri2 = data.data!!
                binding.quarter2.setImageURI(uri2)
            }
            if (uri3 == null) {
                uri3 = data.data!!
                binding.quarter3.setImageURI(uri3)
            }
            if (uri4 == null) {
                uri4 = data.data!!
                binding.quarter4.setImageURI(uri4)
            }

        }
    }

}