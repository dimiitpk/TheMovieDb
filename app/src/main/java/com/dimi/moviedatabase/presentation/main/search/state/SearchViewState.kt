package com.dimi.moviedatabase.presentation.main.search.state

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.ViewState
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.Constants.LAYOUT_GRID_SPAN_COUNT
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchViewState(

    var viewType: ViewType = ViewType.SEARCH,
    var mediaList: List<Media>? = null,
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var layoutManagerState: Parcelable? = null,
    var searchGenre: Int? = null,
    var selectedTab: Int? = null,
    var mediaType: MediaType? = null,
    var network: Network? = null,
    var sortFilter: SortFilter = SortFilter.BY_POPULARITY,
    var sortOrder: SortOrder = SortOrder.DESCENDING,
    var layoutSpanCount: Int = LAYOUT_GRID_SPAN_COUNT,
    var mediaListType: MediaListType? = null

) : Parcelable, ViewState {

    override fun setData(vararg hashMap: HashMap<String, Any>) {
        for( data in hashMap ) {

            (data[MediaType.MOVIE.name] as List<*>?)?.filterIsInstance<Movie>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        mediaList = list
                }
            }
            (data[MediaType.TV_SHOW.name] as List<*>?)?.filterIsInstance<TvShow>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        mediaList = list
                }
            }
            (data[MediaType.PERSON.name] as List<*>?)?.filterIsInstance<Person>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        mediaList = list
                }
            }
        }
    }

    companion object {
        fun getBundleKey() = SearchViewState::class.java.`package`?.name + "." + SearchViewState::class.simpleName
    }
}