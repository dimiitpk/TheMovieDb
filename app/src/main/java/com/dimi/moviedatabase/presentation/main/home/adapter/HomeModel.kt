package com.dimi.moviedatabase.presentation.main.home.adapter

import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

data class HomeModel(
    var list: List<Media>? = null,
    var title: String,
    var mediaType: MediaType,
    var type: String,
    var mediaListType: MediaListType
) {

    fun submitList(list: List<Media>) {
        this.list?.let {
            if( it.isEmpty())
                this.list = list
        }
    }
}