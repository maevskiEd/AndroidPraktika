package ed.maevski.androidpraktika.di.modules

import dagger.Module
import dagger.Provides
import ed.maevski.androidpraktika.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}