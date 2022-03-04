package page.smirnov.wallestertest.presentation.list

import androidx.fragment.app.viewModels
import page.smirnov.wallester.core_ui.presentation.BaseFragment
import page.smirnov.wallestertest.databinding.FragmentListBinding

class ListFragment : BaseFragment<FragmentListBinding>(FragmentListBinding::inflate) {

    override val screenName: String = SCREEN_NAME

    override val viewModel: ListViewModel? by viewModels()

    companion object {
        private const val SCREEN_NAME = "List"

        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}
