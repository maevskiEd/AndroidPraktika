package ed.maevski.androidpraktika.data

import ed.maevski.androidpraktika.data.dao.ArtDao
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import io.reactivex.rxjava3.core.Observable

class MainRepository(private val artDao: ArtDao) {
    fun putToDb(films: List<DeviantPicture>) {
        artDao.insertAll(films)
    }

    fun getAllFromDB(): Observable<List<DeviantPicture>> {
        return artDao.getCachedFilms()
    }

    fun getCategoryFromDB(setting: String): Observable<List<DeviantPicture>> {
        return artDao.getCachedFilmsWithCategory(setting)
    }

    fun deleteFromDB(deviantPicture: DeviantPicture) {
        artDao.deleteArt(deviantPicture)
    }
}