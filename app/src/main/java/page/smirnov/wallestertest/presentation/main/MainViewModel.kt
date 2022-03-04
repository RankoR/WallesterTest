package page.smirnov.wallestertest.presentation.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_ui.presentation.BaseViewModel

internal class MainViewModel : BaseViewModel() {

    private val _currentTab = MutableStateFlow(MainTab.LIST)
    val currentTab: StateFlow<MainTab> = _currentTab

    internal fun onTabSelected(tab: MainTab) {
        Log.d("WTEST", "onTabSelected: tab=$tab")

        viewModelScope.launch {
            _currentTab.emit(tab)
        }
    }
}
