package com.doubleclick.pizzastation.android.Home.ui

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.*
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterGoverorate
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.OnSpinnerEventsListener
import com.doubleclick.pizzastation.android.databinding.FragmentProfileBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.utils.SessionManger.getCurrentEmail
import com.doubleclick.pizzastation.android.utils.SessionManger.getCurrentUserId
import com.doubleclick.pizzastation.android.utils.SessionManger.getImage
import com.doubleclick.pizzastation.android.utils.SessionManger.getName
import com.doubleclick.pizzastation.android.utils.SessionManger.getPhone
import com.doubleclick.pizzastation.android.utils.SessionManger.setImage
import com.doubleclick.pizzastation.android.utils.SessionManger.setPhone
import com.doubleclick.pizzastation.android.utils.UploadRequestBody
import com.doubleclick.pizzastation.android.utils.getFileName
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ProfileFragment : Fragment(), UploadRequestBody.UploadCallback {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var uri: Uri
    private lateinit var viewModel: MainViewModel


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            this.uri = uri!!
            val filePath = SiliCompressor.with(requireActivity()).compress(
                uri.toString(),
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/Pizza/Images/"
                )
            )
            binding.imageProfile.setImageURI(Uri.parse(filePath))
            val parcelFileDescriptor =
                requireActivity().contentResolver.openFileDescriptor(
                    Uri.parse(filePath)!!,
                    "r",
                    null
                )
                    ?: return@registerForActivityResult
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file =
                File(
                    requireActivity().cacheDir,
                    requireActivity().contentResolver.getFileName(Uri.parse(filePath)!!)
                )
            binding.progressBar.visibility = View.VISIBLE
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val body = UploadRequestBody(file, "image", this@ProfileFragment)
            sendBody(body)

        }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendBody(body: UploadRequestBody) {
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getImageResponseModel("Bearer " + SessionManger.getToken(requireActivity()))
                .observe(viewLifecycleOwner) {
                    it.clone().enqueue(object : Callback<ImageResponseCallback> {
                        override fun onResponse(
                            call: Call<ImageResponseCallback>,
                            response: Response<ImageResponseCallback>
                        ) {
                            uploadImage(body, response.body()?.data?.get(0))
                        }

                        override fun onFailure(call: Call<ImageResponseCallback>, t: Throwable) {

                        }

                    })
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun uploadImage(body: UploadRequestBody, imageResponseModel: ImageResponseModel?) {
        GlobalScope.launch(Dispatchers.Main) {
            val id = getCurrentUserId(requireActivity()).toString()
            val name = getName(requireActivity()).toString()
            viewModel.uploadImage(
                "Bearer " + SessionManger.getToken(requireActivity()),
                imageResponseModel?.id.toString(),
                MultipartBody.Part.createFormData("image", "$id$name.jpg"/*file.name*/, body)
            ).observe(viewLifecycleOwner) {
                it.enqueue(object : Callback<MessageCallback> {
                    override fun onResponse(
                        call: Call<MessageCallback>,
                        response: Response<MessageCallback>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            binding.userName.text = getName(requireActivity())
                            binding.email.text = getCurrentEmail(requireActivity())
                            Glide.with(requireActivity()).load(
                                "http://172.16.0.98/users_images/${getImage(requireActivity())}.jpg"
                            ).into(binding.imageProfile)
                            setImage(requireActivity(), "$id$name")
                        }
                    }

                    override fun onFailure(call: Call<MessageCallback>, t: Throwable) {
                        Log.d("MultipartBody", "onFailure: ${t.message}")
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                })
            }
        }
    }

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
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            binding.userName.text = getName(requireActivity())
            binding.email.text = getCurrentEmail(requireActivity())
            binding.phone.text = getPhone(requireActivity())
            Glide.with(requireActivity()).load(
                "http://172.16.0.98/users_images/${
                    getImage(requireActivity())
                }.jpg"
            ).into(binding.imageProfile)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.getImageResponseModel(
                    "Bearer " + SessionManger.getToken(
                        requireActivity()
                    )
                )
                    .observe(viewLifecycleOwner) {
                        it.clone()
                            .enqueue(object : Callback<ImageResponseCallback> {
                                override fun onResponse(
                                    call: Call<ImageResponseCallback>,
                                    response: Response<ImageResponseCallback>
                                ) {
                                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                        response.body()?.data?.get(0)?.phone_number?.let { phone ->
                                            setPhone(
                                                requireActivity(),
                                                phone
                                            )
                                        }
                                    }
                                }

                                override fun onFailure(
                                    call: Call<ImageResponseCallback>,
                                    t: Throwable
                                ) {

                                }

                            })
                    }

            }
        }


        /* viewModel.getGovernorate().observe(viewLifecycleOwner) {
             it.clone().enqueue(object : Callback<GovernorateList> {
                 override fun onResponse(
                     call: Call<GovernorateList>,
                     response: Response<GovernorateList>
                 ) {
                     governorateModelList = response.body()!!.data

                     val adapter = SpinnerAdapterGoverorate(
                         requireActivity(),
                         governorateModelList
                     )
                     binding.spinnerGovernorate.adapter = adapter
                     binding.spinnerGovernorate.onItemSelectedListener = object :
                         AdapterView.OnItemSelectedListener {
                         @RequiresApi(Build.VERSION_CODES.N)
                         override fun onItemSelected(
                             adapterView: AdapterView<*>?,
                             view: View,
                             i: Int,
                             l: Long
                         ) {
                             Toast.makeText(requireContext(), "ddsvf", Toast.LENGTH_LONG).show()
                             viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                 viewModel.getImageResponseModel(
                                     "Bearer " + SessionManger.getToken(
                                         requireActivity()
                                     )
                                 )
                                     .observe(viewLifecycleOwner) {
                                         it.clone()
                                             .enqueue(object : Callback<ImageResponseCallback> {
                                                 override fun onResponse(
                                                     call: Call<ImageResponseCallback>,
                                                     response: Response<ImageResponseCallback>
                                                 ) {
                                                     binding.phone.text =
                                                         response.body()?.data?.get(0)?.phone_number
                                                             ?: ""
                                                     viewLifecycleOwner.lifecycleScope.launch(
                                                         Dispatchers.Main
                                                     ) {
                                                         viewModel.editGovernment(
                                                             "Bearer " + SessionManger.getToken(
                                                                 requireActivity()
                                                             ),
                                                             response.body()?.data?.get(0)?.id.toString(),
                                                             GovernmentId(governorateModelList[i].id.toString())
                                                         ).observe(viewLifecycleOwner) {
                                                             it.clone().enqueue(object :
                                                                 Callback<MessageCallback> {
                                                                 override fun onResponse(
                                                                     call: Call<MessageCallback>,
                                                                     response: Response<MessageCallback>
                                                                 ) {
                                                                     Toast.makeText(
                                                                         requireActivity(),
                                                                         response.body()?.message.toString(),
                                                                         Toast.LENGTH_LONG
                                                                     ).show()
                                                                 }

                                                                 override fun onFailure(
                                                                     call: Call<MessageCallback>,
                                                                     t: Throwable
                                                                 ) {
                                                                     Log.e(
                                                                         "TAG",
                                                                         "onFailure: ${t.message}"
                                                                     )
                                                                 }

                                                             })
                                                         }
                                                     }
                                                 }

                                                 override fun onFailure(
                                                     call: Call<ImageResponseCallback>,
                                                     t: Throwable
                                                 ) {

                                                 }

                                             })
                                     }

                             }
                         }

                         override fun onNothingSelected(adapterView: AdapterView<*>?) {
                         }
                     }

                 }

                 override fun onFailure(call: Call<GovernorateList>, t: Throwable) {

                 }

             })
         }*/
    }

    private fun onClick() {
        binding.editPen.setOnClickListener {
            val editNameFragment = EditNameFragment();
            editNameFragment.show(requireActivity().supportFragmentManager, "Edit");
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
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }

        binding.changeImage.setOnClickListener {
            openImage()
        }

        binding.callUsCard.setOnClickListener {
            try {
                val callUri = Uri.parse("tel:01221930858")
                val intentCall = Intent(Intent.ACTION_DIAL, callUri)
                startActivity(intentCall)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "You don't have call app!", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    fun openImage() {
        getContent.launch("image/*")
    }


    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }


}