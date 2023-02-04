package com.doubleclick.pizzastation.android.Home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.*
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterBranches
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterGoverorate
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.OnSpinnerEventsListener
import com.doubleclick.pizzastation.android.`interface`.SendNotes
import com.doubleclick.pizzastation.android.databinding.FragmentNotesBottomSheetBinding
import com.doubleclick.pizzastation.android.model.BranchesList
import com.doubleclick.pizzastation.android.model.BranchesModel
import com.doubleclick.pizzastation.android.model.GovernorateList
import com.doubleclick.pizzastation.android.model.GovernorateModel
import com.doubleclick.pizzastation.android.views.superbottomsheet.SuperBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Predicate
import java.util.stream.Collectors


/**
 * Created By Eslam Ghazy on 1/25/2023
 */
class BottomSheetNotesFragment(private val sendNotes: SendNotes) : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentNotesBottomSheetBinding
    private val TAG = "BottomSheetFragment"
    private lateinit var viewModel: MainViewModel
    private lateinit var menuOptionItemSelectedGovernorateModel: GovernorateModel
    private lateinit var menuOptionItemSelectedBranchesModel: BranchesModel
    private var governorateModelList: List<GovernorateModel> = ArrayList()
    private var branchesModelList: List<BranchesModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNotesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.notes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                sendNotes.onTextNote(char.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        viewModel.getBranches().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<BranchesList> {
                override fun onResponse(
                    call: Call<BranchesList>,
                    response: Response<BranchesList>
                ) {
                    branchesModelList = response.body()!!.data
                }

                override fun onFailure(call: Call<BranchesList>, t: Throwable) {

                }

            })
        }

        binding.spinnerArea.setSpinnerEventsListener(object :
            OnSpinnerEventsListener {
            override fun onPopupWindowOpened(spinner: Spinner?) {
                binding.spinnerArea.background =
                    resources.getDrawable(R.drawable.bg_spinner_down)
            }

            override fun onPopupWindowClosed(spinner: Spinner?) {
                binding.spinnerArea.background = resources.getDrawable(R.drawable.bg_spinner_up)
            }

        })


        viewModel.getGovernorate().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<GovernorateList> {
                override fun onResponse(
                    call: Call<GovernorateList>,
                    response: Response<GovernorateList>
                ) {
                    governorateModelList = response.body()!!.data

                    val adapter = SpinnerAdapterGoverorate(
                        requireActivity(),
                        governorateModelList
                    )
                    binding.spinnerGovernorate.adapter = adapter
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
                            selectArea();
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {
                            menuOptionItemSelectedGovernorateModel = governorateModelList[0]
                        }
                    }

                }

                override fun onFailure(call: Call<GovernorateList>, t: Throwable) {

                }

            })
        }


        binding.completeOrder.setOnClickListener {
            sendNotes.onTextNote(binding.notes.text.toString())
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectArea() {

        val byGovernorate: Predicate<BranchesModel> =
            Predicate<BranchesModel> { branch -> branch.government_id == menuOptionItemSelectedGovernorateModel.id }

        binding.spinnerArea.adapter = SpinnerAdapterBranches(
            requireActivity(),
            /*https://zetcode.com/java/filterlist*/
            branchesModelList.stream().filter(byGovernorate)
                .collect(Collectors.toList())
        )
        binding.spinnerArea.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ) {
                menuOptionItemSelectedBranchesModel = branchesModelList[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                menuOptionItemSelectedBranchesModel = branchesModelList[0]
            }
        }
    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED

}