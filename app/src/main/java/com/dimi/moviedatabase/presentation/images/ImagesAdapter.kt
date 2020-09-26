package com.dimi.moviedatabase.presentation.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.framework.network.NetworkConstants
import kotlinx.android.synthetic.main.layout_image_view_in_view_group.view.*

class ImagesAdapter
constructor(
    private val requestManager: RequestManager
) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private var _fullScreenMode = MutableLiveData(false)

    val fullScreenMode : LiveData<Boolean>
        get() = _fullScreenMode

    private val _diffCallback = object : DiffUtil.ItemCallback<Image>() {

        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            ImageRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(_diffCallback).build()
        )

    internal inner class ImageRecyclerChangeCallback(
        private val adapter: ImagesAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_image_view_in_view_group,
                parent,
                false
            ),
            requestManager
        )
    }

    fun changeFullScreenMode() {
        _fullScreenMode.value = !_fullScreenMode.value!!
        notifyItemRangeChanged(0, itemCount, null)
    }

    fun getItem(position: Int): Image {
        return differ.currentList[position]
    }

    fun setList(list: List<Image>): Int {
        differ.submitList(list)
        return list.size
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
        holder.bind(differ.currentList[position], _fullScreenMode.value!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ViewHolder(
        itemView: View,
        val requestManager: RequestManager,
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(image: Image, fullScreenMode: Boolean) {

            if (fullScreenMode)
                itemView.image.scaleType = ImageView.ScaleType.CENTER_CROP
            else
                itemView.image.scaleType = ImageView.ScaleType.FIT_CENTER

            requestManager
                .load(NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + image.filePath)
                .into(itemView.image)
        }
    }
}