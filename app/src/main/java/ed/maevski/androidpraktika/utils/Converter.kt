package ed.maevski.androidpraktika.utils

import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.data.entity.DeviantartResponse
import io.reactivex.rxjava3.core.Observable

object Converter {
    fun convertApiListToDTOList(response : DeviantartResponse): List<DeviantPicture> {
        var list: List<DeviantPicture> = emptyList()

        list = Observable.fromIterable(listOf(response.results)).flatMap { it ->
            Observable.fromIterable(it)
        }.map { it ->
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
                setting = ""
//                setting = preferences.getDefaultCategory()
            )
        }.toList().blockingGet()

        return list
    }
}