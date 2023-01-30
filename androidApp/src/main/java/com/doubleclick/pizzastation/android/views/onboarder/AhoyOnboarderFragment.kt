package com.doubleclick.pizzastation.android.views.onboarder

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.doubleclick.pizzastation.android.databinding.FragmentAhoyBinding

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
class AhoyOnboarderFragment : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private lateinit var binding: FragmentAhoyBinding

    @StringRes
    private var titleResId = 0

    @ColorRes
    private var titleColor = 0

    @StringRes
    private var descriptionResId = 0

    @ColorRes
    private var backgroundColor = 0

    @ColorRes
    private var descriptionColor = 0

    @RawRes
    private var imageResId = 0
    private var titleTextSize = 0f
    private var descriptionTextSize = 0f
    private var iconHeight = 0
    private var iconWidth = 0
    private var marginTop = 0
    private var marginBottom = 0
    private var marginLeft = 0
    private var marginRight = 0
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAhoyBinding.inflate(inflater, container, false)

        val bundle = arguments
        title = bundle!!.getString(AHOY_PAGE_TITLE, null)
        titleResId = bundle.getInt(AHOY_PAGE_TITLE_RES_ID, 0)
        titleColor = bundle.getInt(AHOY_PAGE_TITLE_COLOR, 0)
        titleTextSize = bundle.getFloat(AHOY_PAGE_TITLE_TEXT_SIZE, 0f)
        description = bundle.getString(AHOY_PAGE_DESCRIPTION, null)
        descriptionResId = bundle.getInt(AHOY_PAGE_DESCRIPTION_RES_ID, 0)
        descriptionColor = bundle.getInt(AHOY_PAGE_DESCRIPTION_COLOR, 0)
        descriptionTextSize = bundle.getFloat(AHOY_PAGE_DESCRIPTION_TEXT_SIZE, 0f)
        imageResId = bundle.getInt(AHOY_PAGE_IMAGE_RES_ID, 0)
        backgroundColor = bundle.getInt(AHOY_PAGE_BACKGROUND_COLOR, 0)
        iconWidth = bundle.getInt(
            AHOY_PAGE_ICON_WIDTH, dpToPixels(
                128,
                activity
            ).toInt()
        )
        iconHeight = bundle.getInt(
            AHOY_PAGE_ICON_HEIGHT, dpToPixels(
                128,
                activity
            ).toInt()
        )
        marginTop = bundle.getInt(
            AHOY_PAGE_MARGIN_TOP, dpToPixels(
                80,
                activity
            ).toInt()
        )
        marginBottom = bundle.getInt(
            AHOY_PAGE_MARGIN_BOTTOM, dpToPixels(
                0,
                activity
            ).toInt()
        )
        marginLeft = bundle.getInt(
            AHOY_PAGE_MARGIN_LEFT, dpToPixels(
                0,
                activity
            ).toInt()
        )
        marginRight = bundle.getInt(
            AHOY_PAGE_MARGIN_RIGHT, dpToPixels(
                0,
                activity
            ).toInt()
        )

        if (title != null) {
            binding.tvTitle!!.text = title
        }
        if (titleResId != 0) {
            binding.tvTitle!!.text = resources.getString(titleResId)
        }
        if (description != null) {
            binding.tvDescription!!.text = description
        }
        if (descriptionResId != 0) {
            binding.tvDescription!!.text = resources.getString(descriptionResId)
        }
        if (titleColor != 0) {
            binding.tvTitle!!.setTextColor(ContextCompat.getColor(requireActivity(), titleColor))
        }
        if (descriptionColor != 0) {
            binding.tvDescription!!.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    descriptionColor
                )
            )
        }
        if (imageResId != 0) {
            binding.lottieAnimationView.setAnimation(imageResId)
//            binding.lottieAnimationView!!.setImageDrawable(
//                ContextCompat.getDrawable(
//                    requireActivity(),
//                    imageResId
//                )
//            )
        }
        if (titleTextSize != 0f) {
            binding.tvTitle!!.textSize = titleTextSize
        }
        if (descriptionTextSize != 0f) {
            binding.tvDescription!!.textSize = descriptionTextSize
        }
        if (backgroundColor != 0) {
            binding.cvCardview!!.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    backgroundColor
                )
            )
        }
        if (iconWidth != 0 && iconHeight != 0) {
            val layoutParams = FrameLayout.LayoutParams(iconWidth, iconHeight)
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
            binding.lottieAnimationView!!.layoutParams = layoutParams
        }
        return binding.root
    }

    private fun dpToPixels(dp: Int, context: Context?): Float {
        return dp * requireContext().resources.displayMetrics.density
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun getCardView(): CardView? {
        return null
    }

    fun getTitleView(): TextView? {
        return null
    }

    fun getDescriptionView(): TextView? {
        return binding.tvDescription
    }

    companion object {
        private const val AHOY_PAGE_TITLE = "ahoy_page_title"
        private const val AHOY_PAGE_TITLE_RES_ID = "ahoy_page_title_res_id"
        private const val AHOY_PAGE_TITLE_COLOR = "ahoy_page_title_color"
        private const val AHOY_PAGE_TITLE_TEXT_SIZE = "ahoy_page_title_text_size"
        private const val AHOY_PAGE_DESCRIPTION = "ahoy_page_description"
        private const val AHOY_PAGE_DESCRIPTION_RES_ID = "ahoy_page_description_res_id"
        private const val AHOY_PAGE_DESCRIPTION_COLOR = "ahoy_page_description_color"
        private const val AHOY_PAGE_DESCRIPTION_TEXT_SIZE = "ahoy_page_description_text_size"
        private const val AHOY_PAGE_IMAGE_RES_ID = "ahoy_page_image_res_id"
        private const val AHOY_PAGE_BACKGROUND_COLOR = "ahoy_page_background_color"
        private const val AHOY_PAGE_ICON_WIDTH = "ahoy_page_icon_width"
        private const val AHOY_PAGE_ICON_HEIGHT = "ahoy_page_icon_height"
        private const val AHOY_PAGE_MARGIN_LEFT = "ahoy_page_margin_left"
        private const val AHOY_PAGE_MARGIN_RIGHT = "ahoy_page_margin_right"
        private const val AHOY_PAGE_MARGIN_TOP = "ahoy_page_margin_top"
        private const val AHOY_PAGE_MARGIN_BOTTOM = "ahoy_page_margin_bottom"
        fun newInstance(card: AhoyOnboarderCard): AhoyOnboarderFragment {
            val args = Bundle()
            args.putString(AHOY_PAGE_TITLE, card.getTitle())
            args.putString(AHOY_PAGE_DESCRIPTION, card.getDescription())
            args.putInt(AHOY_PAGE_TITLE_RES_ID, card.getTitleResourceId())
            args.putInt(AHOY_PAGE_DESCRIPTION_RES_ID, card.getDescriptionResourceId())
            args.putInt(AHOY_PAGE_TITLE_COLOR, card.getTitleColor())
            args.putInt(AHOY_PAGE_DESCRIPTION_COLOR, card.getDescriptionColor())
            args.putInt(AHOY_PAGE_IMAGE_RES_ID, card.getImageResourceId())
            args.putFloat(AHOY_PAGE_TITLE_TEXT_SIZE, card.getTitleTextSize())
            args.putFloat(AHOY_PAGE_DESCRIPTION_TEXT_SIZE, card.getDescriptionTextSize())
            args.putInt(AHOY_PAGE_BACKGROUND_COLOR, card.getBackgroundColor())
            args.putInt(AHOY_PAGE_ICON_HEIGHT, card.getIconHeight())
            args.putInt(AHOY_PAGE_ICON_WIDTH, card.getIconWidth())
            args.putInt(AHOY_PAGE_MARGIN_LEFT, card.getMarginLeft())
            args.putInt(AHOY_PAGE_MARGIN_RIGHT, card.getMarginRight())
            args.putInt(AHOY_PAGE_MARGIN_TOP, card.getMarginTop())
            args.putInt(AHOY_PAGE_MARGIN_BOTTOM, card.getMarginBottom())
            val fragment = AhoyOnboarderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

