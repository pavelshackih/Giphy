package io.pavelshackih.giphy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.pavelshackih.giphy.model.data.db.GiphyRow

@Database(
    entities = [GiphyRow::class],
    version = 1,
    exportSchema = false
)
abstract class GiphyDb : RoomDatabase() {

    abstract fun imagesDao(): GiphyDao
}