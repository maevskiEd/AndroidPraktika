package ed.maevski.androidpraktika

import android.app.Application
import ed.maevski.androidpraktika.di.AppComponent
import ed.maevski.androidpraktika.di.DaggerAppComponent
import ed.maevski.androidpraktika.domain.Token


class App : Application() {
    lateinit var dagger: AppComponent
//    lateinit var token: Token

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.create()

    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}