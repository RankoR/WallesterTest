package page.smirnov.wallestertest.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.ActivityMainBinding
import page.smirnov.wallestertest.presentation.favorite.FavoritesFragment
import page.smirnov.wallestertest.presentation.list.ListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding
            .inflate(layoutInflater)
            .also { binding = it }
            .root
            .let(::setContentView)

        setupUi()
        setupViewModel()
    }

    private fun setupUi() {
        setupBottomBar()
    }

    private fun setupBottomBar() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val tab = when (item.itemId) {
                R.id.list -> MainTab.LIST
                R.id.favorites -> MainTab.FAVORITES
                else -> throw IllegalArgumentException("Unknown item with id = ${item.itemId}")
            }

            viewModel.onTabSelected(tab)

            true
        }
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.currentTab.collectLatest(::showTab)
                }
            }
        }
    }

    private fun showTab(tab: MainTab) {
        Log.d("WTEST", "New tab: $tab")

        // For now, the navigation is pretty ugly as we use no libs
        // Usually it should be something like Cicerone that handles it nicely
        // There is no backstack between tabs
        // But implementing «good» and correct navigation looks like a pretty large test task itself
        // So, I use a very simple solution:

        val fragment = when (tab) {
            MainTab.LIST -> ListFragment.newInstance()
            MainTab.FAVORITES -> FavoritesFragment.newInstance()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
