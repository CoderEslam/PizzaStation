package com.doubleclick.pizzastation.android.views.imageslider.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.OffersModel
import com.doubleclick.pizzastation.android.utils.Constants.OFFERS_URL
import com.doubleclick.pizzastation.android.views.imageslider.constants.ActionTypes
import com.doubleclick.pizzastation.android.views.imageslider.constants.ScaleTypes
import com.doubleclick.pizzastation.android.views.imageslider.interfaces.ItemClickListener
import com.doubleclick.pizzastation.android.views.imageslider.interfaces.TouchListener
import com.doubleclick.pizzastation.android.views.imageslider.models.SlideModel
import com.doubleclick.pizzastation.android.views.imageslider.transformation.RoundedTransformation
import com.squareup.picasso.Picasso


class ViewPagerAdapter(
    context: Context?,
    imageList: List<OffersModel>,
    private var radius: Int,
    private var errorImage: Int,
    private var placeholder: Int,
    private var titleBackground: Int,
    private var scaleType: ScaleTypes?,
    private var textAlign: String
) : PagerAdapter() {

    private val TAG = "ViewPagerAdapter"

    constructor(
        context: Context,
        imageList: List<OffersModel>,
        radius: Int,
        errorImage: Int,
        placeholder: Int,
        titleBackground: Int,
        textAlign: String
    ) :
            this(
                context,
                imageList,
                radius,
                errorImage,
                placeholder,
                titleBackground,
                null,
                textAlign
            )


    private var imageList: List<OffersModel>? = imageList
    private var layoutInflater: LayoutInflater? =
        context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    private var itemClickListener: ItemClickListener? = null
    private var touchListener: TouchListener? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return imageList!!.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val itemView = layoutInflater!!.inflate(R.layout.pager_row, container, false)

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_layout)
        val textView: TextView = itemView.findViewById(R.id.text_view)

        if (imageList!![position].offer_name != null) {
            textView.text = imageList!![position].offer_name
            linearLayout.setBackgroundResource(titleBackground)
            textView.gravity = getGravityFromAlign(textAlign)
            linearLayout.gravity = getGravityFromAlign(textAlign)
        } else {
            linearLayout.visibility = View.INVISIBLE
        }

        try {
            // Image from url or local path check.
            val loader = Picasso.get().load(OFFERS_URL + imageList!![position].offer_image!!)

            // set Picasso options.
            if ((scaleType != null && scaleType == ScaleTypes.CENTER_CROP) || imageList!![position].scaleType == ScaleTypes.CENTER_CROP) {
                loader.fit().centerCrop()
            } else if ((scaleType != null && scaleType == ScaleTypes.CENTER_INSIDE) || imageList!![position].scaleType == ScaleTypes.CENTER_INSIDE) {
                loader.fit().centerInside()
            } else if ((scaleType != null && scaleType == ScaleTypes.FIT) || imageList!![position].scaleType == ScaleTypes.FIT) {
                loader.fit()
            }

            loader.transform(RoundedTransformation(radius, 0))
                .placeholder(placeholder)
                .error(errorImage)
                .into(imageView)
        } catch (e: NullPointerException) {
            Log.e(TAG, "instantiateItem: ${e.message}")
        }

        container.addView(itemView)

        imageView.setOnClickListener {
            itemClickListener?.onItemSelected(
                position,
                imageList!![position]
            )
        }

        if (touchListener != null) {
            imageView!!.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> touchListener!!.onTouched(ActionTypes.MOVE)
                    MotionEvent.ACTION_DOWN -> touchListener!!.onTouched(ActionTypes.DOWN)
                    MotionEvent.ACTION_UP -> touchListener!!.onTouched(ActionTypes.UP)
                }
                false
            }
        }


        return itemView
    }

    /**
     * Get layout gravity value from textAlign variable
     *
     * @param  textAlign  text align by user
     */
    fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "RIGHT" -> {
                Gravity.RIGHT
            }
            "CENTER" -> {
                Gravity.CENTER
            }
            else -> {
                Gravity.LEFT
            }
        }
    }

    /**
     * Set item click listener
     *
     * @param  itemClickListener  callback by user
     */
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    /**
     * Set touch listener for listen to image touch
     *
     * @param  touchListener  interface callback
     */
    fun setTouchListener(touchListener: TouchListener) {
        this.touchListener = touchListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}