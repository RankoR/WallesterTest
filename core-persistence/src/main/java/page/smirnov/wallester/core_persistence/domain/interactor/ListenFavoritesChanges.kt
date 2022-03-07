package page.smirnov.wallester.core_persistence.domain.interactor

import kotlinx.coroutines.flow.Flow
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository

interface ListenFavoritesChanges {
    suspend fun exec(): Flow<Unit>
}

class ListenFavoritesChangesImpl(
    private val favoritesRepository: FavoritesRepository
) : ListenFavoritesChanges {

    override suspend fun exec(): Flow<Unit> {
        return favoritesRepository.changesFlow
    }
}
