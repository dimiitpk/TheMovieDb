package com.dimi.moviedatabase.presentation.main.view.adapter


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.framework.network.NetworkConstants.SMALL_IMAGE_URL_PREFIX
import com.dimi.moviedatabase.presentation.GlideApp
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*
import kotlinx.android.synthetic.main.layout_season_list_item.view.*
import kotlinx.android.synthetic.main.layout_season_list_item.view.image

class SeasonAdapter(

    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _diffCallback = object : DiffUtil.ItemCallback<Season>() {

        override fun areItemsTheSame(oldItem: Season, newItem: Season): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Season, newItem: Season): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, _diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SeasonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_season_list_item,
                parent,
                false
            ),
            requestManager,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SeasonViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<Season>
    ) {
        for (season in list) {

            if (season.posterPath != null)
                requestManager
                    .load(SMALL_IMAGE_URL_PREFIX + season.posterPath)
                    .preload()
        }
    }

    fun submitList(list: List<Season>?) {
        differ.submitList(list)
    }

    class SeasonViewHolder
    constructor(
        itemView: View,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Season) = with(itemView) {

            itemView.arrow_select_season.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            requestManager
                .load(SMALL_IMAGE_URL_PREFIX + item.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.image)

            itemView.episodes_text.text = "(${item.episodeCount}episodes)"
            itemView.season_name.text = item.seasonName
            itemView.season_overview.text = item.overview
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Season)
    }
}