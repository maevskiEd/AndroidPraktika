package ed.maevski.androidpraktika.utils

import ed.maevski.androidpraktika.data.entity.Results
import ed.maevski.androidpraktika.domain.DeviantPicture

object Converter {
    fun convertApiListToDtoList(list: List<Results>?): List<DeviantPicture> {
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
                )
            )
        }
        return result
    }
}
