package page.smirnov.wallestertest.presentation.detail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import page.smirnov.wallester.core.util.extension.onFailureLog
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepositoryHolder
import page.smirnov.wallester.core_persistence.domain.interactor.AddFavorite
import page.smirnov.wallester.core_persistence.domain.interactor.AddFavoriteImpl
import page.smirnov.wallester.core_persistence.domain.interactor.IsFavorite
import page.smirnov.wallester.core_persistence.domain.interactor.IsFavoriteImpl
import page.smirnov.wallester.core_persistence.domain.interactor.ListenFavoritesChanges
import page.smirnov.wallester.core_persistence.domain.interactor.ListenFavoritesChangesImpl
import page.smirnov.wallester.core_persistence.domain.interactor.RemoveFavorite
import page.smirnov.wallester.core_persistence.domain.interactor.RemoveFavoriteImpl
import page.smirnov.wallester.core_ui.presentation.BaseViewModel

class DetailViewModel : BaseViewModel() {

    // DI
    private val listenFavoritesChanges: ListenFavoritesChanges = ListenFavoritesChangesImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val isFavorite: IsFavorite = IsFavoriteImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val addFavorite: AddFavorite = AddFavoriteImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val removeFavorite: RemoveFavorite = RemoveFavoriteImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    private var internalBeer: Beer? = null

    private val _beer: MutableSharedFlow<Beer> = MutableSharedFlow(replay = 1)
    val beer: Flow<Beer> = _beer.asSharedFlow()

    init {
        listenForFavoriteChanges()
    }

    private fun listenForFavoriteChanges() {
        viewModelScope.launch {
            listenFavoritesChanges.exec().collect {
                updateFavoriteState()
            }
        }
    }

    private fun updateFavoriteState() {
        viewModelScope.launch {
            internalBeer?.let { beer ->
                isFavorite
                    .exec(beer)
                    .onFailureLog()
                    .onSuccess { isFavorite ->
                        if (beer.isFavorite != isFavorite) {
                            internalBeer = beer
                                .copy(isFavorite = isFavorite)
                                .also { _beer.emit(it) }
                        }
                    }
            }
        }
    }

    internal fun setBeer(beer: Beer) {
        internalBeer = beer

        viewModelScope.launch {
            _beer.emit(beer)
        }
    }

    internal fun toggleFavorite() {
        viewModelScope.launch {
            internalBeer?.let { beer ->
                if (beer.isFavorite) {
                    removeFavorite.exec(beer)
                } else {
                    addFavorite.exec(beer)
                }
            }
        }
    }
}
