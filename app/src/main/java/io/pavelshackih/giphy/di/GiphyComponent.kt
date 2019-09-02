package io.pavelshackih.giphy.di

import dagger.Component
import io.pavelshackih.giphy.domain.MainInteractor
import javax.inject.Singleton

@Singleton
@Component(modules = [GiphyModule::class])
interface GiphyComponent {

    fun mainInteractor(): MainInteractor
}
