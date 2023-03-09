package com.doubleclick.pizzastation.android.activies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.databinding.ActivityPaymentChooseBinding
import com.doubleclick.pizzastation.android.model.CartModel
import com.doubleclick.pizzastation.android.model.MenuModel

class PaymentChooseActivity : AppCompatActivity() {

    private val TAG = "PaymentChooseActivity"

    private lateinit var binding: ActivityPaymentChooseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.completeOrder.setOnClickListener {
            if (binding.payCard.isChecked) {
                val i = Intent(this@PaymentChooseActivity, PaymentWebViewActivity::class.java)
                i.putExtra("carts", intent.extras?.getSerializable("carts"))
                i.putExtra("total", intent.extras?.getDouble("total"))
                i.putExtra("amount", intent.extras?.getInt("amount"))
                i.putExtra("notes", intent.extras?.getString("notes"))
                i.putExtra("phone", intent.extras?.getString("phone"))
                i.putExtra("address", intent.extras?.getString("address"))
                i.putExtra(
                    "governorateModel",
                    intent.extras?.getSerializable("governorateModel")
                )
                i.putExtra("branchesModel", intent.extras?.getSerializable("branchesModel"))
                startActivity(i)
                finish()
            } else if (binding.payWallet.isChecked) {
                Toast.makeText(this, "Not available now", Toast.LENGTH_SHORT).show()
            } else if (binding.paycash.isChecked) {
                Toast.makeText(this, "Not available now", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You have to choose first", Toast.LENGTH_SHORT).show()
            }
        }

    }
}