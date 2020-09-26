package com.dimi.moviedatabase.business.domain.model

import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

object MovieUtils {

    const val NO_RESULTS_ID = -1L

    fun createDummyMovie(): Movie {
        return Movie(
            NO_RESULTS_ID,
            "",
            0.0,
            0,
            Date(),
            0.0f,
            "",
            "",
            null,
            ""
        )
    }

    fun createNoMoreResultsList(): List<Movie> {
        val list: ArrayList<Movie> = ArrayList()
        list.add(createDummyMovie())
        return list
    }
}