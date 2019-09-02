package io.pavelshackih.giphy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.pavelshackih.giphy.data.ImagesRepositoryImpl
import io.pavelshackih.giphy.data.SettingsRepositoryImpl
import io.pavelshackih.giphy.data.db.GiphyDao
import io.pavelshackih.giphy.data.db.GiphyDb
import io.pavelshackih.giphy.data.network.GiphyApi
import io.pavelshackih.giphy.domain.ImagesRepository
import io.pavelshackih.giphy.domain.MainInteractor
import io.pavelshackih.giphy.domain.MainInteractorImpl
import io.pavelshackih.giphy.domain.SettingsRepository
import javax.inject.Singleton

@Module
class GiphyModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context.applicationContext

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Context): SettingsRepository =
        SettingsRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideGiphyApi(): GiphyApi = GiphyApi.API

    @Provides
    @Singleton
    fun provideGiphyDb(context: Context): GiphyDb {
        return Room.databaseBuilder(
            context,
            GiphyDb::class.java, DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideGiphyDao(db: GiphyDb): GiphyDao = db.imagesDao()

    @Provides
    @Singleton
    fun provideGiphyRepository(
        api: GiphyApi,
        db: GiphyDb,
        dao: GiphyDao
    ): ImagesRepository {
        return ImagesRepositoryImpl(api, db, dao)
    }

    @Provides
    @Singleton
    fun provideMainInteractor(
        settingsRepository: SettingsRepository,
        imagesRepository: ImagesRepository
    ): MainInteractor = MainInteractorImpl(imagesRepository, settingsRepository)

    companion object {
        private const val DB_NAME = "images"
    }
}