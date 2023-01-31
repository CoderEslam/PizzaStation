package com.doubleclick.pizzastation.android.Home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.FavoriteAdapter
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentFavoriteBinding
import com.doubleclick.pizzastation.android.model.FavoriteModelList
import com.doubleclick.pizzastation.android.utils.SessionManger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getFavorite("Bearer " + SessionManger.getToken(requireActivity()))
                .observe(viewLifecycleOwner) {
                    it.enqueue(object : Callback<FavoriteModelList> {
                        override fun onResponse(
                            call: Call<FavoriteModelList>,
                            response: Response<FavoriteModelList>
                        ) {
                            binding.rvFavorite.apply {
                                adapter = response.body()?.data?.let { it1 ->
                                    FavoriteAdapter(
                                        it1,
                                        requireActivity() as HomeActivity
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<FavoriteModelList>, t: Throwable) {

                        }

                    });
                }
        }

    }
}