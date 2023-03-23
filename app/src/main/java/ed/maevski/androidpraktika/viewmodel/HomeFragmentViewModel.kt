package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.data.ApiCallback
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Interactor
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {

    var picturesListLiveData: LiveData<List<DeviantPicture>>

//    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor


    init {
        println("HomeFragmentViewModel: Init")
        App.instance.dagger.inject(this)
        picturesListLiveData = interactor.getDeviantPicturesFromDBWithCategory()

//        getDeviantArts()
    }


    fun getDeviantArts() {
//        showProgressBar.postValue(true)
        println("getDeviantArts")
        interactor.getDeviantArtsFromApi(1, object : ApiCallback {
            override fun onSuccess() {
                println("getDeviantArts:  onSuccess")

//                showProgressBar.postValue(false)
            }

            override fun onFailure() {
//                showProgressBar.postValue(false)
            }
        })
    }
}