package page.smirnov.wallester.core_persistence.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface AddFavorite {
    suspend fun exec(beer: Beer): Result<Unit>
}

class AddFavoriteImpl(
    private val favoritesRepository: FavoritesRepository
) : AddFavorite {

    override suspend fun exec(beer: Beer): Result<Unit> {
        return favoritesRepository.addFavoriteBeer(beer)
    }
}
