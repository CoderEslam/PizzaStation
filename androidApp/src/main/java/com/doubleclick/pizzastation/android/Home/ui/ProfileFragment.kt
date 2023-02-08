package com.doubleclick.pizzastation.android.Home.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.coroutines.launch
import java.io.File


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var uri: Uri

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

    fun openImage() {
        val intent = Intent();
        intent.type = "image/*";
        intent.action = Intent.ACTION_GET_CONTENT;
        startActivityForResult(intent, 1000);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            uri = data.data!!
            val filePath = SiliCompressor.with(requireActivity()).compress(
                uri.toString(),
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/Pizza/Images/"
                )
            )
        }
    }


}