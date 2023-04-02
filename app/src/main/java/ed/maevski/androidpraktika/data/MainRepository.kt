package ed.maevski.androidpraktika.data

import ed.maevski.androidpraktika.data.dao.ArtDao
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class MainRepository(private val artDao: ArtDao) {
    fun putToDb(films: List<DeviantPicture>) {
        artDao.insertAll(films)
    }

    fun getAllFromDB(): Flow<List<DeviantPicture>> {
        return artDao.getCachedFilms()
    }

    fun getCategoryFromDB(setting: String): Flow<List<DeviantPicture>> {
        return artDao.getCachedFilmsWithCategory(setting)
    }

    fun deleteFromDB(deviantPicture: DeviantPicture) {
        artDao.deleteArt(deviantPicture)
    }
}