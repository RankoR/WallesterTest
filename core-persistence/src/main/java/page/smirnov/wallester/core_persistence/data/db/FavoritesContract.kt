package page.smirnov.wallester.core_persistence.data.db

import android.provider.BaseColumns

object FavoritesContract {

    object FavoriteBeer : BaseColumns {
        const val TABLE_NAME = "favorites"

        const val COLUMN_NAME_BEER_ID = "beer_id"
        const val COLUMN_NAME_BEER_NAME = "beer_name"
        const val COLUMN_NAME_ABV = "abv"
        const val COLUMN_NAME_EBC = "ebc"
        const val COLUMN_NAME_IBU = "ibu"

        const val CREATE_STATEMENT = """
            CREATE TABLE $TABLE_NAME (
                ${BaseColumns._ID} INTEGER PRIMARY KEY,
                $COLUMN_NAME_BEER_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_NAME_BEER_NAME TEXT NOT NULL,
                $COLUMN_NAME_ABV REAL NOT NULL,
                $COLUMN_NAME_EBC REAL NOT NULL,
                $COLUMN_NAME_IBU REAL NOT NULL
            )
        """

        const val EXISTS_STATEMENT_FORMAT = """
            SELECT EXISTS (SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME_BEER_ID='%d' LIMIT 1)
        """

        val selectionColumns = arrayOf(
            COLUMN_NAME_BEER_ID,
            COLUMN_NAME_BEER_NAME,
            COLUMN_NAME_ABV,
            COLUMN_NAME_EBC,
            COLUMN_NAME_IBU
        )
    }
}
