package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface IsFavorite {
    suspend fun exec(beer: Beer): Result<Boolean>
}

class IsFavoriteImpl(
    private val favoritesRepository: FavoritesRepository
) : IsFavorite {

    override suspend fun exec(beer: Beer): Result<Boolean> {
        return favoritesRepository.isFavorite(beer)
    }
}
