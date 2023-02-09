package com.doubleclick.pizzastation.android.Home.ui

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.HistoryActivity
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.OrdersActivity
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentProfileBinding
import com.doubleclick.pizzastation.android.model.ImageResponseCallback
import com.doubleclick.pizzastation.android.model.ImageResponseModel
import com.doubleclick.pizzastation.android.model.MessageCallback
import com.doubleclick.pizzastation.android.model.SandImage
import com.doubleclick.pizzastation.android.utils.SessionManger
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
            val id = SessionManger.getCurrentUserId(requireActivity()).toString()
            val name = SessionManger.getName(requireActivity()).toString()
            Log.e("TAG", "uploadImage: ${imageResponseModel?.user_image}")
            imageResponseModel?.user_image?.let { SessionManger.setImage(requireActivity(), it) }
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
                        Toast.makeText(
                            requireActivity(),
                            response.body()?.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
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
            binding.userName.text = SessionManger.getName(requireActivity())
            binding.email.text = SessionManger.getCurrentEmail(requireActivity())
            Glide.with(requireActivity()).load(
                "http://172.16.0.98/users_images/123456789null.jpg"
            ).into(binding.imageProfile)
        }

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

    fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ", arrayOf(filePath), null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }


}