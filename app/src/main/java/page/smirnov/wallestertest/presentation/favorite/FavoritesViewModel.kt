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
import page.smirnov.wallestertest.data.model.SortingMode
import page.smirnov.wallestertest.domain.interactor.SortBeers
import page.smirnov.wallestertest.domain.interactor.SortBeersImpl

class FavoritesViewModel : BaseViewModel() {

    // DI
    private val listenFavoritesChanges: ListenFavoritesChanges = ListenFavoritesChangesImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val getFavorites: GetFavorites = GetFavoritesImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val sortBeers: SortBeers = SortBeersImpl()

    var sortingMode: SortingMode = SortingMode.NAME
        set(value) {
            if (value != field) {
                field = value
                applySorting()
                emitBeers()
            }
        }

    private var beers: List<Beer> = emptyList()

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

                    this@FavoritesViewModel.beers = beers

                    applySorting()
                    emitBeers()
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

    private fun applySorting() {
        Log.i("WTEST", "Sorting mode: $sortingMode")

        beers = sortBeers.exec(beers, sortingMode)
    }

    private fun emitBeers() {
        viewModelScope.launch {
            _beerList.emit(beers)
        }
    }

    internal fun onBeerClick(beer: Beer) {
        Log.i("WTEST", "Beer: $beer")

        viewModelScope.launch {
            _openBeerScreen.emit(beer)
        }
    }
}
