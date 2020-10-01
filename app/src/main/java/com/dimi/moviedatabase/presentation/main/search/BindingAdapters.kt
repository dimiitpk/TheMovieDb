package com.dimi.moviedatabase.presentation.main.search

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dimi.moviedatabase.business.domain.model.Network
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.setTextAndCompoundDrawablesColor
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType


object BindingAdapters {

    @BindingAdapter(value = ["android:viewType", "android:mediaType", "android:network"], requireAll = true)
    @JvmStatic
    fun TextView.setToolbarName(viewType: ViewType, mediaType: MediaType, network: Network?) {
        when (viewType) {
            ViewType.SEARCH -> {
                invisible()
            }
            ViewType.GENRE -> {
                text = mediaType.pluralName
                visible()
            }
            ViewType.NONE -> {
                text = mediaType.pluralName
                visible()
            }
            ViewType.NETWORK -> {
                text = network?.name
                visible()
            }
        }
    }
    @BindingAdapter( "android:textAndDrawableColor" )
    @JvmStatic
    fun TextView.setTextAndDrawablesColor(colorId: Int) {
        setTextAndCompoundDrawablesColor(colorId)
    }


//    @BindingAdapter("searchQueryAttrChanged")
//    @JvmStatic
//    fun SearchView.setListener(listener: InverseBindingListener) {
//
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
//    }
//
//    @BindingAdapter("searchQuery")
//    @JvmStatic
//    fun SearchView.setTextValue(value: String?) {
//        val searchEditText = this.findViewById(R.id.search_src_text) as EditText
//        if (value != searchEditText.text.toString()) searchEditText.setText(value)
//    }
//
//    @InverseBindingAdapter(attribute = "searchQuery")
//    @JvmStatic
//    fun SearchView.getTextValue(): String? = (this.findViewById(R.id.search_src_text) as EditText).text.toString()
}