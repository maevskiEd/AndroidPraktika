package ed.maevski.androidpraktika.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ed.maevski.androidpraktika.data.MainRepository
import ed.maevski.androidpraktika.data.dao.ArtDao
import ed.maevski.androidpraktika.data.db.AppDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideArtDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "art_db"
        ).build().artDao()

    @Provides
    @Singleton
    fun provideRepository(artDao: ArtDao) = MainRepository(artDao)
}