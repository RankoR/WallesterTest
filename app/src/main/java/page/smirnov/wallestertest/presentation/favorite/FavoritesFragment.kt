package page.smirnov.wallestertest.presentation.favorite

import android.util.Log
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.FragmentFavoritesBinding
import page.smirnov.wallestertest.presentation.detail.DetailFragment

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    override val screenName: String = SCREEN_NAME

    override val viewModel: FavoritesViewModel by viewModels()

    private val favoritesAdapter = FavoritesAdapter()

    override fun onStart() {
        super.onStart()

        updateToolbar {
            title = getString(R.string.title_screen_favorites)
        }
    }

    override fun setupView() {
        super.setupView()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        favoritesAdapter.onItemClick = viewModel::onBeerClick

        binding?.beersRv?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()

        launchRepeatingOn(Lifecycle.State.CREATED) {
            launch { viewModel.beerList.collect(::showBeers) }
            launch { viewModel.openBeerScreen.collect(::openBeerScreen) }
        }
    }

    private fun showBeers(beers: List<Beer>) {
        Log.i("WTEST", "Got ${beers.size} beers")
        favoritesAdapter.beers = beers
    }

    private fun openBeerScreen(beer: Beer) {
        Log.i("WTEST", "Open: $beer")
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, DetailFragment.newInstance(beer))
            addToBackStack(DetailFragment::class.java.simpleName)
        }
    }

    companion object {
        private const val SCREEN_NAME = "Favorites"

        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}
