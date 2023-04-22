package ed.maevski.androidpraktika.domain

import android.annotation.SuppressLint
import ed.maevski.androidpraktika.data.*
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.data.entity_token.TokenPlaceboResponse
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import ed.maevski.androidpraktika.utils.Converter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
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
                        }.filter{it.category == "Visual Art"}
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

//                        errorEvent.post
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

/*        return suspendCoroutine { continuation ->
            scope.launch {
                launch {


                }.join()
                println("checkToken: Главный job этой функции")
            }
        }*/
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
//    fun getTagSearchResultFromApi(search: String): Observable<List<DeviantPicture>> {
    fun getTagSuggestionsFromApi(search: String) {
        val accessToken = preferences.getToken()
        val ll = retrofitService.getTagSuggestions(accessToken, search)
        println("getTagSearchResultFromApi -> ll $ll")
/*        val xx = ll.flatMap{it}.map {
//            fromIterable(it.results)
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
        }*/

/*            .map { it ->
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
            }*/

/*        val ll = retrofitService.getTagSearch(accessToken, search).map { it ->
            fromIterable(it)
        }

            .map {

//                Converter.convertApiListToDTOList(it)
            }*/
    }
}
