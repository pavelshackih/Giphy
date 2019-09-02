package io.pavelshackih.giphy.domain

interface SettingsRepository {

    suspend fun getSearchQuery(): String

    suspend fun setSearchQuery(searchQuery: String)
}