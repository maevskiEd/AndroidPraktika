package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.domain.Token
import java.util.concurrent.Executors
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    //    val picturesListLiveData = MutableLiveData<List<Item>>()
    val picturesListLiveData: MutableLiveData<List<Item>> = MutableLiveData()

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)

        getDeviantArts()
    }


    fun getDeviantArts() {
        interactor.getDeviantArtsFromApi(1, object : ApiCallback {
            override fun onSuccess(pictures: List<Item>) {
                println("onSuccess")

                picturesListLiveData.postValue(pictures)
            }

            override fun onFailure() {
                println("onFailure")
//                picturesListLiveData.postValue(interactor.getDeviantPicturesFromDBWithCategory())
                Executors.newSingleThreadExecutor().execute {
                    picturesListLiveData.postValue(interactor.getDeviantPicturesFromDB())
                }
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(pictures: List<Item>)

        fun onFailure()
    }
}