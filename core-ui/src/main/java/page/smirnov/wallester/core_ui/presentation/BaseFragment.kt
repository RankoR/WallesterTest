package page.smirnov.wallester.core_ui.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    protected abstract val screenName: String // TODO: For analytics

    protected var binding: VB? = null

    protected abstract val viewModel: BaseViewModel?

    protected open val loaderView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflate
            .invoke(inflater, container, false)
            .also { binding = it }
            .let(::requireNotNull)
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
    }

    protected open fun setupView() {}

    protected open fun setupViewModel() {
        viewModel?.apply {
            launchRepeatingOn(Lifecycle.State.STARTED) {
                launch { showErrorMessage.collect { this@BaseFragment.showErrorMessage(it) } }
                launch { showLoading.collect(::showLoading) }
            }
        }
    }

    protected fun launchRepeatingOn(state: Lifecycle.State, block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state, block)
        }
    }

    override fun onStart() {
        super.onStart()

        reportScreenViewStart()
    }

    override fun onStop() {
        reportScreenViewEnd()

        super.onStop()
    }

    override fun onDestroyView() {
        binding = null

        super.onDestroyView()
    }

    protected open fun reportScreenViewStart() {
        activity?.let {
            // TODO
            // Analytics.reportScreenViewStart(it, screenName)
        }
    }

    protected open fun reportScreenViewEnd() {
        // TODO
        // Analytics.reportScreenViewEnd(screenName)
    }

    protected open fun showLoading(isLoading: Boolean) {
        Log.v("TEST", "Is loading: $isLoading")

        loaderView?.isVisible = true
    }

    protected open fun showErrorMessage(message: String) {
        Log.v("TEST", "Show error")

        showToast(message) // TODO

        loaderView?.isVisible = false
    }

    protected open fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }
}

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
