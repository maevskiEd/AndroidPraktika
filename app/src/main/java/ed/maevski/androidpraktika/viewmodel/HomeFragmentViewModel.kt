package ed.maevski.androidpraktika.viewmodel

import android.database.Cursor
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {

    lateinit var picturesListData: Observable<List<DeviantPicture>>

    lateinit var cursor: Observable<Cursor>

    val tagSuggestionsSubject: BehaviorSubject<Cursor> = BehaviorSubject.create()

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        println("HomeFragmentViewModel: Init")
        App.instance.dagger.inject(this)
    }

    fun getDeviantArts() {
        println("getDeviantArts")
        interactor.getDeviantArtsFromApi(1)
    }

    fun getTagSuggestions(search: String) = interactor.getTagSuggestionsFromApi(search)

    fun getTagBrowseResult(search: String) = interactor.getTagBrowseFromApi(search)
}