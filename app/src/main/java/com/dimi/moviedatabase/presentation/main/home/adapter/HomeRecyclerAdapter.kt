package com.dimi.moviedatabase.presentation.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.util.SpacesItemDecoration
import kotlinx.android.synthetic.main.layout_home_list_item.view.*


class HomeRecyclerAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _diffCallback = object : DiffUtil.ItemCallback<HomeModel>() {

        override fun areItemsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            return ((oldItem.mediaType == newItem.mediaType) && (oldItem.mediaListType == newItem.mediaListType))
        }

        override fun areContentsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            HomeRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(_diffCallback).build()
        )

    internal inner class HomeRecyclerChangeCallback(
        private val adapter: HomeRecyclerAdapter
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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(
        list: List<HomeModel>?
    ) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_home_list_item,
            parent,
            false
        )
        return HomeViewHolder(requestManager, view, interaction)
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is HomeViewHolder -> {
                viewHolder.bind(differ.currentList[position])
            }
        }
    }

    class HomeViewHolder(
        private val requestManager: RequestManager,
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView), MediaListAdapter.Interaction {

        private var recyclerAdapter: MediaListAdapter<Media>

        init {
            with(itemView) {

                recycler_view.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                    val spaceDecoration = SpacesItemDecoration(15)
                    removeItemDecoration(spaceDecoration)
                    addItemDecoration(spaceDecoration)
                    recyclerAdapter =
                        MediaListAdapter(
                            requestManager = requestManager,
                            interaction = this@HomeViewHolder,
                            resize = true
                        )
                    adapter = recyclerAdapter
                }
            }
        }

        fun bind(item: HomeModel) = with(itemView) {

            arrow_right.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            category_text.text = item.type
            info_text.text = item.title

            recyclerAdapter.apply {
                item.list?.let {
                    preloadGlideImages(
                        list = it
                    )
                    if( it.isNotEmpty())
                        submitList(
                            list = it,
                            isQueryExhausted = false
                        )
                    progress_bar.gone()
                }
            }
        }

        override fun onItemSelected(position: Int, item: Media) {
            interaction?.onMediaSelected(position, item)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: HomeModel)

        fun onMediaSelected(position: Int, item: Media)
    }
}