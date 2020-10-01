package com.dimi.moviedatabase.presentation.images

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.movie.MovieUseCases
import com.dimi.moviedatabase.business.interactors.people.PeopleUseCases
import com.dimi.moviedatabase.business.interactors.tv_show.TvShowUseCases
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.presentation.common.BaseViewModel
import com.dimi.moviedatabase.util.SHARE_INTENT_IMAGE_FILE_NAME
import com.dimi.moviedatabase.presentation.images.state.FullScreenImagesStateEvent.*
import com.dimi.moviedatabase.presentation.images.state.FullScreenImagesViewState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import java.io.File

@ExperimentalCoroutinesApi
@FlowPreview
class FullScreenImagesViewModel
@ViewModelInject
constructor(
    @ApplicationContext private val context: Context,
    private val movieUseCases: MovieUseCases,
    private val tvShowUseCases: TvShowUseCases,
    private val peopleUseCases: PeopleUseCases,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<FullScreenImagesViewState>(savedStateHandle) {

    private var lastDownloadingStatusMessage = ""
    private val picturesDirectory =
        File(Environment.DIRECTORY_PICTURES)


    override fun handleNewData(data: FullScreenImagesViewState) {
        data.let { viewState ->
            viewState.backdrops?.let { list ->
                setBackDrops(list)
            }
            viewState.posters?.let { list ->
                setPosters(list)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<FullScreenImagesViewState>?> = when (stateEvent) {

            is GetImages -> {
                when (stateEvent.mediaType) {
                    MediaType.MOVIE -> movieUseCases.movieImagesUseCase.getResults(
                        viewState = FullScreenImagesViewState(),
                        movieId = stateEvent.mediaId.toInt(),
                        stateEvent = stateEvent
                    )
                    MediaType.TV_SHOW -> tvShowUseCases.tvShowImagesUseCase.getResults(
                        viewState = FullScreenImagesViewState(),
                        tvShowId = stateEvent.mediaId,
                        stateEvent = stateEvent
                    )
                    MediaType.PERSON -> peopleUseCases.personImagesUseCase.getResults(
                        viewState = FullScreenImagesViewState(),
                        personId = stateEvent.mediaId.toInt(),
                        stateEvent = stateEvent
                    )
                }
            }

            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }
            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }

    fun loadImages(mediaType: MediaType, mediaId: Long) {
        if (getPostersSize() <= 0)
            setStateEvent(GetImages(mediaId, mediaType))
    }

    override fun initNewViewState() = FullScreenImagesViewState()

    override fun getViewStateCopyWithoutBigLists(viewState: FullScreenImagesViewState): FullScreenImagesViewState {
        return viewState.copy()
    }

    override fun getUniqueViewStateIdentifier(): String {
        return FullScreenImagesViewState.BUNDLE_KEY
    }

    override fun onCleared() {
        super.onCleared()
        deleteSavedImageForSharing()
    }

    fun deleteSavedImageForSharing() {
        getSendingImageDownloadId()?.let {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.remove(it)
            setSendingImageDownloadId(null)
        }
    }

    fun sendImage(filePath: String) {

        val url = NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + filePath

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = getDownloadManagerRequest(
            SHARE_INTENT_IMAGE_FILE_NAME,
            Uri.parse(url),
            picturesDirectory
        )

        setSendingImageDownloadId(downloadManager.enqueue(request))
        getSendingImageDownloadId()?.let {
            val query = DownloadManager.Query().setFilterById(it)
            CoroutineScope(Dispatchers.IO).launch {
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                        withContext(Main) {
                            setSendingImageFileUri(downloadManager.getUriForDownloadedFile(it))
                        }
                    }

                    cursor.close()
                }
            }
        }
    }

    fun downloadImage(filePath: String) {

        val url = NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + filePath

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = getDownloadManagerRequest(filePath, Uri.parse(url), picturesDirectory)

        val downloadId = downloadManager.enqueue(request)

        executeDownload(
            url,
            downloadManager,
            DownloadManager.Query().setFilterById(downloadId)
        )
    }

    private fun executeDownload(
        url: String,
        downloadManager: DownloadManager,
        query: DownloadManager.Query
    ) {

        viewModelScope.launch(Default) {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                statusMessage(url, picturesDirectory, status).let { message ->
                    if (message != lastDownloadingStatusMessage) {
                        createToastMessage(message)
                        lastDownloadingStatusMessage = message
                    }
                }
                cursor.close()
            }
        }
    }

    private suspend fun createToastMessage(message: String) {
        withContext(Main) {
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message,
                            UIComponentType.Toast,
                            MessageType.None
                        )
                    )
                )
            )
        }
    }

    private fun statusMessage(url: String, directory: File, status: Int): String {
        return when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
    }

    private fun getDownloadManagerRequest(
        title: String,
        downloadUri: Uri,
        directory: File
    ): DownloadManager.Request {

        if (!directory.exists()) {
            directory.mkdirs()
        }

        return DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setAllowedOverRoaming(false)
            if (title != SHARE_INTENT_IMAGE_FILE_NAME) setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setTitle(title)
            setDescription("")
            setDestinationInExternalPublicDir(
                directory.toString(),
                context.getString(R.string.app_name) + "/" + title
            )
        }
    }


    fun setSendingImageFileUri(sendingImageFileUri: Uri?) {
        val update = getCurrentViewStateOrNew()
        update.sendingImageFileUri = sendingImageFileUri
        setViewState(update)
    }

    fun getSendingImageFileUri(): Uri? {
        return getCurrentViewStateOrNew().sendingImageFileUri
    }

    fun getPostersSize(): Int {
        return getCurrentViewStateOrNew().posters?.size ?: 0
    }

    fun getBackdropsSize(): Int {
        return getCurrentViewStateOrNew().backdrops?.size ?: 0
    }

    fun setSendingImageDownloadId(sendingImageDownloadId: Long?) {
        val update = getCurrentViewStateOrNew()
        update.sendingImageDownloadId = sendingImageDownloadId
        setViewState(update)
    }

    fun getSendingImageDownloadId(): Long? {
        return getCurrentViewStateOrNew().sendingImageDownloadId
    }

    fun setOrientation(orientation: Int) {
        val update = getCurrentViewStateOrNew()
        update.orientation = orientation
        setViewState(update)
    }

    fun getOrientation(): Int {
        return getCurrentViewStateOrNew().orientation
    }

    private fun setPosters(list: List<Image>) {
        val update = getCurrentViewStateOrNew()
        update.posters = list
        setViewState(update)
    }


    private fun setBackDrops(list: List<Image>) {
        val update = getCurrentViewStateOrNew()
        update.backdrops = list
        setViewState(update)
    }
}