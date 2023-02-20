package ed.maevski.androidpraktika.data

import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeviantartApi {
    @GET("browse/newest")
    fun getPictures(
        @Query("access_token") tokenKey: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,

        ): Call<DeviantartResponse>
}


