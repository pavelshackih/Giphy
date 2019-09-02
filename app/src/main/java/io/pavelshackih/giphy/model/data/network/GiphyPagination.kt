package io.pavelshackih.giphy.model.data.network

import com.google.gson.annotations.SerializedName

data class GiphyPagination(
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("total_count")
    val totalCount: Int
)