package com.doubleclick.pizzastation.android.liquidswipe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.doubleclick.pizzastation.android.Auth.AuthActivity
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.MainActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.databinding.FragmentSplashBinding
import com.doubleclick.pizzastation.android.map.MapsActivity
import com.doubleclick.pizzastation.android.utils.SessionManger
import kotlinx.coroutines.launch

private const val ARG_DOTES = "ARG_DOTES"
private const val ARG_ANIMATION = "ARG_ANIMATION"
private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_POSITION = "ARG_POSITION"

class SplashFragment : Fragment() {
    private var dotes: Int? = null
    private var lotteAnimation: Int? = null
    private var position: Int? = null
    private var title: String? = null
    private lateinit var binding: FragmentSplashBinding

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dotes = it.getInt(ARG_DOTES)
            lotteAnimation = it.getInt(ARG_ANIMATION)
            position = it.getInt(ARG_POSITION)
            title = it.getString(ARG_TITLE)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                if (SessionManger.getCurrentEmail(activity as MainActivity)
                        ?.isNotEmpty() == true
                    &&
                    SessionManger.getToken(activity as MainActivity)?.isNotEmpty() == true
                ) {
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()
                }
            }
            background = resources.getDrawable(R.drawable.bg)
            binding.dots.setImageDrawable(resources.getDrawable(dotes!!))
            binding.lottieAnimationView.setAnimation(lotteAnimation!!)
            binding.title.text = title
            /////////////////////////////////////////////////////////
            binding.title.setOnClickListener {
                startActivity(Intent(requireContext(), HomeActivity::class.java));
            }
            /////////////////////////////////////////////////////////
            if (position == 3) {
                binding.llStart.visibility = View.VISIBLE
                binding.startBtn.setOnClickListener {
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                }
            } else {
                binding.llStart.visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(dots: Int, anim: Int, title: String, position: Int) =
            SplashFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DOTES, dots)
                    putInt(ARG_ANIMATION, anim)
                    putString(ARG_TITLE, title)
                    putInt(ARG_POSITION, position)
                }
            }
    }
}
