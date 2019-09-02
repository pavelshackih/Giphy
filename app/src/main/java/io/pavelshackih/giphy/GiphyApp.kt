package io.pavelshackih.giphy

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import io.pavelshackih.giphy.di.DaggerGiphyComponent
import io.pavelshackih.giphy.di.GiphyComponent
import io.pavelshackih.giphy.di.GiphyModule

class GiphyApp : Application() {

    private lateinit var innerComponent: GiphyComponent

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    // https://github.com/Kotlin/kotlinx.coroutines/issues/878
                    // after R8 fix should be ok
                    // .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }

        super.onCreate()

        innerComponent = DaggerGiphyComponent.builder()
            .giphyModule(GiphyModule(this))
            .build()
    }

    val component: GiphyComponent
        get() = innerComponent
}