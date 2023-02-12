package com.doubleclick.pizzastation.android.Home.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.OurLocationAdapter
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterGoverorate
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.OnSpinnerEventsListener
import com.doubleclick.pizzastation.android.databinding.FragmentOurBranchsBinding
import com.doubleclick.pizzastation.android.model.BranchesList
import com.doubleclick.pizzastation.android.model.BranchesModel
import com.doubleclick.pizzastation.android.model.GovernorateList
import com.doubleclick.pizzastation.android.model.GovernorateModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Predicate
import java.util.stream.Collectors


class OurBranchsFragment : Fragment() {

    private lateinit var binding: FragmentOurBranchsBinding
    private lateinit var viewModel: MainViewModel
    private var governorateModelList: ArrayList<GovernorateModel> = ArrayList()
    private var branchesModelList: ArrayList<BranchesModel> = ArrayList()
    private var branchesModelListFilter: ArrayList<BranchesModel> = ArrayList()
    private lateinit var menuOptionItemSelectedGovernorateModel: GovernorateModel
    private lateinit var menuOptionItemSelectedBranchesModel: BranchesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOurBranchsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getBranches().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<BranchesList> {
                override fun onResponse(
                    call: Call<BranchesList>,
                    response: Response<BranchesList>
                ) {
                    branchesModelList.addAll(response.body()!!.data)
                }

                override fun onFailure(call: Call<BranchesList>, t: Throwable) {

                }

            })
        }
        viewModel.getGovernorate().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<GovernorateList> {
                override fun onResponse(
                    call: Call<GovernorateList>,
                    response: Response<GovernorateList>
                ) {

                    Log.d("governorateModelList", "onResponse: " + response.body()?.data.toString())
                    governorateModelList.addAll(response.body()!!.data)
                    try {
                        binding.spinnerGovernorate.adapter =
                            SpinnerAdapterGoverorate(requireActivity(), governorateModelList)
                    } catch (e: IllegalStateException) {
                        Log.e("TAG", "onResponse: ${e.message}")
                    }

                }

                override fun onFailure(call: Call<GovernorateList>, t: Throwable) {

                }

            })
        }

        binding.spinnerGovernorate.setSpinnerEventsListener(object : OnSpinnerEventsListener {
            override fun onPopupWindowOpened(spinner: Spinner?) {
                binding.spinnerGovernorate.background =
                    resources.getDrawable(R.drawable.bg_spinner_down)
            }

            override fun onPopupWindowClosed(spinner: Spinner?) {
                binding.spinnerGovernorate.background =
                    resources.getDrawable(R.drawable.bg_spinner_up)
            }

        })

        binding.spinnerGovernorate.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ) {
                menuOptionItemSelectedGovernorateModel = governorateModelList[i]
                selectArea()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                menuOptionItemSelectedGovernorateModel = governorateModelList[0]
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectArea() {
        val byGovernorate: Predicate<BranchesModel> =
            Predicate<BranchesModel> { branch -> branch.government_id == menuOptionItemSelectedGovernorateModel.id }
        binding.rvOurBranshes.adapter = OurLocationAdapter(
            branchesModelList.stream().filter(byGovernorate).collect(Collectors.toList())
        )

    }

}