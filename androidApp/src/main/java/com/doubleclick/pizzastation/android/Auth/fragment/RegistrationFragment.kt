package com.doubleclick.pizzastation.android.Auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.doubleclick.pizzastation.android.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

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
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick();
    }

    private fun onClick() {
        binding.signUp.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToSignUpFragment()
            )
        }
        binding.signIn.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment()
            )
        }
    }
}