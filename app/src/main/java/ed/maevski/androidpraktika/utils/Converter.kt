package ed.maevski.androidpraktika.utils

import ed.maevski.androidpraktika.data.entity.Results
import ed.maevski.androidpraktika.data.entity_token.TokenResponse
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Token

object Converter {
    fun convertApiListToDtoList(list: List<Results>?, _setting: String): List<DeviantPicture> {
        val result = mutableListOf<DeviantPicture>()
        list?.forEach {
            result.add(
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
                    setting = _setting
                )
            )
//            println(it)
        }
        return result
    }

    fun convertApiTokenListToDtoToken(tokenResponse: TokenResponse?): Token? {
        return tokenResponse?.let { Token(it.access_token, true) }
    }
}
