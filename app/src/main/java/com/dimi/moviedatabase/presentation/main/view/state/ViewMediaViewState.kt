package com.dimi.moviedatabase.presentation.main.view.state

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.ViewState
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_IMAGES
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_RECOMMENDED_MEDIA
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_SIMILAR_MEDIA
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_TRAILERS
import kotlinx.android.parcel.Parcelize

enum class SeasonContainerState {
    SEASONS_VIEW, EPISODES_VIEW, EPISODES_DETAIL_VIEW
}

@Parcelize
data class ViewMediaViewState(

    var media: Media? = null,
    var mediaType: MediaType? = null,
    var trailers: List<Video>? = null,
    var backdrops: List<Image>? = null,
    var posters: List<Image>? = null,
    var similarMedia: List<Media>? = null,
    var recommendedMedia: List<Media>? = null,
    var seasonEpisodes: List<Episode>? = null,
    var selectedSeason: Season? = null,
    var selectedEpisode: Episode? = null,
    var seasonContainerState: SeasonContainerState = SeasonContainerState.SEASONS_VIEW

) : Parcelable, ViewState {

    override fun setData(vararg hashMap: HashMap<String, Any>) {
        for( data in hashMap ) {

            (data[USE_CASE_TRAILERS] as List<*>?)?.filterIsInstance<Video>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        trailers = list
                }
            }
            (data[MEDIA_DETAILS] as Media?)?.let { movie ->
                media = movie
            }
            (data[USE_CASE_IMAGES] as List<*>?)?.filterIsInstance<Image>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty()) {
                        val (match, rest) = list.partition { it.isPoster }
                        posters = match
                        backdrops = rest
                    }
                }
            }
            (data[USE_CASE_SIMILAR_MEDIA] as List<*>?)?.filterIsInstance<Media>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        similarMedia = list
                }
            }
            (data[USE_CASE_RECOMMENDED_MEDIA] as List<*>?)?.filterIsInstance<Media>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        recommendedMedia = list
                }
            }
            (data[TV_SHOW_EPISODES] as List<*>?)?.filterIsInstance<Episode>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        seasonEpisodes = list
                }
            }
        }
    }

    companion object {
        const val MEDIA_DETAILS = "com.dimi.moviedatabase.presentation.main.view.state.MEDIA_DETAILS"
        const val TV_SHOW_EPISODES = "com.dimi.moviedatabase.presentation.main.view.state.TV_SHOW_EPISODES"

        fun getBundleKey() = ViewMediaViewState::class.java.`package`?.name + "." + ViewMediaViewState::class.simpleName
    }
}