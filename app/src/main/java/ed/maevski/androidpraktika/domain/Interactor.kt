package ed.maevski.androidpraktika.domain

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import ed.maevski.androidpraktika.data.*
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.data.entity_tag.Results
import ed.maevski.androidpraktika.data.entity_tag.TagSuggestionsResponse
import ed.maevski.androidpraktika.data.entity_token.TokenPlaceboResponse
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import ed.maevski.androidpraktika.utils.Converter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.filter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Interactor(
    private val repo: MainRepository,
    private val retrofitService: DeviantartApi,
    var token: Token,
    private val preferences: PreferenceProvider
) {

    //и страницу, которую нужно загрузить (это для пагинации)
    fun getDeviantArtsFromApi(page: Int) {
        val accessToken = preferences.getToken()
        var list: List<DeviantPicture> = emptyList()

        println("getDeviantArtsFromApi")
        retrofitService.getPictures(getDefaultCategoryFromPreferences(), accessToken, 0, 20)
            .enqueue(object : Callback<DeviantartResponse> {

                override fun onResponse(
                    call: Call<DeviantartResponse>,
                    response: Response<DeviantartResponse>
                ) {
                    println("getDeviantArtsFromApi: onResponse")

                    response.body()?.let { response ->
                        list = fromIterable(listOf(response.results)).flatMap { it ->
                            fromIterable(it)
                        }.filter { it.category == "Visual Art" }
                            .map { it ->
                                DeviantPicture(
                                    id = it.deviationid,
                                    title = it.title,
                                    author = it.author.username,
                                    picture = 0,
                                    description = "",
                                    url = it.preview.src,
                                    urlThumb150 = it.thumbs[0].src,
                                    countFavorites = it.stats.favourites,
                                    comments = it.stats.comments,
                                    countViews = 100000,
                                    isInFavorites = false,
                                    setting = preferences.getDefaultCategory()
                                )
                            }.toList().blockingGet()
                    }
                    Completable.fromSingle<List<DeviantPicture>> {
                        repo.putToDb(list)
                    }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }

                override fun onFailure(call: Call<DeviantartResponse>, t: Throwable) {
                    println("override fun onFailure(call: Call<DeviantartResponse>, t: Throwable)")
                    //В случае провала вызываем другой метод коллбека
                    println("getDeviantArtsFromApi: onFailure")
                }
            })
    }

    fun getTokenFromApi(subject: BehaviorSubject<String>) {
        retrofitService.getToken("client_credentials", API.CLIENT_ID, API.CLIENT_SECRET)
            .enqueue(object : Callback<TokenResponse> {

                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    println("getTokenFromApi: onResponse")

                    if (response.body()?.status.equals("success")) {
                        println("getTokenFromApi: onResponse -> success")

                        saveAccessTokenFromPreferences(
                            response.body()?.access_token ?: ""
                        )
                        subject.onNext("success")
                        subject.onComplete()
                    } else {
                        subject.onNext("error")
                        subject.onComplete()
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    subject.onNext("connect ERROR")
                    subject.onComplete()
//                    t.printStackTrace()
//                    errorEvent.postValue("connect ERROR")
                }
            })
    }

    fun checkToken(subject: BehaviorSubject<String>, accessToken: String) {
        retrofitService.checkToken(accessToken)
            .enqueue(object : Callback<TokenPlaceboResponse> {

                override fun onResponse(
                    call: Call<TokenPlaceboResponse>,
                    response: Response<TokenPlaceboResponse>
                ) {
                    println("checkToken: onResponse")

                    if (response.body()?.status.equals("success")) {
                        println("checkToken: onResponse  -> success")
                        println("response.body()?.status : ${response.body()?.status}")

                        subject.onNext("success")
                        subject.onComplete()

                    } else {
                        println("checkToken: onResponse  -> error")

                        subject.onNext("error")
                        subject.onComplete()

                    }
                }

                override fun onFailure(call: Call<TokenPlaceboResponse>, t: Throwable) {
//                    t.printStackTrace()
                    println("checkToken: onFailure")

                    subject.onNext("connect ERROR")
                    subject.onComplete()
                }
            })
    }

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun getAccessTokenFromPreferences() = preferences.getToken()

    fun saveAccessTokenFromPreferences(accessToken: String) {
        preferences.saveToken(accessToken)
    }

    fun getDeviantPicturesFromDB(): Observable<List<DeviantPicture>> = repo.getAllFromDB()

    fun getDeviantPicturesFromDBWithCategory(): Observable<List<DeviantPicture>> {
        println("getDeviantPicturesFromDBWithCategory -> ${preferences.getDefaultCategory()}")
        return repo.getCategoryFromDB(preferences.getDefaultCategory())
    }

    @SuppressLint("CheckResult")
    fun getTagSuggestionsFromApi(subject: BehaviorSubject<Cursor>, search: String) {
//        Первый ключ в MatrixCursor всегда жестко называется _id
        val cursor = MatrixCursor(arrayOf("_id", "tag"))
        val accessToken = preferences.getToken()
//        По этой переменной индексируем записи в создаваемом курсоре
        var i = 0

//        Ищем на сайте Deviantart все tag подподающие под условие (минимальное количество символов = 3)
//        Затем проходит по каждому элементу списка ответа и формируем курсор на основе MatrixCursor
/*        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            println("Внутри Observable")
            subscriber.onNext("map")

        }).subscribeOn(Schedulers.io())
            .map {
                println("Inside map")
                println("it: $it")
                val xx = retrofitService.getTagSuggestions(accessToken, search)
                println("xx: $xx")
                xx
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    println("Error")
//                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                },
                onNext = {
                    println("On next")
//                    filmsAdapter.addItems(it)
                }
            )*/
//            .addTo(autoDisposable)



        val yy = retrofitService.getTagSuggestions(accessToken, search).enqueue(object : Callback<TagSuggestionsResponse>{
            override fun onResponse(
                call: Call<TagSuggestionsResponse>,
                response: Response<TagSuggestionsResponse>
            ) {
                println("getTagSuggestionsFromApi: onResponse")
                println(response.body())
            }

            override fun onFailure(call: Call<TagSuggestionsResponse>, t: Throwable) {
                println("getTagSuggestionsFromApi: onFailure")
                println(t)
            }
        })

//        println("getTagSuggestionsFromApi -> xx: $xx")
/*       xx.map {
           println(it.results)
        }*/
/*             xx.map {
            it.forEach {
                cursor.addRow(arrayOf(i++, it.tag_name))
            }
            cursor
        }*/
        println("interacor -> getTagSuggestionsFromApi -> cursor: $cursor")
        subject.onNext(cursor)
//        subject.onComplete()
    }

    @SuppressLint("CheckResult")
    fun getSearchResultFromApi(search: String): Observable<TagSuggestionsResponse> {
        val accessToken = preferences.getToken()
        return retrofitService.getFilmFromSearch(search, accessToken)
    }

    fun getTagBrowseFromApi(tag: String): Observable<DeviantartResponse> {
        val accessToken = preferences.getToken()
        return retrofitService.getTagsBrowse(tag, 21,accessToken)
    }
}
