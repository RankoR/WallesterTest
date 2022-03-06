package page.smirnov.wallester.core_persistence.data.repository

import android.database.Cursor
import androidx.core.content.contentValuesOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import page.smirnov.wallester.core.util.withContextCatching
import page.smirnov.wallester.core_persistence.data.db.FavoritesContract
import page.smirnov.wallester.core_persistence.data.db.FavoritesDbHelper
import page.smirnov.wallester.core_persistence.data.model.FavoriteBeer

interface FavoritesRepository {
    val changesFlow: Flow<Unit> // Fires when favorites are changed

    suspend fun getFavoriteBeers(): Result<List<FavoriteBeer>>
    suspend fun isFavorite(favoriteBeer: FavoriteBeer): Result<Boolean>
    suspend fun addFavoriteBeer(favoriteBeer: FavoriteBeer): Result<Unit>
    suspend fun removeFavoriteBeer(favoriteBeer: FavoriteBeer): Result<Unit>
}

class FavoritesRepositoryImpl(
    private val favoritesDbHelper: FavoritesDbHelper, // Should be provided with DI
    private val dispatcher: CoroutineDispatcher // Should be provided with DI
) : FavoritesRepository {

    override val changesFlow: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)

    override suspend fun getFavoriteBeers(): Result<List<FavoriteBeer>> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.readableDatabase.use { db ->
                db.query(
                    FavoritesContract.FavoriteBeer.TABLE_NAME,
                    FavoritesContract.FavoriteBeer.selectionColumns,
                    null,
                    null,
                    null,
                    null,
                    null
                ).use { cursor ->
                    cursor.toList {
                        FavoriteBeer(
                            id = getLong(getColumnIndexOrThrow(FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_ID))
                        )
                    }
                }
            }
        }
    }

    override suspend fun isFavorite(favoriteBeer: FavoriteBeer): Result<Boolean> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.readableDatabase.use { db ->
                db.rawQuery(
                    String.format(FavoritesContract.FavoriteBeer.EXISTS_STATEMENT_FORMAT, favoriteBeer.id),
                    null
                ).use { cursor ->
                    if (cursor.moveToFirst()) {
                        cursor.getInt(0) == 1
                    } else {
                        false
                    }
                }
            }
        }
    }

    override suspend fun addFavoriteBeer(favoriteBeer: FavoriteBeer): Result<Unit> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.writableDatabase.use { db ->
                val contentValues = contentValuesOf(
                    FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_ID to favoriteBeer.id
                )

                db.insert(FavoritesContract.FavoriteBeer.TABLE_NAME, null, contentValues)
            }

            notifyFavoritesChanged()
        }
    }

    override suspend fun removeFavoriteBeer(favoriteBeer: FavoriteBeer): Result<Unit> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.writableDatabase.use { db ->
                db.delete(
                    FavoritesContract.FavoriteBeer.TABLE_NAME,
                    "${FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_ID} = ?",
                    arrayOf(favoriteBeer.id.toString())
                )
            }

            notifyFavoritesChanged()
        }
    }

    private suspend fun notifyFavoritesChanged() {
        changesFlow.emit(Unit)
    }

    private inline fun <T> Cursor.toList(crossinline mappingFunction: Cursor.() -> T): List<T> {
        return generateSequence { if (moveToNext()) this else null }
            .map { it.mappingFunction() }
            .toList()
    }
}
