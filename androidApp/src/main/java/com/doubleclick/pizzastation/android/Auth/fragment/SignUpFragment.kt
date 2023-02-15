package com.doubleclick.pizzastation.android.Auth.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.doubleclick.pizzastation.android.Auth.AuthActivity
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentSignUpBinding
import com.doubleclick.pizzastation.android.map.MapsActivity
import com.doubleclick.pizzastation.android.model.Login
import com.doubleclick.pizzastation.android.model.LoginResponse
import com.doubleclick.pizzastation.android.model.Registration
import com.doubleclick.pizzastation.android.model.RegistrationResponse
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: MainViewModel

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
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        onCLick();
    }

    private fun onCLick() {
        binding.signUp.setOnClickListener { view ->
            if (isEmpty()) {
                viewModel.getRegisterResponse(
                    Registration(
                        name = binding.userName.text.toString(),
                        email = binding.email.text.toString(),
                        password = binding.userPassword.text.toString(),
                        password_confirmation = binding.confiremPassword.text.toString()
                    )
                ).observe(viewLifecycleOwner) {
                    it.enqueue(object : Callback<RegistrationResponse> {
                        override fun onResponse(
                            call: Call<RegistrationResponse>,
                            response: Response<RegistrationResponse>
                        ) {
                            Toast.makeText(
                                requireContext(),
                                response.body().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            viewLifecycleOwner.lifecycleScope.launch {
                                SessionManger.updateSession(
                                    activity as AuthActivity,
                                    response.body()!!.device_token,
                                    response.body()!!.user.id.toString(),
                                    binding.userPassword.text.toString(),
                                    response.body()!!.user.email,
                                    response.body()!!.user.name
                                )
                            }
                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        }

                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
                }
            } else {
                Snackbar.make(
                    view,
                    resources.getString(R.string.your_password_or_email_is_wrong),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun isEmpty(): Boolean =
        binding.email.text.toString().isNotEmpty()
                && binding.userPassword.text.toString().isNotEmpty()
                && binding.userName.text.toString().isNotEmpty()
                && passwordConfirme()

    private fun passwordConfirme(): Boolean =
        binding.userPassword.text.toString() == binding.confiremPassword.text.toString()

}