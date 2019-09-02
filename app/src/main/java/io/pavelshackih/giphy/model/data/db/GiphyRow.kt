package io.pavelshackih.giphy.model.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class GiphyRow(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var gifId: String,
    var position: Int,
    var searchQuery: String,
    var url: String
)