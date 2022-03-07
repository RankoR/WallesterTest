package page.smirnov.wallestertest.presentation.favorite

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.data.model.SortingMode
import page.smirnov.wallestertest.databinding.FragmentFavoritesBinding
import page.smirnov.wallestertest.presentation.detail.DetailFragment

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    override val screenName: String = SCREEN_NAME

    override val viewModel: FavoritesViewModel by viewModels()

    override val loaderView: View? by lazy { binding?.progressBar }

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
        setupSpinner()
    }

    private fun setupRecyclerView() {
        favoritesAdapter.onItemClick = viewModel::onBeerClick

        binding?.beersRv?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun setupSpinner() {
        binding?.sortingSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortingMode = when (position) {
                    0 -> SortingMode.NAME
                    1 -> SortingMode.ABV
                    2 -> SortingMode.EBC
                    3 -> SortingMode.IBU
                    else -> throw IllegalArgumentException("Unknown position = $position")
                }

                viewModel.sortingMode = sortingMode
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        ArrayAdapter.createFromResource(
            context ?: return,
            R.array.favorites_sorting_modes,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }.let { adapter ->
            binding?.sortingSpinner?.adapter = adapter
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
