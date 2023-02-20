package ed.maevski.androidpraktika.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.domain.Interactor
import ed.maevski.androidpraktika.domain.Item

class HomeFragmentViewModel: ViewModel() {
//    val picturesListLiveData = MutableLiveData<List<Item>>()
    val picturesListLiveData:  MutableLiveData<List<Item>> = MutableLiveData()

    private var interactor: Interactor = App.instance.interactor

    init {
        interactor.getFilmsFromApi(1, object : ApiCallback {
            override fun onSuccess(pictures: List<Item>) {
                picturesListLiveData.postValue(pictures)
            }

            override fun onFailure() {
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(pictures: List<Item>)
        fun onFailure()
    }
}