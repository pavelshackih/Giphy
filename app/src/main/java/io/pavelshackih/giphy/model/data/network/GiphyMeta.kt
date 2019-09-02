package io.pavelshackih.giphy.model.data.network

import com.google.gson.annotations.SerializedName

data class GiphyMeta(
    @SerializedName("msg")
    val message: String,
    @SerializedName("response_id")
    val responseId: String,
    @SerializedName("status")
    val status: Int
)