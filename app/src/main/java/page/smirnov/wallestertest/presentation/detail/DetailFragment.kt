package page.smirnov.wallestertest.presentation.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallester.core_ui.util.setOnSingleClickListener
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    override val screenName: String = SCREEN_NAME
    override val viewModel: DetailViewModel by viewModels()

    override fun setupView() {
        super.setupView()

        binding?.favoriteIv?.setOnSingleClickListener {
            viewModel.toggleFavorite()
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()

        launchRepeatingOn(Lifecycle.State.STARTED) {
            launch { viewModel.beer.collect(::showBeer) }
        }

        initializeBeer()
    }

    private fun initializeBeer() {
        arguments?.getParcelable<Beer>(ARG_BEER)?.let(viewModel::setBeer)
    }

    private fun showBeer(beer: Beer) {
        binding?.apply {
            nameTv.text = beer.name
            alcoholTv.text = getString(R.string.format_abv, beer.abv)
            ebcTv.text = getString(R.string.format_ebc, beer.ebc)
            ibuTv.text = getString(R.string.format_ibu, beer.ibu)

            val favoriteResId = if (beer.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
            favoriteIv.setImageResource(favoriteResId)
        }

        updateToolbar {
            title = beer.name
        }
    }

    companion object {
        private const val SCREEN_NAME = "Detail"

        private const val ARG_BEER = "beer"

        fun newInstance(beer: Beer): DetailFragment {
            return DetailFragment().apply {
                arguments = bundleOf(
                    ARG_BEER to beer
                )
            }
        }
    }
}
