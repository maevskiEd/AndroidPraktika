package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Item

class HomeFragmentViewModel: ViewModel() {
    val picturesListLiveData = MutableLiveData<List<Item>>()

    private var interactor: Interactor = App.instance.interactor

    init {
        val films = interactor.getPicturesDB()
        picturesListLiveData.postValue(films)
    }
}