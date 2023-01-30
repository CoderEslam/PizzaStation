package com.doubleclick.pizzastation.android.Auth.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.doubleclick.pizzastation.android.Auth.AuthActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentSignInBinding
import com.doubleclick.pizzastation.android.databinding.SignInBinding
import com.doubleclick.pizzastation.android.map.MapsActivity
import com.doubleclick.pizzastation.android.model.Login
import com.doubleclick.pizzastation.android.model.LoginResponse
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInFragment : Fragment() {

    private lateinit var binding: SignInBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "SignInFragment"

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
        binding = SignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        onClick();


    }

    private fun onClick() {
        binding.signIn.setOnClickListener { view ->
            if (isEmpty()) {
                viewModel.getLoginResponse(
                    Login(
                        email = binding.emailAddress.text.toString(),
                        password = binding.password.text.toString()
                    )
                ).observe(viewLifecycleOwner) {
                    it.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            Toast.makeText(
                                requireContext(),
                                response.body().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            try {
                                viewLifecycleOwner.lifecycleScope.launch {
                                    SessionManger.updateSession(
                                        activity as AuthActivity,
                                        response.body()!!.device_token,
                                        response.body()!!.user.id.toString(),
                                        binding.password.text.toString(),
                                        response.body()!!.user.email,
                                    )
                                }
                            } catch (e: NullPointerException) {
                                Log.e(TAG, "onResponse: ${e.message}")
                            }

                            startActivity(Intent(requireActivity(), MapsActivity::class.java))
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
        binding.forgePassword.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgetPasswordFragment())
        }
    }

    private fun isEmpty(): Boolean =
        binding.emailAddress.text.toString().isNotEmpty()
                && binding.password.text.toString().isNotEmpty()
}