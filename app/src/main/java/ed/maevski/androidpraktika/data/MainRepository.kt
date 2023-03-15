package ed.maevski.androidpraktika.data

import android.content.ContentValues
import android.database.Cursor
import ed.maevski.androidpraktika.data.db.DatabaseHelper
import ed.maevski.androidpraktika.domain.DeviantPicture

class MainRepository(databaseHelper: DatabaseHelper) {
    //Инициализируем объект для взаимодействия с БД
    private val sqlDb = databaseHelper.readableDatabase

    //Создаем курсор для обработки запросов из БД
    private lateinit var cursor: Cursor

    fun putToDb(deviantPicture: DeviantPicture, setting : String) {
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, deviantPicture.title)
            put(DatabaseHelper.COLUMN_AUTHOR, deviantPicture.author)
            put(DatabaseHelper.COLUMN_PICTURE, deviantPicture.picture)
            put(DatabaseHelper.COLUMN_DESCRIPTION, deviantPicture.description)
            put(DatabaseHelper.COLUMN_URL, deviantPicture.url)
            put(DatabaseHelper.COLUMN_COUNT_FAVORITES, deviantPicture.countFavorites)
            put(DatabaseHelper.COLUMN_COMMENTS, deviantPicture.comments)
            put(DatabaseHelper.COLUMN_COUNT_VIEWS, deviantPicture.countViews)
            put(DatabaseHelper.COLUMN_IS_IN_FAVORITES, if (deviantPicture.isInFavorites) 1 else 0)
            put(DatabaseHelper.COLUMN_SETTING, setting)
        }
        //Кладем фильм в БД
        sqlDb.insert(DatabaseHelper.TABLE_NAME, null, cv)
    }

    fun getAllFromDB(): List<DeviantPicture> {
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        //Сюда будем сохранять результат получения данных
        val result = mutableListOf<DeviantPicture>()
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()) {
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val author = cursor.getString(2)
                val picture = cursor.getInt(3)
                val description = cursor.getString(4)
                val url = cursor.getString(5)
                val countFavorites = cursor.getInt(6)
                val comments = cursor.getInt(7)
                val countViews = cursor.getInt(8)
                val isInFavorites = cursor.getInt(9)

                result.add(
                    DeviantPicture(
                        "",
                        title,
                        author,
                        picture,
                        description,
                        url,
                        "",
                        countFavorites,
                        comments,
                        countViews,
                        isInFavorites != 0
                    )
                )
            } while (cursor.moveToNext())
        }
        //Возвращаем список фильмов
        return result
    }

    fun getCategoryFromDB(setting: String): List<DeviantPicture> {
        println("getCategoryFromDB")
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_SETTING} = ?", arrayOf(setting))
        val result = mutableListOf<DeviantPicture>()

        if (cursor.moveToFirst()) {
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val author = cursor.getString(2)
                val picture = cursor.getInt(3)
                val description = cursor.getString(4)
                val url = cursor.getString(5)
                val countFavorites = cursor.getInt(6)
                val comments = cursor.getInt(7)
                val countViews = cursor.getInt(8)
                val isInFavorites = cursor.getInt(9)

                result.add(
                    DeviantPicture(
                        "",
                        title,
                        author,
                        picture,
                        description,
                        url,
                        "",
                        countFavorites,
                        comments,
                        countViews,
                        isInFavorites != 0
                    )
                )
            } while (cursor.moveToNext())
        }
        return result
    }

    fun deleteFromDB(deviantPicture: DeviantPicture){
        sqlDb.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_TITLE + " = ?", arrayOf(deviantPicture.title))
    }
}