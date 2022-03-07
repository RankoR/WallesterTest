package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface RemoveFavorite {
    suspend fun exec(beer: Beer): Result<Unit>
}

class RemoveFavoriteImpl(
    private val favoritesRepository: FavoritesRepository
) : RemoveFavorite {

    override suspend fun exec(beer: Beer): Result<Unit> {
        return favoritesRepository.removeFavoriteBeer(beer)
    }
}
