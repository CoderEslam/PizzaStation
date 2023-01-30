package com.doubleclick.pizzastation.android.Home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.MenuAdapter
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentBottomSheetBinding
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.views.superbottomsheet.SuperBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By Eslam Ghazy on 1/25/2023
 */
class BottomSheetFragment(private val query: String) : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "BottomSheetFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getSearchMenu(query).observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(
                    call: Call<MenuList>,
                    response: Response<MenuList>
                ) {
                    try {
                        binding.searchFilterRv.apply {
                            adapter = MenuAdapter(response.body()!!.data)
                        }
                    } catch (e: NullPointerException) {
                        Log.e(TAG, "onResponse: ${e.message}")
                    }

                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {

                }

            })
        }


    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED
}