package ed.maevski.androidpraktika.data

import androidx.lifecycle.LiveData
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

    fun getAllFromDB(): LiveData<List<DeviantPicture>> {
        val a = artDao.getCachedFilms()
        println("getAllFromDB - > $a -> ${a.value}")

        return a
    }

    fun getCategoryFromDB(setting: String): LiveData<List<DeviantPicture>> {
        val a = artDao.getCachedFilmsWithCategory(setting)
        println("getCategoryFromDB - > $a -> ${a.value}")
        return a
    }

    fun deleteFromDB(deviantPicture: DeviantPicture){
        Executors.newSingleThreadExecutor().execute {
            artDao.deleteArt(deviantPicture)
        }
    }
}