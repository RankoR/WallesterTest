package page.smirnov.wallestertest.presentation.favorite

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import page.smirnov.wallester.core.util.extension.onFailureLog
import page.smirnov.wallester.core.util.extension.onFinish
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepositoryHolder
import page.smirnov.wallester.core_persistence.domain.interactor.GetFavorites
import page.smirnov.wallester.core_persistence.domain.interactor.GetFavoritesImpl
import page.smirnov.wallester.core_persistence.domain.interactor.ListenFavoritesChanges
import page.smirnov.wallester.core_persistence.domain.interactor.ListenFavoritesChangesImpl
import page.smirnov.wallester.core_ui.presentation.BaseViewModel

class FavoritesViewModel : BaseViewModel() {

    // DI
    private val listenFavoritesChanges: ListenFavoritesChanges = ListenFavoritesChangesImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val getFavorites: GetFavorites = GetFavoritesImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    private val _beerList = MutableStateFlow<List<Beer>>(emptyList())
    val beerList: Flow<List<Beer>> = _beerList

    private val _openBeerScreen = MutableSharedFlow<Beer>()
    val openBeerScreen = _openBeerScreen.asSharedFlow() // Normally this would be handled by some nav library like Cicerone

    init {
        loadFavorites()
        listenForFavoriteChanges()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            setIsLoading(true)

            getFavorites
                .exec()
                .onFailureLog()
                .onFailure(::showErrorMessage)
                .onSuccess { beers ->
                    Log.i("WTEST", "Loaded ${beers.size} favorite beers")

                    _beerList.emit(beers)
                }
                .onFinish { setIsLoading(false) }
        }
    }

    private fun listenForFavoriteChanges() {
        viewModelScope.launch {
            listenFavoritesChanges.exec().collect {
                Log.i("WTEST", "Favorites: Favorites updated")

                loadFavorites()
            }
        }
    }

    internal fun onBeerClick(beer: Beer) {
        Log.i("WTEST", "Beer: $beer")

        viewModelScope.launch {
            _openBeerScreen.emit(beer)
        }
    }
}
