package io.pavelshackih.giphy.model.data.network

import com.google.gson.annotations.SerializedName

data class GiphyResponse(
    @SerializedName("data")
    val data: List<GiphyItem>,
    @SerializedName("meta")
    val meta: GiphyMeta,
    @SerializedName("pagination")
    val pagination: GiphyPagination
)