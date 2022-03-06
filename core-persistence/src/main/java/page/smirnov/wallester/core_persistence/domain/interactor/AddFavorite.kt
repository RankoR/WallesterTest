package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.FavoriteBeer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface AddFavorite {
    suspend fun exec(favoriteBeer: FavoriteBeer): Result<Unit>
}

class AddFavoriteImpl(
    private val favoritesRepository: FavoritesRepository
) : AddFavorite {

    override suspend fun exec(favoriteBeer: FavoriteBeer): Result<Unit> {
        return favoritesRepository.addFavoriteBeer(favoriteBeer)
    }
}
