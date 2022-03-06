package page.smirnov.wallestertest.presentation.list

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.databinding.FragmentListBinding

class ListFragment : BaseFragment<FragmentListBinding>(FragmentListBinding::inflate) {

    override val screenName: String = SCREEN_NAME

    override val viewModel: ListViewModel by viewModels()

    private val beersAdapter = BeersListAdapter()

    override fun setupView() {
        super.setupView()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        beersAdapter.onItemClick = viewModel::onBeerClick

        binding?.beersRv?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = beersAdapter
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.beerList.collect(::showBeers) }
            }
        }
    }

    private fun showBeers(beers: List<Beer>) {
        beersAdapter.beers = beers
    }

    companion object {
        private const val SCREEN_NAME = "List"

        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}
