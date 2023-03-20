package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Token
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {
    val tokenLiveData: MutableLiveData<Token> = MutableLiveData()

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        initToken()
    }

    private fun initToken() {
        TODO("Not yet implemented")
    }
}