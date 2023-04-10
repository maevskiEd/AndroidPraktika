package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {

    lateinit var picturesListData: Observable<List<DeviantPicture>>

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        println("HomeFragmentViewModel: Init")
        App.instance.dagger.inject(this)

//        picturesListData = interactor.getDeviantPicturesFromDBWithCategory()
//        getDeviantArts()
    }


    fun getDeviantArts() {
        println("getDeviantArts")
        interactor.getDeviantArtsFromApi(1)
    }
}