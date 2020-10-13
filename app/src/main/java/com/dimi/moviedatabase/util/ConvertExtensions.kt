package com.dimi.moviedatabase.util

import com.dimi.moviedatabase.business.domain.state.Enumerable
import com.dimi.moviedatabase.framework.network.NetworkConstants
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun <T> List<T>.enumAsString(): String where T : Enum<T>, T : Enumerable = this
    .map { it.code }
    .joinToString(separator = ",")

fun <T> String.toEnumList(creator: (code: Int) -> T): List<T> where T : Enum<T>, T : Enumerable =
    this
        .split(',')
        .map { it.toInt() }
        .map { creator(it) }


fun String.toListOfString(): List<String> = this
    .split(",")
    .map { it }

fun String.toListOfInts(): List<Int> =
    if (isNullOrBlank())
        emptyList()
    else
        this
            .split(",")
            .map { it.toInt() }

fun Int.toHoursAndMinutesText(): String = this.let {
    val hours = it / 60
    val min = it % 60
    if( hours == 1 && min == 0 )
        "60 min"
    else if (hours > 0)
        "$hours hrs $min min"
    else "$min min"
}

fun <T> List<T>.asString(): String = this.joinToString(",")

fun Date?.toSimpleString(): String {
    val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
    return if( this != null ) outputFormat.format(this) else "-"
}

fun String?.toDate() : Date? {
    if (this.isNullOrBlank()) return null
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return inputFormat.parse(this) ?: null
}

fun <T> List<T>.cutList(page: Int): List<T> {
    var limit = (page * NetworkConstants.PAGE_SIZE)
    return if (isEmpty()) listOf()
    else {
        if (limit > size) limit = size
        this.subList(0, limit).toList()
    }
}