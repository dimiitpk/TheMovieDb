package com.dimi.moviedatabase.presentation.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.presentation.GlideApp

class BackDropSlider
constructor(val requestManager: RequestManager) :
    RecyclerView.Adapter<BackDropSlider.ViewHolder>() {

    private var imageList: List<Image> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_image_view,
                parent,
                false
            ),
            requestManager
        )
    }

    fun setList(list: List<Image>) {
        imageList = list
    }

    fun preloadGlideImages(
        list: List<Image>
    ) {
        for (image in list)
            requestManager
                .load(NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + image.filePath)
                .preload()

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(itemView: View, val requestManager: RequestManager) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(image: Image) {
            requestManager
                .load(NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + image.filePath)
                .into((itemView as ImageView))

            itemView.alpha = 0.5f
        }
    }
}