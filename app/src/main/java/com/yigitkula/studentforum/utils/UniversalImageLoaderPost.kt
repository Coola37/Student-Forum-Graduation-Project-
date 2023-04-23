package com.yigitkula.studentforum.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.yigitkula.studentforum.R

class UniversalImageLoaderPost(val mContext: Context) {

    val config: ImageLoaderConfiguration
        get() {

            val defaultOptions = DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(FadeInBitmapDisplayer(400)).build()
            return ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build()
        }

    companion object {
        private val defaultImage = R.drawable.ic_add
        fun setImage(
            imgURL: String,
            imgView: ImageView,
            progressBar: ProgressBar?,
            initialPath: String
        ) {

            //imgUrl:facebook.com/images/logo.jpeg
            //initialPath:http://

            val imageLoader = ImageLoader.getInstance()
            imageLoader.displayImage(initialPath + imgURL, imgView, object : ImageLoadingListener {
                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    if (progressBar != null)
                        progressBar.visibility = View.VISIBLE


                }

                override fun onLoadingFailed(
                    imageUri: String?,
                    view: View?,
                    failReason: FailReason?
                ) {
                    if (progressBar != null)
                        progressBar.visibility = View.GONE
                }

                override fun onLoadingComplete(
                    imageUri: String?,
                    view: View?,
                    loadedImage: Bitmap?
                ) {
                    if (progressBar != null)
                        progressBar.visibility = View.GONE
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    if (progressBar != null)
                        progressBar.visibility = View.GONE
                }

            })


        }
    }
}