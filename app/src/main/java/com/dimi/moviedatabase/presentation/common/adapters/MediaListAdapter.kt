package com.dimi.moviedatabase.presentation.common.adapters

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.moviedatabase.BR
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.databinding.LayoutCastListItemBinding
import com.dimi.moviedatabase.databinding.LayoutMediaDetailListItemBinding
import com.dimi.moviedatabase.databinding.LayoutMediaSimpleListItemBinding
import com.dimi.moviedatabase.framework.network.NetworkConstants.SMALL_IMAGE_URL_PREFIX
import com.dimi.moviedatabase.presentation.common.OnDataBindingClickListener
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.util.Constants.LAYOUT_LIST_SPAN_COUNT
import com.dimi.moviedatabase.util.toSimpleString
import kotlinx.android.synthetic.main.layout_cast_list_item.view.*
import kotlinx.android.synthetic.main.layout_media_detail_list_item.view.*
import kotlinx.android.synthetic.main.layout_media_simple_list_item.view.*

class MediaListAdapter<T : Media>(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null,
    private val restoration: Restoration? = null,
    private val resize: Boolean = false,
    private val layout: StaggeredGridLayoutManager? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class RecyclerViewLayoutState {
        DETAIL_LAYOUT_STATE,
        SIMPLE_LAYOUT_STATE,
        DETAIL_LAYOUT_PERSON_STATE
    }

    private val _diffCallback = object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            MovieRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(_diffCallback).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = when (viewType) {
            RecyclerViewLayoutState.DETAIL_LAYOUT_PERSON_STATE.ordinal ->
                LayoutCastListItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false )
            RecyclerViewLayoutState.DETAIL_LAYOUT_STATE.ordinal ->
                LayoutMediaDetailListItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false )
            else ->
                LayoutMediaSimpleListItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false )
        }

        return MediaViewHolder(
            binding,
            interaction = interaction,
            restoration = restoration,
            resize = resize
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is MediaViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    internal inner class MovieRecyclerChangeCallback(
        private val adapter: MediaListAdapter<T>
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

    fun preloadGlideImages(
        list: List<T>
    ) {
//        for (item in list) {
//            if (item.posterPath != null)
//                if (item is Person)
//                    requestManager
//                        .load(SMALL_IMAGE_URL_PREFIX + item.posterPath)
//                        .circleCrop()
//                        .preload()
//                else
//                    requestManager
//                        .load(SMALL_IMAGE_URL_PREFIX + item.posterPath)
//                        .preload()
//        }
    }

    override fun getItemViewType(position: Int): Int {

        layout?.let { layout ->
            return if (layout.spanCount == LAYOUT_LIST_SPAN_COUNT)
                if (differ.currentList[position].mediaType == MediaType.PERSON)
                    RecyclerViewLayoutState.DETAIL_LAYOUT_PERSON_STATE.ordinal
                else
                    RecyclerViewLayoutState.DETAIL_LAYOUT_STATE.ordinal
            else {
                RecyclerViewLayoutState.SIMPLE_LAYOUT_STATE.ordinal
            }
        } ?: return RecyclerViewLayoutState.SIMPLE_LAYOUT_STATE.ordinal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun changeViewLayout(): StaggeredGridLayoutManager? {
        layout?.let { layout ->
            if (layout.spanCount == 1) {
                layout.spanCount = 3
                notifyItemRangeChanged(0, itemCount)
            } else {
                layout.spanCount = 1
                notifyItemRangeChanged(0, itemCount)
            }
        }
        return layout
    }

    // if query is exhausted and there is no more results
    // creating new dummy object with id -1
    // that id will be triggered in getViewType
    fun submitList(
        list: List<T>?,
        isQueryExhausted: Boolean
    ) {
        val newList = list?.toMutableList()


//        if (isQueryExhausted)
        //          newList?.add(MovieUtils.createDummyMovie())

        val commitCallback = Runnable {
            // if process died must restore list position
            // very annoying
            restoration?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class MediaViewHolder
    constructor(
        private val binding: ViewDataBinding,
        private val interaction: Interaction?,
        private val restoration: Restoration?,
        private val resize: Boolean = false
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Media) = with(itemView) {

            binding.setVariable(BR.media, item)
            binding.executePendingBindings()

            if (resize && binding is LayoutMediaSimpleListItemBinding) {
                val size = Point()
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(
                    size
                )
                binding.movieItemContainer.layoutParams.width = size.x / 4
            }

            itemView.setOnClickListener {
                restoration?.saveListPosition()
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Media)
    }

    interface Restoration {
        fun restoreListPosition()

        fun saveListPosition()
    }
}