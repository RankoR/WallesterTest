package page.smirnov.wallester.core_persistence.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Normally we should use Room for this
 */
class FavoritesDbHelper(
    context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(FavoritesContract.FavoriteBeer.CREATE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        const val DB_NAME = "favorites.db"
        private const val DB_VERSION = 1
    }
}
