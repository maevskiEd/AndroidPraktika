package ed.maevski.androidpraktika.di

import dagger.Component
import ed.maevski.androidpraktika.di.modules.RemoteModule
import ed.maevski.androidpraktika.di.modules.DatabaseModule
import ed.maevski.androidpraktika.di.modules.DomainModule
import ed.maevski.androidpraktika.viewmodel.HomeFragmentViewModel
import javax.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
}