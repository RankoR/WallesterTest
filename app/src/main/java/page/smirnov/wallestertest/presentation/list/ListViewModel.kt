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

    private val _beerList = MutableStateFlow<List<Beer>>(emptyList())
    val beerList: Flow<List<Beer>> = _beerList

    private val _openBeerScreen = MutableSharedFlow<Beer>()
    val openBeerScreen = _openBeerScreen.asSharedFlow() // Normally this would be handled by some nav library like Cicerone

    private var beers: List<Beer> = emptyList() // Can be solved more gracefully with a pagination library

    private var currentPage = 0 // Normally we should use a pagination library, but it's an overkill for such project

    private var isLoading = false

    init {
        loadNextPage()
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
                    _beerList.emit(beers)
                }
                .onFinish { isLoading = false }
        }
    }

    internal fun onBeerClick(beer: Beer) {
        Log.i("WTEST", "Beer: $beer")

        viewModelScope.launch {
            _openBeerScreen.emit(beer)
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
