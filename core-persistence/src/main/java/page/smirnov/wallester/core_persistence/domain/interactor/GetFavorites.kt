package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface GetFavorites {
    suspend fun exec(): Result<List<Beer>>
}

class GetFavoritesImpl(
    private val favoritesRepository: FavoritesRepository
) : GetFavorites {

    override suspend fun exec(): Result<List<Beer>> {
        return favoritesRepository.getFavoriteBeers()
    }
}
