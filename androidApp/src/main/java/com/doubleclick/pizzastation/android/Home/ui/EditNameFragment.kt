package com.doubleclick.pizzastation.android.Home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentEditNameBinding
import com.doubleclick.pizzastation.android.model.ImageResponseCallback
import com.doubleclick.pizzastation.android.model.MessageCallback
import com.doubleclick.pizzastation.android.model.PhoneNumber
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.utils.SessionManger.setPhone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By Eslam Ghazy on 1/2/2023
 */
class EditNameFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditNameBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNameBinding.inflate(inflater, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.editBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.getImageResponseModel(
                    "Bearer " + SessionManger.getToken(
                        requireActivity()
                    )
                )
                    .observe(viewLifecycleOwner) {
                        it.clone()
                            .enqueue(object : Callback<ImageResponseCallback> {
                                override fun onResponse(
                                    call: Call<ImageResponseCallback>,
                                    response: Response<ImageResponseCallback>
                                ) {
                                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                        sendId(response.body()?.data?.get(0)?.id)
                                        response.body()?.data?.get(0)?.phone_number?.let { phone ->
                                            setPhone(
                                                requireActivity(),
                                                phone
                                            )
                                        }
                                    }
                                }

                                override fun onFailure(
                                    call: Call<ImageResponseCallback>,
                                    t: Throwable
                                ) {

                                }

                            })
                    }

            }

        }

    }

    private fun sendId(id: Int?) {
        viewLifecycleOwner.lifecycleScope.launch(
            Dispatchers.Main
        ) {
            try {
                viewModel.editPhone(
                    "Bearer " + SessionManger.getToken(
                        requireActivity()
                    ),
                    id!!.toString(),
                    PhoneNumber(binding.phone.text.toString())
                ).observe(viewLifecycleOwner) {
                    it.clone().enqueue(object :
                        Callback<MessageCallback> {
                        override fun onResponse(
                            call: Call<MessageCallback>,
                            response: Response<MessageCallback>
                        ) {
                            Toast.makeText(
                                requireActivity(),
                                response.body()?.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onFailure(
                            call: Call<MessageCallback>,
                            t: Throwable
                        ) {
                            Log.e(
                                "TAG",
                                "onFailure: ${t.message}"
                            )
                        }

                    })
                }
            } catch (_: NullPointerException) {
                Toast.makeText(
                    requireActivity(),
                    "Some error occur , try again later",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }

}