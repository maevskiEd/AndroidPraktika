package ed.maevski.androidpraktika.domain

import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.data.API
import ed.maevski.androidpraktika.data.ApiConstants
import ed.maevski.androidpraktika.data.DeviantartApi
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.utils.Converter
import ed.maevski.androidpraktika.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val retrofitService: DeviantartApi) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
//        retrofitService.getPictures(API.TOKEN,0,10).enqueue(object : Callback<DeviantartResponse> {
        retrofitService.getPictures(App.instance.tokenKey,0,20).enqueue(object : Callback<DeviantartResponse> {

            override fun onResponse(
                call: Call<DeviantartResponse>,
                response: Response<DeviantartResponse>
            ) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.results))
            }

            override fun onFailure(call: Call<DeviantartResponse>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }
}