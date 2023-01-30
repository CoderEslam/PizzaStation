package com.doubleclick.pizzastation.android.Home.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.doubleclick.pizzastation.android.HistoryActivity
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.OffersActivity
import com.doubleclick.pizzastation.android.OrdersActivity
import com.doubleclick.pizzastation.android.databinding.FragmentProfileBinding
import com.doubleclick.pizzastation.android.utils.SessionManger
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick();
    }

    private fun onClick() {
        binding.editPen.setOnClickListener {
            val editNameFragment = EditNameFragment();
            editNameFragment.show(requireActivity().supportFragmentManager, "Edit");
        }

        binding.offerCard.setOnClickListener {
            startActivity(Intent(requireActivity(), OffersActivity::class.java))
        }

        binding.historyCard.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }

        binding.ordersCard.setOnClickListener {
            startActivity(Intent(requireActivity(), OrdersActivity::class.java))
        }
        binding.logoutCard.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                SessionManger.logout(
                    activity as HomeActivity
                )
            }
        }
    }
}