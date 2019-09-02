package io.pavelshackih.giphy.util

import android.app.Activity
import io.pavelshackih.giphy.GiphyApp
import io.pavelshackih.giphy.di.GiphyComponent

fun Activity.asComponent(): GiphyComponent {
    val appContext = this.applicationContext as GiphyApp
    return appContext.component
}