package io.pavelshackih.giphy.data.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.pavelshackih.giphy.model.data.db.GiphyRow

@Dao
interface GiphyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<GiphyRow>)

    @Query("select * from images where searchQuery = :query order by position ASC")
    fun getImages(query: String): DataSource.Factory<Int, GiphyRow>

    @Query("delete from images")
    fun deleteAll()
}