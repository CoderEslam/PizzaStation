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
import com.doubleclick.pizzastation.android.Adapter.*
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.ItemSizeExtraListener
import com.doubleclick.pizzastation.android.databinding.AlertExtrasLayoutBinding
import com.doubleclick.pizzastation.android.databinding.CustomPizzaFragmentBinding
import com.doubleclick.pizzastation.android.databinding.FragmentBottomSheetBinding
import com.doubleclick.pizzastation.android.model.Extra
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.model.Sizes
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.views.superbottomsheet.SuperBottomSheetFragment
import kotlinx.android.synthetic.main.alert_extras_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By Eslam Ghazy on 1/25/2023
 */
class BottomSheetPopUpExtrasFragment(
    val itemListener: ItemSizeExtraListener,
    val sizes: ArrayList<Sizes>,
    val menuModel: MenuModel?,
    val extrasAdded: MutableList<Extra>? = null
) : SuperBottomSheetFragment() {

    private lateinit var binding: AlertExtrasLayoutBinding
    private val TAG = "BottomSheetPopUpExtrasFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AlertExtrasLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.text = menuModel?.name
        Glide.with(requireActivity()).load(Constants.IMAGE_URL + menuModel?.image)
            .into(binding.imageItem)
        binding.rvExtraSizes.adapter = ItemExtraSizeAdapter(itemListener, sizes, extrasAdded,this)
    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED
}