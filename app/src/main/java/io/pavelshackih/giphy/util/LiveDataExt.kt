package io.pavelshackih.giphy.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

inline fun <X, Y> LiveData<X>.flatMap(crossinline block: (X) -> LiveData<Y>): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(this, object : Observer<X> {
        private var source: LiveData<Y>? = null

        override fun onChanged(x: X) {
            val newLiveData = block(x)
            source?.let {
                result.removeSource(it)
            }
            source = newLiveData
            source?.let {
                result.addSource(it) { y -> result.setValue(y) }
            }
        }
    })
    return result
}