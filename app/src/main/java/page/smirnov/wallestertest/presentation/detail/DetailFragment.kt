package page.smirnov.wallestertest.presentation.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    override val screenName: String = SCREEN_NAME
    override val viewModel: DetailViewModel by viewModels()

    override fun setupView() {
        super.setupView()
    }

    override fun setupViewModel() {
        super.setupViewModel()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.beer.collect(::showBeer) }
            }
        }

        initializeBeer()
    }

    private fun initializeBeer() {
        arguments?.getParcelable<Beer>(ARG_BEER)?.let(viewModel::setBeer)
    }

    private fun showBeer(beer: Beer) {
        binding?.apply {
            nameTv.text = beer.name
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
