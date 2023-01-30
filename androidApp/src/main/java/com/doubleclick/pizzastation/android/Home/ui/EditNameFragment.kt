package com.doubleclick.pizzastation.android.Home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doubleclick.pizzastation.android.databinding.FragmentEditNameBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created By Eslam Ghazy on 1/2/2023
 */
class EditNameFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditNameBinding

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

    }

}