package io.pavelshackih.giphy.model.data.network

import com.google.gson.annotations.SerializedName

data class GiphyItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: Map<String, GiphyImage>
)