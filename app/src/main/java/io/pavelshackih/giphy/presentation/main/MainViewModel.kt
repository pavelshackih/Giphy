package io.pavelshackih.giphy.presentation.main

import androidx.lifecycle.*
import androidx.paging.PagedList
import io.pavelshackih.giphy.domain.MainInteractor
import io.pavelshackih.giphy.model.data.db.GiphyRow
import io.pavelshackih.giphy.model.domain.NetworkState
import io.pavelshackih.giphy.util.flatMap
import kotlinx.coroutines.launch

class MainViewModel(private val mainInteractor: MainInteractor) : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()
    private val imagesLiveData = searchLiveData.flatMap { query ->
        liveData { emit(mainInteractor.getImages(query)) }
    }

    val images: LiveData<PagedList<GiphyRow>> = imagesLiveData.switchMap { it.pagedList }
    val networkState: LiveData<NetworkState> = imagesLiveData.switchMap { it.networkState }
    val refreshState: LiveData<NetworkState> = imagesLiveData.switchMap { it.refreshState }

    init {
        viewModelScope.launch { onSearch(mainInteractor.getSearchQuery()) }
    }

    fun onSearch(search: String) {
        searchLiveData.value = search
    }

    fun onSwipeRefresh() = imagesLiveData.value?.refresh?.invoke()

    fun onRetry() = imagesLiveData.value?.retry?.invoke()
}