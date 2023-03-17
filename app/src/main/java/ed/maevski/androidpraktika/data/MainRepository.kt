package ed.maevski.androidpraktika.data

import ed.maevski.androidpraktika.data.dao.ArtDao
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import java.util.concurrent.Executors

class MainRepository(private val artDao: ArtDao) {
    fun putToDb(films: List<DeviantPicture>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            artDao.insertAll(films)
        }
    }

    fun getAllFromDB(): List<DeviantPicture> {
        return artDao.getCachedFilms()
    }

    fun getCategoryFromDB(setting: String): List<DeviantPicture> {
        return artDao.getCachedFilmsWithCategory(setting)
    }

    fun deleteFromDB(deviantPicture: DeviantPicture){
        Executors.newSingleThreadExecutor().execute {
            artDao.deleteArt(deviantPicture)
        }
    }
}