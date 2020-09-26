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
import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.framework.network.NetworkConstants.BIG_IMAGE_URL_PREFIX
import com.dimi.moviedatabase.framework.network.NetworkConstants.SMALL_IMAGE_URL_PREFIX
import com.dimi.moviedatabase.util.toSimpleString
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*

class EpisodeAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _diffCallback = object : DiffUtil.ItemCallback<Episode>() {

        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, _diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_episode_list_item,
                parent,
                false
            ),
            requestManager,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EpisodeViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun preloadGlideImages(
        list: List<Episode>
    ) {
        for (episode in list) {
            if (episode.stillPath != null)
                requestManager
                    .load(SMALL_IMAGE_URL_PREFIX + episode.stillPath)
                    .preload()
        }
    }

    fun submitList(list: List<Episode>?) {
        differ.submitList(list)
    }

    class EpisodeViewHolder
    constructor(
        itemView: View,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Episode) = with(itemView) {

            itemView.more_info_button.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            requestManager
                .load(BIG_IMAGE_URL_PREFIX + item.stillPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.image)

            itemView.episode_name.text = item.name
            itemView.episode_number_and_date.text = resources.getString(R.string.episode_info_format, item.episodeNumber, item.airDate.toSimpleString())
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Episode)
    }
}