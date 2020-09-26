package com.dimi.moviedatabase.presentation.images.state

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.ViewState
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_IMAGES
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FullScreenImagesViewState(

    var mediaType: MediaType? = null,
    var posters: List<Image>? = null,
    var backdrops: List<Image>? = null,
    var orientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
    var sendingImageFileUri: Uri? = null,
    var sendingImageDownloadId: Long? = null
) : Parcelable, ViewState {

    override fun setData(vararg hashMap: HashMap<String, Any>) {
        for (data in hashMap) {

            (data[USE_CASE_IMAGES] as List<*>?)?.filterIsInstance<Image>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty()) {
                        val (match, rest) = list.partition { it.isPoster }
                        posters = match
                        backdrops = rest
                    }
                }
            }
        }
    }

    companion object {

        const val BUNDLE_KEY =
            "com.dimi.moviedatabase.presentation.images.state.FullScreenImagesViewState"
    }
}