package io.pavelshackih.giphy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import io.pavelshackih.giphy.data.converter.GiphyResponseConverter
import io.pavelshackih.giphy.data.db.GiphyDao
import io.pavelshackih.giphy.data.db.GiphyDb
import io.pavelshackih.giphy.data.network.GiphyApi
import io.pavelshackih.giphy.domain.ImagesRepository
import io.pavelshackih.giphy.model.data.db.GiphyRow
import io.pavelshackih.giphy.model.domain.Listing
import io.pavelshackih.giphy.model.domain.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagesRepositoryImpl(
    private val api: GiphyApi,
    private val db: GiphyDb,
    private val dao: GiphyDao
) : ImagesRepository {

    private val converter = GiphyResponseConverter()

    override fun getImages(searchQuery: String): Listing<GiphyRow> {
        val boundaryCallback = ImagesBoundaryCallback { callback, offset ->
            GlobalScope.launch { onNextPageSync(callback, searchQuery, offset) }
        }

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap { onNewSearch(searchQuery) }

        val dataSourceFactory = dao.getImages(searchQuery)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setMaxSize(PagedList.Config.MAX_SIZE_UNBOUNDED)
            .setInitialLoadSizeHint(PAGE_LIMIT)
            .setPrefetchDistance(PAGE_LIMIT - 5)
            .setPageSize(PAGE_LIMIT - 10)
            .build()

        val pagedList = LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listing(
            pagedList,
            boundaryCallback.networkState,
            retry = { boundaryCallback.helper.retryAllFailed() },
            refresh = { refreshTrigger.value = null },
            refreshState = refreshState
        )
    }

    private fun onNewSearch(searchQuery: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>(NetworkState.LOADING)
        GlobalScope.launch { doInitialSync(searchQuery, networkState) }
        return networkState
    }

    /**
     * initial sync between local device and remote server
     */
    private suspend fun doInitialSync(
        searchQuery: String,
        networkState: MutableLiveData<NetworkState>
    ) = withContext(Dispatchers.IO) {
        try {
            val response = api.search(searchQuery, limit = PAGE_LIMIT, offset = 0)
            val images = converter.convert(searchQuery, response)
            db.runInTransaction {
                dao.deleteAll()
                dao.insertAll(images)
            }
            networkState.postValue(NetworkState.LOADED)
        } catch (e: Throwable) {
            networkState.postValue(NetworkState.error(e.message))
        }
    }


    /**
     * loads and stores next page
     */
    private suspend fun onNextPageSync(
        callback: PagingRequestHelper.Request.Callback,
        searchQuery: String,
        offset: Int
    ) = withContext(Dispatchers.IO) {
        try {
            val response = api.search(searchQuery, limit = PAGE_LIMIT, offset = offset)
            val images = converter.convert(searchQuery, response)
            dao.insertAll(images)
            callback.recordSuccess()
        } catch (e: Throwable) {
            callback.recordFailure(e)
        }
    }

    companion object {
        private const val PAGE_LIMIT = 30
    }
}