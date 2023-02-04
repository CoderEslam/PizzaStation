package com.doubleclick.pizzastation.android.Home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.HomeAdapter
import com.doubleclick.pizzastation.android.CustomizePizzaActivity
import com.doubleclick.pizzastation.android.Home.BottomSheetFragment
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentHomeBinding
import com.doubleclick.pizzastation.android.`interface`.OpenSearchView
import com.doubleclick.pizzastation.android.`interface`.itemListener
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.views.SimpleSearchView.SimpleSearchView
import com.doubleclick.pizzastation.android.views.SimpleSearchView.utils.DimensUtils.convertDpToPx
import com.doubleclick.pizzastation.android.views.imageslider.constants.ScaleTypes
import com.doubleclick.pizzastation.android.views.imageslider.models.SlideModel
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), itemListener, OpenSearchView {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "APIHOMEFRAGMENT"
    private var listAutoComplete: ArrayList<String> = ArrayList()

    private var isSearch = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getCategory().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<CategoryList> {
                override fun onResponse(
                    call: Call<CategoryList>,
                    response: Response<CategoryList>
                ) {
                    try {
                        loadMenu(response.body()!!.data);
                    } catch (e: NullPointerException) {
                    }
                }

                override fun onFailure(call: Call<CategoryList>, t: Throwable) {

                }

            })
        }
        viewModel.getOffers().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<OffersList> {
                override fun onResponse(call: Call<OffersList>, response: Response<OffersList>) {
                    try {
                        binding.imageSlider.setImageList(
                            response.body()!!.data,
                            ScaleTypes.FIT
                        )
                    } catch (e: NullPointerException) {
                        Log.e(TAG, "onResponse: ${e.message}")
                    }

                }

                override fun onFailure(call: Call<OffersList>, t: Throwable) {
                }

            })
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(requireActivity(), CustomizePizzaActivity::class.java))
            if (isSearch) {
                setupSearchView();
                isSearch = false
            } else {
                searchView.onBackPressed()
                isSearch = true
            }
        }

    }


    private fun setupSearchView() = with(binding) {
        searchView.setMenuItem(this@HomeFragment)
        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                try {
                    val sheet = BottomSheetFragment(query)
                    sheet.show(
                        requireActivity().supportFragmentManager,
                        "BottomSheetFragment"
                    )
                } catch (e: NullPointerException) {
                    Log.e(TAG, "onResponse: ${e.message}")
                }
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                Log.d(TAG, "onQueryTextCleared: ")
                return false
            }
        })

        // Adding padding to the animation because of the hidden menu item
        val revealCenter = binding.searchView.revealAnimationCenter
        revealCenter!!.x -= convertDpToPx(40, requireContext())
    }


    override fun isOpenSearchView(isSearch: Boolean) {
        this.isSearch = isSearch
    }

    override fun mListener(postion: String) {
        viewModel.getMenuFilter(postion)
    }

    private fun loadMenu(categoryModel: List<CategoryModel>) {
        viewModel.getMenu().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(
                    call: Call<MenuList>,
                    response: Response<MenuList>
                ) {
                    classification(categoryModel = categoryModel, menus = response.body()!!.data)
                    Log.e(TAG, "onResponse: ${response.body()!!.toString()}")

                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

    private fun classification(categoryModel: List<CategoryModel>, menus: List<MenuModel>) {
        val listParent: ArrayList<CategoricalMenu> = ArrayList()
        for (categoryName in categoryModel) {
            val child: ArrayList<MenuModel> = ArrayList()
            for (menu in menus) {
                listAutoComplete.add(menu.name!!)
                if (categoryName.category == menu.category) {
                    child.add(menu)
                }
            }
            if (categoryName.category != "Extra") {
                listParent.add(CategoricalMenu(categoryName, child))
            }
        }
        binding.animationView.visibility = View.GONE
        binding.itemMenuRv.adapter = HomeAdapter(listParent)
        binding.searchView.setAutoComplete(listAutoComplete)

    }
}