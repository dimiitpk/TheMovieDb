package com.dimi.moviedatabase.presentation.main.search

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.util.Constants.LAYOUT_GRID_SPAN_COUNT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"


data class UserPreferences(
    val sortFilter: SortFilter,
    val sortOrder: SortOrder,
    val layoutSpanCount: Int
)

class UserPreferencesRepository(context: Context) {

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(
            name = USER_PREFERENCES_NAME
        )


    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val SORT_FILTER = preferencesKey<String>("sort_filter")
        val LAYOUT_MODE = preferencesKey<Int>("layout_span_count")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val sortOrder =
                SortOrder.valueOf(
                    preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.DESCENDING.name
                )
            val sortFilter =
                SortFilter.valueOf(
                    preferences[PreferencesKeys.SORT_FILTER] ?: SortFilter.BY_POPULARITY.name
                )

            val layoutMode = preferences[PreferencesKeys.LAYOUT_MODE] ?: LAYOUT_GRID_SPAN_COUNT

            UserPreferences(sortFilter, sortOrder, layoutMode)
        }

    suspend fun saveLayoutMode(layoutSpanCount: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAYOUT_MODE] = layoutSpanCount
        }
    }

    suspend fun enableFilterByPopularity() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_FILTER] = SortFilter.BY_POPULARITY.name
        }
    }

    suspend fun enableFilterByFirstAirDate() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_FILTER] = SortFilter.BY_FIRST_AIR_DATE.name
        }
    }

    suspend fun enableFilterByVoteAverage() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_FILTER] = SortFilter.BY_VOTE_COUNT.name
        }
    }

    suspend fun enableOrderASC() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = SortOrder.ASCENDING.name
        }
    }

    suspend fun enableOrderDESC() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = SortOrder.DESCENDING.name
        }
    }
}