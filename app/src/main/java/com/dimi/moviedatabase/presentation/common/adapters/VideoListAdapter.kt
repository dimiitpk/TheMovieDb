package com.dimi.moviedatabase.presentation.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.layout_video_list_item.view.*


class VideoListAdapter(
    private val videoIds: ArrayList<Video>,
    private val lifecycle: Lifecycle
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_video_list_item,
            parent,
            false
        )


        lifecycle.addObserver(view.youtube_player_view as YouTubePlayerView)

        return VideoViewHolder(view, null, null)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is VideoViewHolder -> {
                println("VideoListAdapter onBindViewHolder ${videoIds[position]}")
                viewHolder.cueVideo(videoIds[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return videoIds.size
    }

    class VideoViewHolder(
        itemView: View,
        private var youtubePlayer: YouTubePlayer? = null,
        private var currentVideoId: String? = null
    ) : RecyclerView.ViewHolder(itemView) {


        init {
            (itemView.youtube_player_view as YouTubePlayerView).addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youtubePlayer = youTubePlayer
                    currentVideoId?.let {
                        youtubePlayer!!.cueVideo(it, 0f)
                    }
                }
            })
        }

        fun cueVideo(video: Video) {
            println("VideoListAdapter cueVideo $video")
            itemView.video_title.text = video.name
            currentVideoId = video.key
            youtubePlayer?.cueVideo(video.key, 0f) ?: return
        }
    }

}