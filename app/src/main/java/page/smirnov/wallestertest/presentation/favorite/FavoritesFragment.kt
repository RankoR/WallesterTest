package page.smirnov.wallestertest.presentation.favorite

import androidx.fragment.app.viewModels
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.FragmentFavoritesBinding

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    override val screenName: String = SCREEN_NAME

    override val viewModel: FavoritesViewModel? by viewModels()

    override fun onStart() {
        super.onStart()

        updateToolbar {
            title = getString(R.string.title_screen_favorites)
        }
    }

    companion object {
        private const val SCREEN_NAME = "Favorites"

        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}
