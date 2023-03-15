package ed.maevski.androidpraktika.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ed.maevski.androidpraktika.data.MainRepository
import ed.maevski.androidpraktika.data.db.DatabaseHelper
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}