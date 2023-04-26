package ed.maevski.androidpraktika.data

import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import ed.maevski.androidpraktika.data.entity_tag.Results
import ed.maevski.androidpraktika.data.entity_tag.TagSuggestionsResponse
import ed.maevski.androidpraktika.data.entity_token.TokenPlaceboResponse
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeviantartApi {

    @GET("api/v1/oauth2/browse/tags")
    fun getTagsBrowse(
        @Query("tag") tag: String,
        @Query("limit") limit: Int,
        @Query("access_token") tokenKey: String,
    ): Observable<DeviantartResponse>

    @GET("api/v1/oauth2/browse/tags/search")
    fun getFilmFromSearch(
        @Query("tag_name") tagName: String,
        @Query("access_token") tokenKey: String,
    ): Observable<TagSuggestionsResponse>

    @GET("api/v1/oauth2/browse/tags/search")
    fun getTagSuggestions(
        @Query("access_token") tokenKey: String,
        @Query("tag_name") tagName: String
            ): Call<TagSuggestionsResponse>

    @GET("api/v1/oauth2/browse/{category}")
    fun getPictures(
        @Path("category") category: String,
        @Query("access_token") tokenKey: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,

        ): Call<DeviantartResponse>

    @GET("oauth2/token")
    fun getToken(
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
    ): Call<TokenResponse>

    @GET("/api/v1/oauth2/placebo")
    fun checkToken(
        @Query("access_token") tokenKey: String,
    ): Call<TokenPlaceboResponse>

    object ApiConst {
        const val BASE_URL = "https://www.deviantart.com/"
    }
}


