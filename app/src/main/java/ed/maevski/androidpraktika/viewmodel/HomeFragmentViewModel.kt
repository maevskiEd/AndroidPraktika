package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.domain.Token
import java.util.concurrent.Executors
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    val picturesListLiveData: LiveData<List<DeviantPicture>>

    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        picturesListLiveData = interactor.getDeviantPicturesFromDB()
        getDeviantArts()
    }


    fun getDeviantArts() {
        showProgressBar.postValue(true)
        interactor.getDeviantArtsFromApi(1, object : ApiCallback {
            override fun onSuccess() {
                showProgressBar.postValue(false)
            }

            override fun onFailure() {
                showProgressBar.postValue(false)
            }
        })
    }

    interface ApiCallback {
        fun onSuccess()

        fun onFailure()
    }
}