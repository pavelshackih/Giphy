package io.pavelshackih.giphy.data

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import io.pavelshackih.giphy.model.data.db.GiphyRow
import io.pavelshackih.giphy.util.createStatusLiveData
import java.util.concurrent.Executors

class ImagesBoundaryCallback(
    private val onLoadMore: (PagingRequestHelper.Request.Callback, Int) -> Unit
) : PagedList.BoundaryCallback<GiphyRow>() {

    val helper = PagingRequestHelper(Executors.newSingleThreadExecutor())
    val networkState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            onLoadMore(it, 0)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: GiphyRow) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            onLoadMore(it, itemAtEnd.position)
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: GiphyRow) {
        // do nothing
    }
}