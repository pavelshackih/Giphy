package io.pavelshackih.giphy.model.data.network

import com.google.gson.annotations.SerializedName

data class GiphyImage(
    @SerializedName("width")
    val width: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("totalCount")
    val size: String,
    @SerializedName("title")
    val title: String
)