package ed.maevski.androidpraktika.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.domain.Interactor
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject


class MainActivityViewModel : ViewModel() {
    //    var flagToken = Channel<Boolean>(Channel.CONFLATED)
    var flagToken: BehaviorSubject<Boolean> = BehaviorSubject.create()
//    var errorEvent = Channel<Boolean>(Channel.CONFLATED)

    private var access_token: String

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        println("MainActivityViewModel: Init")

        App.instance.dagger.inject(this)
        access_token = interactor.getAccessTokenFromPreferences()

//        MainScope().launch {
//            errorEvent.send(false)

        initToken()
//        }
    }

    @SuppressLint("CheckResult")
    fun initToken() {
        flagToken.onNext(true)

        val tokenSubject = BehaviorSubject.create<String>()
        if (access_token.isEmpty()) {
            println("initToken: then")

            tokenSubject.subscribe {
                flagToken.onNext(false)
            }
            interactor.getTokenFromApi(tokenSubject)

        } else {
            println("initToken: else")

            tokenSubject.subscribe {
                if (it == "error") {
                    println("initToken: else -> error")

                    val tokenIntoSubject = BehaviorSubject.create<String>()
                    tokenIntoSubject.subscribe {
                        flagToken.onNext(false)
                    }
                    interactor.getTokenFromApi(tokenIntoSubject)
                } else {
                    println("initToken: else -> success")
                    flagToken.onNext(false)}
            }
            val s = interactor.checkToken(tokenSubject, access_token)

        }
    }
}