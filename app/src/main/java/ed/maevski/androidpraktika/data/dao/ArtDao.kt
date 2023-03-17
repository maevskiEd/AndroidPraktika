package ed.maevski.androidpraktika.data.dao

import androidx.room.*
import ed.maevski.androidpraktika.data.entity.DeviantPicture

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao
interface ArtDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_arts")
    fun getCachedFilms(): List<DeviantPicture>

    @Query("SELECT * FROM cached_arts WHERE setting LIKE :search")
    fun getCachedFilmsWithCategory(search: String): List<DeviantPicture>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<DeviantPicture>)

    @Delete
    fun deleteArt(art: DeviantPicture)
}