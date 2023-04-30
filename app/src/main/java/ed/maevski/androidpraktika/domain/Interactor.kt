package ed.maevski.androidpraktika.domain

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import ed.maevski.androidpraktika.data.*
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.data.entity_tag.TagSuggestionsResponse
import ed.maevski.androidpraktika.data.entity_token.TokenPlaceboResponse
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
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
    fun getTagSuggestionsFromApi(search: String) : Observable<TagSuggestionsResponse>{
        val accessToken = preferences.getToken()
        return retrofitService.getTagSuggestions(search, accessToken)
    }


    fun getTagBrowseFromApi(tag: String): Observable<DeviantartResponse> {
        val accessToken = preferences.getToken()
        return retrofitService.getTagsBrowse(tag, 21,accessToken)
    }
}
