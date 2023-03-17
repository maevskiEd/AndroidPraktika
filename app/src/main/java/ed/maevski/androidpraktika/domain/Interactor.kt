package ed.maevski.androidpraktika.domain

import ed.maevski.androidpraktika.data.API
import ed.maevski.androidpraktika.data.DeviantartApi
import ed.maevski.androidpraktika.data.MainRepository
import ed.maevski.androidpraktika.data.PreferenceProvider
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.data.entity_token.TokenPlaceboResponse
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import ed.maevski.androidpraktika.utils.Converter
import ed.maevski.androidpraktika.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: DeviantartApi,
    var token: Token,
    private val preferences: PreferenceProvider
) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getDeviantArtsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        if (token.tokenKey.isEmpty()) getTokenFromApi()

//        if (token.tokenKey.isNotEmpty() && checkToken()) {
        if (token.tokenKey.isNotEmpty()) {
            retrofitService.getPictures(getDefaultCategoryFromPreferences(), token.tokenKey, 0, 20)
                .enqueue(object : Callback<DeviantartResponse> {

                    override fun onResponse(
                        call: Call<DeviantartResponse>,
                        response: Response<DeviantartResponse>
                    ) {
                        //При успехе мы вызываем метод, передаем onSuccess и в этот коллбэк список фильмов
                        val list = Converter.convertApiListToDtoList(response.body()?.results)
                        repo.putToDb(list)
                        //Кладем фильмы в бд
/*                        list.forEach {
                            repo.putToDb(deviantPicture = it, preferences.getDefaultCategory())
                        }*/
                        callback.onSuccess(list)
                    }

                    override fun onFailure(call: Call<DeviantartResponse>, t: Throwable) {
                        println("override fun onFailure(call: Call<DeviantartResponse>, t: Throwable)")
                        //В случае провала вызываем другой метод коллбека
                        callback.onFailure()
                    }
                })
        }
    }

    fun getTokenFromApi() {
        retrofitService.getToken("client_credentials", API.CLIENT_ID, API.CLIENT_SECRET)
            .enqueue(object : Callback<TokenResponse> {

                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                    token = Converter.convertApiTokenListToDtoToken(response.body())!!
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    //В случае провала вызываем другой метод коллбека
                    t.printStackTrace()
                }
            })
    }

    fun checkToken(): Boolean {
        retrofitService.checkToken(token.tokenKey)
            .enqueue(object : Callback<TokenPlaceboResponse> {
                var status: String = ""

                override fun onResponse(
                    call: Call<TokenPlaceboResponse>,
                    response: Response<TokenPlaceboResponse>
                ) {
                    //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                    status = response.body()?.status ?: ""
                    if (status == "success") token.status = true
                    else getTokenFromApi()
                }

                override fun onFailure(call: Call<TokenPlaceboResponse>, t: Throwable) {
                    //В случае провала вызываем другой метод коллбека
                    token.status = false
                    t.printStackTrace()
                }
            })

        return token.status
    }

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun getDeviantPicturesFromDB(): List<DeviantPicture> = repo.getAllFromDB()

    fun getDeviantPicturesFromDBWithCategory(): List<DeviantPicture> {
        return repo.getCategoryFromDB(preferences.getDefaultCategory())
    }
}
