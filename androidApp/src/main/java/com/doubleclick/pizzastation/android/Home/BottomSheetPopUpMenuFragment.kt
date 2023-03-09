package com.doubleclick.pizzastation.android.Home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.Adapter.CustomPizzaItemAdapter
import com.doubleclick.pizzastation.android.Adapter.MenuAdapter
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterSlice
import com.doubleclick.pizzastation.android.Adapter.onPizzaClicked
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.CustomPizzaFragmentBinding
import com.doubleclick.pizzastation.android.databinding.FragmentBottomSheetBinding
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.views.superbottomsheet.SuperBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By Eslam Ghazy on 1/25/2023
 */
class BottomSheetPopUpMenuFragment(
    val setPizza: setImagePizza,
    val image: ImageView,
    val pos: Int,
    var menus: ArrayList<MenuModel>
) :
    SuperBottomSheetFragment() {

    private lateinit var binding: CustomPizzaFragmentBinding
    private val TAG = "BottomSheetFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CustomPizzaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter =
            CustomPizzaItemAdapter(menus)
        binding.rvPizza.adapter = adapter
        adapter.callBack(object : onPizzaClicked {
            override fun onPizza(menu: MenuModel) {
                setPizza.setImage(menu, image, pos)
                dismiss()
            }
        });


    }

    interface setImagePizza {
        fun setImage(menu: MenuModel, image: ImageView, pos: Int)
    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED
}