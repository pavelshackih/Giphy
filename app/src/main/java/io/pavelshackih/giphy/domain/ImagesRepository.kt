package io.pavelshackih.giphy.domain

import io.pavelshackih.giphy.model.data.db.GiphyRow
import io.pavelshackih.giphy.model.domain.Listing

interface ImagesRepository {

    fun getImages(searchQuery: String): Listing<GiphyRow>
}