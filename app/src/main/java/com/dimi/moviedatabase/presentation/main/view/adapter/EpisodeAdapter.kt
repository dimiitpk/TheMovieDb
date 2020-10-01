package com.dimi.moviedatabase.presentation.main.view.adapter


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.databinding.LayoutEpisodeListItemBinding

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
            LayoutEpisodeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
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
//        for (episode in list) {
//            if (episode.stillPath != null)
//                requestManager
//                    .load(SMALL_IMAGE_URL_PREFIX + episode.stillPath)
//                    .preload()
//        }
    }

    fun submitList(list: List<Episode>?) {
        differ.submitList(list)
    }

    class EpisodeViewHolder
    constructor(
        private val binding: LayoutEpisodeListItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Episode) {
            binding.episode = item
            binding.moreInfoButton.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Episode)
    }
}