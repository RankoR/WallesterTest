package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.FavoriteBeer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface RemoveFavorite {
    suspend fun exec(favoriteBeer: FavoriteBeer): Result<Unit>
}

class RemoveFavoriteImpl(
    private val favoritesRepository: FavoritesRepository
) : RemoveFavorite {

    override suspend fun exec(favoriteBeer: FavoriteBeer): Result<Unit> {
        return favoritesRepository.removeFavoriteBeer(favoriteBeer)
    }
}
