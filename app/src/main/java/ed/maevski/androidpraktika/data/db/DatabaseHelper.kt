package ed.maevski.androidpraktika.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        //Создаем саму таблицу для фильмов
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT UNIQUE," +
                    "$COLUMN_AUTHOR TEXT," +
                    "$COLUMN_PICTURE INTEGER," +
                    "$COLUMN_DESCRIPTION TEXT," +
                    "$COLUMN_URL TEXT," +
                    "$COLUMN_COUNT_FAVORITES INTEGER," +
                    "$COLUMN_COMMENTS INTEGER," +
                    "$COLUMN_COUNT_VIEWS INTEGER," +
                    "$COLUMN_IS_IN_FAVORITES INTEGER," +
                    "$COLUMN_SETTING TEXT);"
        )
    }
    //Миграций мы не предполагаем, поэтому метод пустой
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        //Название самой БД
        private const val DATABASE_NAME = "deviant.db"
        //Версия БД
        private const val DATABASE_VERSION = 1

        //Константы для работы с таблицей, они нам понадобятся в CRUD операциях и,
        //возможно, в составлении запросов
        const val TABLE_NAME = "arts_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_PICTURE = "picture"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_URL = "url"
        const val COLUMN_COUNT_FAVORITES = "countFavorites"
        const val COLUMN_COMMENTS = "comments"
        const val COLUMN_COUNT_VIEWS = "countViews"
        const val COLUMN_IS_IN_FAVORITES = "isInFavorites"
        const val COLUMN_SETTING = "setting"
    }
}