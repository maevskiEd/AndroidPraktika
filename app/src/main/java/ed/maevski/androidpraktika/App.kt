package ed.maevski.androidpraktika

import android.app.Application
import ed.maevski.androidpraktika.data.API
import ed.maevski.androidpraktika.data.ApiConstants
import ed.maevski.androidpraktika.data.DeviantartApi
import ed.maevski.androidpraktika.data.DeviantartToken
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import ed.maevski.androidpraktika.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var interactor: Interactor
    lateinit var retrofitService: DeviantartApi
    lateinit var retrofitTokenService: DeviantartToken
    var tokenKey = ""

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this

        //Создаём кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()

        getToken(okHttpClient)

        //Создаем Ретрофит
        val retrofit = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ApiConstants.BASE_URL2)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
        //Создаем сам сервис с методами для запросов
        retrofitService = retrofit.create(DeviantartApi::class.java)
        //Инициализируем интерактор
        interactor = Interactor(retrofitService)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }

    fun getToken(okHttpClient: OkHttpClient) {
        //Создаем Ретрофит
        val retrofitToken = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ApiConstants.TOKEN_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
        //Создаем сам сервис с методами для запросов
        retrofitTokenService = retrofitToken.create(DeviantartToken::class.java)

        retrofitTokenService.getToken("client_credentials", API.CLIENT_ID,API.CLIENT_SECRET).enqueue(object :
            Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                tokenKey = response.body()?.access_token ?: ""
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}