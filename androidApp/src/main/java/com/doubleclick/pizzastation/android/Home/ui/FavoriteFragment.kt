package com.doubleclick.pizzastation.android.Home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.FavoriteAdapter
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.OnFavoriteCheckedItem
import com.doubleclick.pizzastation.android.databinding.FragmentFavoriteBinding
import com.doubleclick.pizzastation.android.model.FavoriteModel
import com.doubleclick.pizzastation.android.model.FavoriteModelList
import com.doubleclick.pizzastation.android.model.MessageCallback
import com.doubleclick.pizzastation.android.utils.SessionManger
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment(), OnFavoriteCheckedItem {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: MainViewModel
    private var favoriteModelList: ArrayList<FavoriteModel> = ArrayList()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val TAG = "FavoriteFragment"
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
                            favoriteModelList = response.body()?.data as ArrayList<FavoriteModel>
                            favoriteAdapter = FavoriteAdapter(
                                favoriteModelList,
                                requireActivity() as HomeActivity,
                                this@FavoriteFragment
                            )
                            binding.rvFavorite.adapter = favoriteAdapter

                        }

                        override fun onFailure(call: Call<FavoriteModelList>, t: Throwable) {

                        }

                    });
                }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onFavoriteChecked(postion: Int, favoriteModel: FavoriteModel) {
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.deleteFavorite(
                "Bearer " + SessionManger.getToken(requireActivity()),
                favoriteModel.id.toString()
            )
                .observe(viewLifecycleOwner) {
                    try {
                        //https://square.github.io/retrofit/2.x/retrofit/retrofit2/Call.html
                        //https://stackoverflow.com/questions/52101253/how-to-make-multiple-calls-with-retrofit
                        it.clone().enqueue(object : Callback<MessageCallback> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<MessageCallback>,
                                response: Response<MessageCallback>
                            ) {
                                try {
                                    if (favoriteModelList.size == 1) {
                                        favoriteModelList.removeAt(0);
                                        favoriteAdapter.notifyItemRemoved(0)
                                        favoriteAdapter.notifyItemRangeChanged(
                                            0,
                                            favoriteModelList.size
                                        )
                                        favoriteAdapter.notifyDataSetChanged()
                                    }
                                    favoriteModelList.removeAt(postion)
                                    favoriteAdapter.notifyItemRemoved(postion)
                                    favoriteAdapter.notifyItemRangeChanged(
                                        0,
                                        favoriteModelList.size
                                    )
                                    favoriteAdapter.notifyDataSetChanged()
                                    /////////////refresh fragment//////////////////
                                    context?.let {
                                        val fragmentManger =
                                            (context as? AppCompatActivity)?.supportFragmentManager
                                        fragmentManger?.let {
                                            val currentFragment =
                                                fragmentManger.findFragmentById(R.id.main_nav);
                                            currentFragment?.let {
                                                val fragmentTransaction =
                                                    fragmentManger.beginTransaction();
                                                fragmentTransaction.detach(it);
                                                fragmentTransaction.attach(it);
                                                fragmentTransaction.commit();
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "Done",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                    /////////////refresh fragment//////////////////
                                } catch (e: IndexOutOfBoundsException) {
                                    Log.e(TAG, "IndexOutOfBoundsException: ${e.message}")
                                }

                            }

                            override fun onFailure(call: Call<MessageCallback>, t: Throwable) {

                            }

                        })
                    } catch (e: IllegalStateException) {
                        Log.e(TAG, "IllegalStateException: ${e.message}")
                    }
                }
        }
    }


}