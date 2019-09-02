package io.pavelshackih.giphy.domain

import io.pavelshackih.giphy.model.data.db.GiphyRow
import io.pavelshackih.giphy.model.domain.Listing

class MainInteractorImpl(
    private val imagesRepository: ImagesRepository,
    private val settingsRepository: SettingsRepository
) : MainInteractor {

    override suspend fun getImages(searchQuery: String): Listing<GiphyRow> {
        settingsRepository.setSearchQuery(searchQuery)
        return imagesRepository.getImages(searchQuery)
    }

    override suspend fun getSearchQuery(): String = settingsRepository.getSearchQuery()
}