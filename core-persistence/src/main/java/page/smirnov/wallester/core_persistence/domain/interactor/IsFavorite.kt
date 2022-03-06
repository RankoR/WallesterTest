package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.FavoriteBeer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface IsFavorite {
    suspend fun exec(favoriteBeer: FavoriteBeer): Result<Boolean>
}

class IsFavoriteImpl(
    private val favoritesRepository: FavoritesRepository
) : IsFavorite {

    override suspend fun exec(favoriteBeer: FavoriteBeer): Result<Boolean> {
        return favoritesRepository.isFavorite(favoriteBeer)
    }
}
