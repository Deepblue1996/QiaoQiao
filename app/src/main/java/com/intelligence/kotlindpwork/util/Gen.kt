package com.intelligence.kotlindpwork.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.intelligence.kotlindpwork.R

/**
 * Class -
 *
 *
 * Created by Deepblue on 2019/2/27 0027.
 */
class Gen {

    companion object {

        fun show(context: Context?, url: String?, imageView: ImageView?) {
            if (context != null) {
                if (imageView != null) {
                    Glide.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions().crossFade(200))
                        .apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .placeholder(R.drawable.gen_bg)
                        )
                        .into(imageView)
                }
            }
        }

        fun showFit(context: Context?, url: String?, imageView: ImageView?) {
            if (context != null) {
                if (imageView != null) {
                    Glide.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions().crossFade(200))
                        .apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fitCenter()
                                .placeholder(R.drawable.gen_bg)
                        )
                        .into(imageView)
                }
            }
        }

        fun showBlackBg(context: Context?, url: String?, imageView: ImageView?) {
            if (context != null) {
                if (imageView != null) {
                    Glide.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions().crossFade(200))
                        .apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .placeholder(R.drawable.black_bg)
                        )
                        .into(imageView)
                }
            }
        }

        fun showFitBlackBg(context: Context?, url: String?, imageView: ImageView?) {
            if (context != null) {
                if (imageView != null) {
                    Glide.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions().crossFade(200))
                        .apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fitCenter()
                                .placeholder(R.drawable.black_bg)
                        )
                        .into(imageView)
                }
            }
        }

    }
}
