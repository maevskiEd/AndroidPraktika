package ed.maevski.androidpraktika.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ed.maevski.androidpraktika.data.*
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.data.entity_token.TokenPlaceboResponse
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import ed.maevski.androidpraktika.utils.Converter
import ed.maevski.androidpraktika.viewmodel.MainActivityViewModel
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
    fun getDeviantArtsFromApi(page:Int, callback: ApiCallback) {
        val accessToken = preferences.getToken()

        println("getDeviantArtsFromApi")
        retrofitService.getPictures(getDefaultCategoryFromPreferences(), accessToken, 0, 20)
            .enqueue(object : Callback<DeviantartResponse> {

                override fun onResponse(
                    call: Call<DeviantartResponse>,
                    response: Response<DeviantartResponse>
                ) {
                    println("getDeviantArtsFromApi: onResponse")

                    //При успехе мы вызываем метод, передаем onSuccess и в этот коллбэк список фильмов
                    val list = Converter.convertApiListToDtoList(response.body()?.results, preferences.getDefaultCategory())

                    println("call: $call")
                    println("response: $response")
                    println("response.isSuccessful: ${response.isSuccessful}")
                    println("response.body: ${response.body()}")
                    println("response.code: ${response.code()}")
                    println("response.headers: ${response.headers()}")
                    println("response.errorBody: ${response.errorBody()}")
                    println("response.message: ${response.message()}")
                    println("response.raw: ${response.raw()}")
                    println(response.body()?.results)
                    println("list: $list ")

                    repo.putToDb(list)
                    callback.onSuccess()
                }

                override fun onFailure(call: Call<DeviantartResponse>, t: Throwable) {
                    println("override fun onFailure(call: Call<DeviantartResponse>, t: Throwable)")
                    //В случае провала вызываем другой метод коллбека
                    println("getDeviantArtsFromApi: onFailure")
                    callback.onFailure()
                }
            })
    }

    fun getTokenFromApi(flagToken: MutableLiveData<Boolean>, errorEvent: MainActivityViewModel.SingleLiveEvent<String>) {
        retrofitService.getToken("client_credentials", API.CLIENT_ID, API.CLIENT_SECRET)
            .enqueue(object : Callback<TokenResponse> {

                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    println("getTokenFromApi: onResponse")

                    if (response.body()?.status.equals("success")) {
                        println("getTokenFromApi: onResponse -> success")

                        saveAccessTokenFromPreferences(response.body()?.access_token ?: "")
                        flagToken.postValue(false)
                    } else {
//                        errorEvent.post
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                    t.printStackTrace()
                    flagToken.postValue(false)
                    errorEvent.postValue("connect ERROR")
                }
            })
    }

    fun checkToken(accessToken: String, flagToken: MutableLiveData<Boolean>,errorEvent: MainActivityViewModel.SingleLiveEvent<String>){
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
                        flagToken.postValue(false)
                    } else {
                        println("checkToken: onResponse  -> error")
                        println("checkToken: onResponse  -> getTokenFromApi")

                        getTokenFromApi(flagToken, errorEvent)
                    }
                }

                override fun onFailure(call: Call<TokenPlaceboResponse>, t: Throwable) {
//                    t.printStackTrace()
                    errorEvent.postValue("connect ERROR")

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

    fun getDeviantPicturesFromDB(): LiveData<List<DeviantPicture>> = repo.getAllFromDB()

    fun getDeviantPicturesFromDBWithCategory(): LiveData<List<DeviantPicture>> {
        println("getDeviantPicturesFromDBWithCategory -> ${preferences.getDefaultCategory()}")
        return repo.getCategoryFromDB(preferences.getDefaultCategory())
    }
}
