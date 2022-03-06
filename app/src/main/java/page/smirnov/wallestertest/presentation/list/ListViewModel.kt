package page.smirnov.wallestertest.presentation.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import page.smirnov.wallester.core.util.extension.onFailureLog
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

    private var beers: List<Beer> = emptyList() // Can be solved more gracefully with a pagination library

    private var currentPage = 0 // Normally we should use a pagination library, but it's an overkill for such project

    init {
        loadNextPage()
    }

    private fun loadNextPage() {
        currentPage++

        viewModelScope.launch {
            getBeerList
                .exec(currentPage)
                .onFailureLog()
                .onFailure(::showErrorMessage)
                .onSuccess { beersOnPage ->
                    beers = beers + beersOnPage
                    _beerList.emit(beers)
                }
        }
    }

    internal fun onBeerClick(beer: Beer) {
        Log.i("WTEST", "Beer: $beer")
    }

    private suspend fun <T> MutableSharedFlow<List<T>>.addAll(list: List<T>) {
        val lastList = lastOrNull()?.toMutableList() ?: mutableListOf()

        lastList.addAll(list)

        emit(lastList)
    }
}
