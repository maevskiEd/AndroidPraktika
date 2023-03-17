package ed.maevski.androidpraktika.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ed.maevski.androidpraktika.data.dao.ArtDao
import ed.maevski.androidpraktika.data.entity.DeviantPicture

@Database(entities = [DeviantPicture::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}