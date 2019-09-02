package io.pavelshackih.giphy.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.pavelshackih.giphy.domain.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun setSearchQuery(searchQuery: String) {
        withContext(Dispatchers.IO) {
            prefs.edit()
                .putString(SEARCH_QUERY_KEY, searchQuery)
                .apply()
        }
    }

    override suspend fun getSearchQuery(): String {
        return withContext(Dispatchers.IO) {
            prefs.getString(
                SEARCH_QUERY_KEY,
                null
            ) ?: DEFAULT_SEARCH_QUERY
        }
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "SEARCH_QUERY_KEY"
        private const val DEFAULT_SEARCH_QUERY = "funny cats"
    }
}