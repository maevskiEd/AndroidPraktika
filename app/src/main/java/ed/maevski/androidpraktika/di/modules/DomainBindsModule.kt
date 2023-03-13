package ed.maevski.androidpraktika.di.modules

import dagger.Binds
import dagger.Module
import ed.maevski.androidpraktika.domain.AbstractToken
import ed.maevski.androidpraktika.domain.Token

@Module
interface DomainBindsModule {
    @Binds
    fun bindToken(token: Token) : AbstractToken
}