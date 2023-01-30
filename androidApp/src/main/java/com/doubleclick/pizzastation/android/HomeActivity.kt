package com.doubleclick.pizzastation.android

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.doubleclick.pizzastation.android.databinding.ActivityHomeBinding
import androidx.navigation.ui.setupActionBarWithNavController
import com.doubleclick.pizzastation.android.databinding.NoInternetConnectionBinding
import com.doubleclick.pizzastation.android.utils.isNetworkConnected
import com.doubleclick.pizzastation.android.views.curvedBottomNavigation.CbnMenuItem
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var networkBinding: NoInternetConnectionBinding

    @RequiresApi(Build.VERSION_CODES.M)
    private var net: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        net = isNetworkConnected(this)
        if (net) {
            binding = ActivityHomeBinding.inflate(layoutInflater)
            setContentView(binding.root)
            buttonNavigation(savedInstanceState);
        } else {
            networkBinding = NoInternetConnectionBinding.inflate(layoutInflater)
            setContentView(networkBinding.root)
            networkBinding.tryAgain.setOnClickListener {
                //https://stackoverflow.com/questions/3053761/reload-activity-in-android
                finish()
                startActivity(intent)
            }
        }

    }

    private fun buttonNavigation(savedInstanceState: Bundle?) {
        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 2
        val navController = findNavController(R.id.nav_home_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_cart,
                R.id.navigation_layout,
                R.id.navigation_profile,
                R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        val menuItems = arrayOf(
            CbnMenuItem(
                R.drawable.favorite,
                R.drawable.favorite,
                R.id.navigation_favorite
            ),
            CbnMenuItem(
                R.drawable.ic_map_fill,
                R.drawable.ic_map_fill,
                R.id.navigation_location
            ),
            CbnMenuItem(
                R.drawable.home,
                R.drawable.home,
                R.id.navigation_home
            ),

            CbnMenuItem(
                R.drawable.shopping_cart,
                R.drawable.shopping_cart,
                R.id.navigation_cart
            ),
            CbnMenuItem(
                R.drawable.person,
                R.drawable.person,
                R.id.navigation_profile
            ),
        )

        binding.navView.setMenuItems(menuItems, activeIndex)
        binding.navView.setupWithNavController(navController)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSaveInstanceState(outState: Bundle) {
        if (net) {
            outState.putInt("activeIndex", binding.navView.getSelectedIndex())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}