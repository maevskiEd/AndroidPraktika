package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Item

class MainActivityViewModel : ViewModel() {
    val picturesListLiveData = MutableLiveData<List<Item>>()

    private lateinit var interactor: Interactor

    init {
        val films = interactor.getPicturesDB()
        picturesListLiveData.postValue(films)
    }
}