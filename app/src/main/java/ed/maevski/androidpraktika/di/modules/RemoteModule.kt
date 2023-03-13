package ed.maevski.androidpraktika.di.modules

import dagger.Module
import dagger.Provides
import ed.maevski.androidpraktika.BuildConfig
import ed.maevski.androidpraktika.data.ApiConstants
import ed.maevski.androidpraktika.data.DeviantartApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RemoteModule {
    private val halfMinuteForSlowInternet = 30L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        //Настраиваем таймауты для медленного интернета
        .callTimeout(halfMinuteForSlowInternet, TimeUnit.SECONDS)
        .readTimeout(halfMinuteForSlowInternet, TimeUnit.SECONDS)
        //Добавляем логгер
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        //Указываем базовый URL из констант
        .baseUrl(DeviantartApi.ApiConst.BASE_URL)
        //Добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        //Добавляем кастомный клиент
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): DeviantartApi = retrofit.create(DeviantartApi::class.java)
}