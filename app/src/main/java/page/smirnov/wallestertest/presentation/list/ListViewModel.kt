package page.smirnov.wallestertest.presentation.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import page.smirnov.wallester.core.util.extension.onFailureLog
import page.smirnov.wallester.core.util.extension.onFinish
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_network.data.parser.beer.BeerListParserImpl
import page.smirnov.wallester.core_network.domain.BuildUrlImpl
import page.smirnov.wallester.core_network.domain.MakeGetRequestImpl
import page.smirnov.wallester.core_network.domain.beer.GetBeerList
import page.smirnov.wallester.core_network.domain.beer.GetBeerListImpl
import page.smirnov.wallester.core_persistence.data.model.FavoriteBeer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepositoryHolder
import page.smirnov.wallester.core_persistence.domain.interactor.AddFavorite
import page.smirnov.wallester.core_persistence.domain.interactor.AddFavoriteImpl
import page.smirnov.wallester.core_persistence.domain.interactor.GetFavorites
import page.smirnov.wallester.core_persistence.domain.interactor.GetFavoritesImpl
import page.smirnov.wallester.core_persistence.domain.interactor.ListenFavoritesChanges
import page.smirnov.wallester.core_persistence.domain.interactor.ListenFavoritesChangesImpl
import page.smirnov.wallester.core_persistence.domain.interactor.RemoveFavorite
import page.smirnov.wallester.core_persistence.domain.interactor.RemoveFavoriteImpl
import page.smirnov.wallester.core_ui.presentation.BaseViewModel

class ListViewModel : BaseViewModel() {

    /**
     * It's very painful to write such code, as it MUST be injected by a DI
     * But guess what?
     * We can't use it :)
     */
    private val getBeerList: GetBeerList = GetBeerListImpl(
        makeGetRequest = MakeGetRequestImpl(
            buildUrl = BuildUrlImpl(),
            coroutineDispatcher = Dispatchers.IO
        ),
        beerListParser = BeerListParserImpl()
    )

    // DI
    private val listenFavoritesChanges: ListenFavoritesChanges = ListenFavoritesChangesImpl(
        favoritesRepository = FavoritesRepositoryHolder.favoritesRepository
    )

    // DI
    private val getFavorites: GetFavorites = GetFavoritesImpl(
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

    private val _beerList = MutableStateFlow<List<Beer>>(emptyList())
    val beerList: Flow<List<Beer>> = _beerList

    private val _openBeerScreen = MutableSharedFlow<Beer>()
    val openBeerScreen = _openBeerScreen.asSharedFlow() // Normally this would be handled by some nav library like Cicerone

    private var beers: List<Beer> = emptyList() // Can be solved more gracefully with a pagination library

    private var currentPage = 0 // Normally we should use a pagination library, but it's an overkill for such project

    private var isLoading = false

    init {
        loadNextPage()
        listenForFavoriteChanges()
    }

    private fun listenForFavoriteChanges() {
        viewModelScope.launch {
            listenFavoritesChanges.exec().collect {
                Log.i("WTEST", "List: Favorites updated")

                updateFavoritesState()
            }
        }
    }

    private fun loadNextPage() {
        isLoading = true
        currentPage++

        Log.i("WTEST", "Loading page: $currentPage")

        viewModelScope.launch {
            getBeerList
                .exec(currentPage)
                .onFailureLog()
                .onFailure(::showErrorMessage)
                .onSuccess { beersOnPage ->
                    Log.i("WTEST", "Loaded ${beersOnPage.size} beers")

                    beers = beers + beersOnPage

                    // Future improvement: update only for new items
                    updateFavoritesState()
                }
                .onFinish { isLoading = false }
        }
    }

    private fun updateFavoritesState() {
        viewModelScope.launch {
            getFavorites
                .exec()
                .onFailureLog()
                .onSuccess { favorites ->
                    beers = beers.map { beer ->
                        val isFavorite = favorites.find { it.id == beer.id } != null

                        if (beer.isFavorite != isFavorite) {
                            beer.copy(isFavorite = isFavorite)
                        } else {
                            beer
                        }
                    }

                    _beerList.emit(beers)
                }
        }
    }

    internal fun onBeerClick(beer: Beer) {
        Log.i("WTEST", "Beer: $beer")

        viewModelScope.launch {
            _openBeerScreen.emit(beer)
        }
    }

    internal fun toggleFavorite(beer: Beer) {
        viewModelScope.launch {
            if (beer.isFavorite) {
                removeFavorite.exec(FavoriteBeer(id = beer.id))
            } else {
                addFavorite.exec(FavoriteBeer(id = beer.id))
            }
        }
    }

    /**
     * Very naive impl of pagination
     */
    internal fun onScrollPositionChanged(position: Int) {
        if (position == beers.size - 1) {
            Log.i("WTEST", "Scrolled to last position: $position")

            if (!isLoading) {
                Log.i("WTEST", "Load next page")

                loadNextPage()
            }
        }
    }
}
