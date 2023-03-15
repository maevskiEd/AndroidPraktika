package ed.maevski.androidpraktika.di.modules

import dagger.Binds
import dagger.Module
import ed.maevski.androidpraktika.domain.AbstractToken
import ed.maevski.androidpraktika.domain.Token
import javax.inject.Singleton

@Module
interface DomainBindsModule {
    @Binds
    @Singleton
    fun bindToken(token: Token) : AbstractToken
}