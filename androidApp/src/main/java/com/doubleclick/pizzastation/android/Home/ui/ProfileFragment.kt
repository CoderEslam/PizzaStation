package com.doubleclick.pizzastation.android.Home.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doubleclick.pizzastation.android.*
import com.doubleclick.pizzastation.android.Home.PaymentActivity
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.FragmentProfileBinding
import com.doubleclick.pizzastation.android.model.ImageResponseCallback
import com.doubleclick.pizzastation.android.model.ImageResponseModel
import com.doubleclick.pizzastation.android.model.MessageCallback
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_PROFILE_URL
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream


class ProfileFragment : Fragment(), UploadRequestBody.UploadCallback {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var uri: Uri
    private lateinit var viewModel: MainViewModel
    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            try {
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

            } catch (e: NullPointerException) {
                Log.e("registerForActivity", "registerForActivityResult: ${e.message}")
            } catch (e: FileNotFoundException) {
                Log.e("registerForActivity", "registerForActivityResult: ${e.message}")
            }
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
                it.clone().enqueue(object : Callback<MessageCallback> {
                    override fun onResponse(
                        call: Call<MessageCallback>,
                        response: Response<MessageCallback>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            binding.userName.text = getName(requireActivity())
                            binding.email.text = getCurrentEmail(requireActivity())
                            setImage(requireActivity(), "$id$name.jpg")
                            Glide.with(requireActivity()).load(
                                "http://172.16.0.98/users_images/${getImage(requireActivity())}"
                            ).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.imageProfile)
                        }
                    }

                    override fun onFailure(call: Call<MessageCallback>, t: Throwable) {
                        Log.d("MultipartBody", "onFailure: ${t.message}")
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
                "$IMAGE_PROFILE_URL${getImage(requireActivity())}"
            ).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageProfile)
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
                                    try {
                                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                            response.body()?.data?.get(0)?.phone_number?.let { phone ->
                                                setPhone(
                                                    requireActivity(),
                                                    phone
                                                )
                                            }
                                            response.body()?.data?.get(0)?.user_image?.let { image ->
                                                setImage(requireActivity(), image)
                                            }
                                        }
                                    } catch (e: IllegalStateException) {
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
            startActivity(Intent(requireActivity(), PaymentActivity::class.java))
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
        binding.imageProfile.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                zoomImageFromThumb(binding.imageProfile, getImage(requireActivity())!!)
            }
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

    private fun openImage() {
        getContent.launch("image/*")
    }


    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }

    private fun zoomImageFromThumb(thumbView: ImageView, image: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        Glide.with(requireActivity()).load(
            IMAGE_PROFILE_URL + image
        ).diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.expandedImage)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
//        thumbView.alpha = 0f

        binding.expandedImage.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        binding.expandedImage.pivotX = 0f
        binding.expandedImage.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    binding.expandedImage,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        binding.expandedImage,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        binding.expandedImage.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        binding.expandedImage,
                        View.X,
                        startBounds.left
                    )
                ).apply {
                    with(ObjectAnimator.ofFloat(binding.expandedImage, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        binding.expandedImage.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        binding.expandedImage.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }

}