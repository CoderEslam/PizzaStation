package com.doubleclick.pizzastation.android.Home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.HomeAdapter
import com.doubleclick.pizzastation.android.activies.CustomizePizzaActivity
import com.doubleclick.pizzastation.android.Home.BottomSheetFragment
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentHomeBinding
import com.doubleclick.pizzastation.android.`interface`.OpenSearchView
import com.doubleclick.pizzastation.android.`interface`.itemListener
import com.doubleclick.pizzastation.android.activies.CustomSlicePizzaActivity
import com.doubleclick.pizzastation.android.activies.DealOfferActivity
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.ItemDecoration
import com.doubleclick.pizzastation.android.views.HiveLayoutManger.HiveLayoutManager
import com.doubleclick.pizzastation.android.views.SimpleSearchView.SimpleSearchView
import com.doubleclick.pizzastation.android.views.SimpleSearchView.utils.DimensUtils.convertDpToPx
import com.doubleclick.pizzastation.android.views.imageslider.constants.ScaleTypes
import com.doubleclick.pizzastation.android.views.imageslider.interfaces.ItemClickListener
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), itemListener, OpenSearchView {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "API HOME FRAGMENT"
    private var listAutoComplete: ArrayList<String> = ArrayList()
    private lateinit var set: Set<String>

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
                        ).setItemClickListener(object : ItemClickListener {
                            override fun onItemSelected(position: Int, offersModel: OffersModel) {
                                offersModel.offer_group = "1"
                                if (offersModel.offer_group == "1") {
                                    val intent =
                                        Intent(
                                            requireActivity(),
                                            CustomSlicePizzaActivity::class.java
                                        )
                                    intent.putExtra("offersModel", offersModel)
                                    startActivity(intent)
                                } else {
                                    val intent =
                                        Intent(
                                            requireActivity(),
                                            CustomSlicePizzaActivity::class.java
                                        )
                                    intent.putExtra("offersModel", offersModel)
                                    startActivity(intent)
                                }
                            }

                        })
                    } catch (e: NullPointerException) {
                        Log.e(TAG, "onResponse: ${e.message}")
                    }

                }

                override fun onFailure(call: Call<OffersList>, t: Throwable) {
                }

            })
        }

        binding.fab.setOnClickListener {
//            val sender = Sender(Data(""), "")
//            RetrofitInstanceFCM.api.sendNotification(sender).enqueue(object : Callback<MyResponse> {
//                override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
//                    if (response.code() == 200) {
//                        if (response.body()!!.success != 1) {
//                            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<MyResponse>, t: Throwable) {
//
//                }
//
//
//            })
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
                    BottomSheetFragment(query).show(
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
        try {
            viewModel.getMenu().observe(viewLifecycleOwner) {
                it.enqueue(object : Callback<MenuList> {
                    override fun onResponse(
                        call: Call<MenuList>,
                        response: Response<MenuList>
                    ) {
                        classification(
                            categoryModel = categoryModel,
                            menus = response.body()!!.data
                        )
                        Log.e(TAG, "onResponse: ${response.body()!!.toString()}")
                    }

                    override fun onFailure(call: Call<MenuList>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                    }

                })
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "loadMenu: ${e.message}")
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
        // to not repeat items on listAutoComplete
        set = HashSet<String>(listAutoComplete)
        listAutoComplete.clear()
        listAutoComplete.addAll(set)
        binding.searchView.setAutoComplete(listAutoComplete)

    }
}