package com.dimi.moviedatabase.presentation.main.view.adapter

import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.common.*
import com.dimi.moviedatabase.presentation.common.adapters.BindingAdapters.goneUnless
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.view.OnAboutFragmentDataBindingListener
import com.dimi.moviedatabase.util.Genre
import com.dimi.moviedatabase.util.toHoursAndMinutesText
import com.dimi.moviedatabase.util.toSimpleString
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.*


object BindingAdapters {

    @BindingAdapter("app:characterUnlessNull")
    @JvmStatic
    fun setCharacterUnlessNull(view: TextView, media: Media?) {
        if (media != null)
            media.character?.let { character ->
                view.text = view.context.getString(R.string.as_string_format, character)
            } ?: run { view.text = media.overview }
    }

    @BindingAdapter(requireAll = true, value = ["app:entriesFrom", "app:listener", "app:networks"])
    @JvmStatic
    fun setEntries(
        viewGroup: ViewGroup,
        media: Media?,
        listener: OnAboutFragmentDataBindingListener,
        networks: Boolean
    ) {
        if (media != null) {
            viewGroup.removeAllViews()
            when (media.mediaType) {
                MediaType.TV_SHOW, MediaType.MOVIE -> {
                    if (!networks)
                        media.genres?.let { genres ->
                            if (viewGroup.childCount < genres.size)
                                for (g in genres) {
                                    viewGroup.createButton().apply {
                                        val genreName = Genre(media.mediaType).getGenreName(g)
                                        text = genreName
                                        setOnClickListener {
                                            listener.onClickViewType(
                                                ViewType.GENRE,
                                                genreName ?: ""
                                            )
                                        }
                                    }
                                }
                        }
                    else
                        if (media is TvShow) {
                            media.networks?.let { list ->
                                if (viewGroup.childCount < list.size)
                                    for (item in list)
                                        viewGroup.createButton().apply {
                                            text = item.name
                                            setOnClickListener {
                                                listener.onClickViewType(
                                                    ViewType.NETWORK,
                                                    item.name
                                                )
                                            }
                                        }
                            }
                        }
                }
                MediaType.PERSON -> {
                    (media as Person).alsoKnownAs?.let { list ->
                        if (viewGroup.childCount < list.size)
                            for (item in list) {
                                if (item.isNotBlank())
                                    viewGroup.createButton().apply {
                                        text = item
                                    }
                            }
                    }
                }
            }
        }
    }

    @BindingAdapter("app:genreContainer")
    @JvmStatic
    fun setGenreContainer(view: TextView, media: Media?) {
        if (media != null) {
            when (media.mediaType) {
                MediaType.PERSON -> {
                    view.text = view.context.getString(R.string.also_known_as)
                    (media as Person).alsoKnownAs?.let { list ->
                        if (list.isNotEmpty())
                            if (list[0].isNotBlank())
                                view.visible()
                            else view.gone()
                    } ?: view.gone()
                }
                else -> view.text = view.context.getString(R.string.genres)
            }
        }
    }

    @BindingAdapter(
        value = ["android:youtubePlayerState", "android:youtubeListSize"],
        requireAll = true
    )
    @JvmStatic
    fun moreVideosButton(
        view: MaterialButton,
        visibilityState: VisibilityState?,
        listSize: Int
    ) {
        view.goneUnless((listSize > 1))
        if (visibilityState == VisibilityState.Displayed) {
            view.text = view.context.getString(R.string.hide_videos)
            view.iconGravity = MaterialButton.ICON_GRAVITY_START
        } else {
            view.iconGravity = MaterialButton.ICON_GRAVITY_END
            view.text = view.context.getString(R.string.videos_with_length, listSize)
        }
    }

//    @BindingAdapter("ellipsizeAndMaxLines")
//    @JvmStatic
//    fun TextView.setEllipsizeAndMaxLines(value: TextUtils.TruncateAt?) {
//        if( value != ellipsize ) ellipsize = value
////        if (layout.isEllipsized()) {
//   //         maxLines = Int.MAX_VALUE
////            ellipsize = null
////        } else {
////            maxLines = 3
//    //        ellipsize = TextUtils.TruncateAt.END
////        }
//    }

//    @BindingAdapter("ellipsizeAndMaxLinesAttrChanged")
//    @JvmStatic
//    fun TextView.setListener(listener: InverseBindingListener) {
//
//        println("InverseBindingListener ${layout.isEllipsized()}")
//        println("InverseBindingListener2 ${getEllipsizeAndMaxLines()}")
//        listener.onChange()
//        viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                viewTreeObserver.removeOnGlobalLayoutListener(this)
//                println("InverseBindingListener ${layout.isEllipsized()}")
//                if (layout.isEllipsized())
//                    listener.onChange()
//            }
//        })

//        val searchEditText = this.findViewById(R.id.search_src_text) as EditText
//
//        setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                println("InverseBindingListener")
//                searchEditText. = newText
//                listener.onChange()
//                return true
//            }
//        })
   // }
//
//    @InverseBindingAdapter(attribute = "ellipsizeAndMaxLines")
//    @JvmStatic
//    fun TextView.getEllipsizeAndMaxLines(): TextUtils.TruncateAt? = ellipsize


    @BindingAdapter("app:tagLine")
    @JvmStatic
    fun setTagLine(view: TextView, media: Media?) {
        val boolean = media is Movie
        if (boolean) view.text = (media as Movie).tagLine
        view.goneUnless(boolean)
    }

    @BindingAdapter("app:setupTableLayout")
    @JvmStatic
    fun setupTableLayout(view: TableLayout, media: Media?) {
        if (media != null) {
            view.apply {
                if (childCount > 0)
                    removeAllViews()

                when (media.mediaType) {
                    MediaType.MOVIE, MediaType.TV_SHOW -> {
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.original_title),
                                media.originalTitle ?: "-"
                            )
                        )
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.status),
                                media.status ?: "-"
                            )
                        )
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.runtime),
                                media.runtime?.toHoursAndMinutesText() ?: "-"
                            )
                        )
                        when (media) {
                            is Movie -> {
                                addView(
                                    createDetailInfoLine(
                                        context.getString(R.string.release_date),
                                        media.releaseDate.toSimpleString()
                                    )
                                )
                                addView(
                                    createDetailInfoLine(
                                        context.getString(R.string.budget),
                                        NumberFormat.getCurrencyInstance(Locale.US).apply {
                                            maximumFractionDigits = 0
                                            minimumFractionDigits = 0
                                        }.format(media.budget ?: 0) ?: "-"
                                    )
                                )
                                addView(
                                    createDetailInfoLine(
                                        resources.getString(R.string.revenue),
                                        NumberFormat.getCurrencyInstance(Locale.US).apply {
                                            maximumFractionDigits = 0
                                            minimumFractionDigits = 0
                                        }.format(media.revenue ?: 0) ?: "-"
                                    )
                                )
                            }
                            is TvShow -> {
                                addView(
                                    createDetailInfoLine(
                                        context.getString(R.string.first_air_date),
                                        media.firstAirDate.toSimpleString()
                                    )
                                )
                                addView(
                                    createDetailInfoLine(
                                        context.getString(R.string.last_air_date),
                                        media.lastAirDate.toSimpleString()
                                    )
                                )
                            }
                        }
                    }
                    MediaType.PERSON -> {
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.birthday),
                                (media as Person).birthday.toSimpleString()
                            )
                        )
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.place_of_birth),
                                media.placeOfBirth ?: "-"
                            )
                        )
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.death_day),
                                media.deathDay.toSimpleString()
                            )
                        )
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.gender),
                                if (media.gender == 2) context.getString(R.string.male) else context.getString(
                                    R.string.female
                                )
                            )
                        )
                        addView(
                            createDetailInfoLine(
                                context.getString(R.string.known_for_department),
                                media.department ?: "-"
                            )
                        )
                    }
                }
            }
        }
    }
}