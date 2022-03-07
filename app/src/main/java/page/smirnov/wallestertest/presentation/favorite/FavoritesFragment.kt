package page.smirnov.wallestertest.presentation.favorite

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.FragmentFavoritesBinding

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
    }

    companion object {
        private const val SCREEN_NAME = "Favorites"

        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}
