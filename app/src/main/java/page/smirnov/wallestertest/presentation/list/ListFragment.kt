package page.smirnov.wallestertest.presentation.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.FragmentListBinding
import page.smirnov.wallestertest.presentation.detail.DetailFragment

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("WTEST", "onViewCreated")

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        updateToolbar {
            title = getString(R.string.title_screen_list)
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
        beersAdapter.beers = beers
    }

    private fun openBeerScreen(beer: Beer) {
        Log.i("WTEST", "Open: $beer")
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, DetailFragment.newInstance(beer))
            addToBackStack(DetailFragment::class.java.simpleName)
        }
    }

    companion object {
        private const val SCREEN_NAME = "List"

        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}
