package ed.maevski.androidpraktika.di.modules

import dagger.Module
import dagger.Provides
import ed.maevski.androidpraktika.data.DeviantartApi
import ed.maevski.androidpraktika.data.MainRepository
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Token
import javax.inject.Singleton

@Module
class DomainModule {

/*    @Singleton
    @Provides
    fun provideToken() = Token()*/

    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, deviantartApi: DeviantartApi, token: Token) = Interactor(repo = repository, retrofitService = deviantartApi, token = token)

}