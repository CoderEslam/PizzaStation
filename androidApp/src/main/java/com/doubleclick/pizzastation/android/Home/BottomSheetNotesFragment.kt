package com.doubleclick.pizzastation.android.Home

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.doubleclick.pizzastation.android.`interface`.SendNotes
import com.doubleclick.pizzastation.android.databinding.FragmentBottomSheetBinding
import com.doubleclick.pizzastation.android.databinding.FragmentNotesBottomSheetBinding
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.views.superbottomsheet.SuperBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By Eslam Ghazy on 1/25/2023
 */
class BottomSheetNotesFragment(private val sendNotes: SendNotes) : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentNotesBottomSheetBinding
    private val TAG = "BottomSheetFragment"

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

        binding.notes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                sendNotes.onTextNote(char.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.Done.setOnClickListener {
            sendNotes.onTextNote(binding.notes.text.toString())
        }

    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED
}