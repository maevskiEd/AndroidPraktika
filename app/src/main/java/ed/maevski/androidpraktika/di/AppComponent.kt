package ed.maevski.androidpraktika.di

import dagger.Component
import ed.maevski.androidpraktika.data.PreferenceProvider
import ed.maevski.androidpraktika.di.modules.RemoteModule
import ed.maevski.androidpraktika.di.modules.DatabaseModule
import ed.maevski.androidpraktika.di.modules.DomainBindsModule
import ed.maevski.androidpraktika.di.modules.DomainModule
import ed.maevski.androidpraktika.viewmodel.HomeFragmentViewModel
import ed.maevski.androidpraktika.viewmodel.SettingsFragmentViewModel
import javax.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class,
        DomainBindsModule::class
    ]
)
interface AppComponent {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)

    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}