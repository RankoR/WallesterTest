package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.FavoriteBeer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface GetFavorites {
    suspend fun exec(): Result<List<FavoriteBeer>>
}

class GetFavoritesImpl(
    private val favoritesRepository: FavoritesRepository
) : GetFavorites {

    override suspend fun exec(): Result<List<FavoriteBeer>> {
        return favoritesRepository.getFavoriteBeers()
    }
}
