package com.doubleclick.pizzastation.android.Home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doubleclick.pizzastation.android.Adapter.FavoriteAdapter
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavorite.apply {
            adapter = FavoriteAdapter(requireActivity() as HomeActivity)
        }
    }
}