package page.smirnov.wallestertest.presentation.detail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseViewModel

class DetailViewModel : BaseViewModel() {

    private val _beer: MutableSharedFlow<Beer> = MutableSharedFlow()
    val beer: Flow<Beer> = _beer

    fun setBeer(beer: Beer) {
        viewModelScope.launch {
            _beer.emit(beer)
        }
    }
}
