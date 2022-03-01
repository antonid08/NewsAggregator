package antonid.newsaggregator.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import antonid.newsaggregator.R
import antonid.newsaggregator.databinding.ArticlesFragmentBinding
import antonid.newsaggregator.ui.utils.Outcome
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.paginate.Paginate


class ArticlesFragment : Fragment() {

    private var binding: ArticlesFragmentBinding? = null

    private val viewModel: ArticlesViewModel by viewModels(
        factoryProducer = { ArticlesViewModelFactory(requireContext()) }
    )

    val adapter = ArticlesRecyclerAdapter()

    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return with(ArticlesFragmentBinding.inflate(inflater, container, false)) {
            binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.articles?.adapter = adapter
        binding?.articles?.layoutManager = LinearLayoutManager(requireContext())


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getInitialArticlesUpdates().collect {
                when (it) {
                    is Outcome.Progress -> setLoading(it.isLoading, true)
                    is Outcome.Success -> adapter.setArticles(it.data)
                    is Outcome.Failure -> {
                        setLoading(false, true)
                        showError()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticlesUpdates().collect {
                when (it) {
                    is Outcome.Progress -> setLoading(it.isLoading, false)
                    is Outcome.Success -> adapter.addArticles(it.data)
                    is Outcome.Failure ->  {
                        setLoading(false, false)
                        showError()
                    }
                }
            }
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.refreshArticles()
        }

        viewModel.loadInitialArticles()

        Paginate.with(binding?.articles, paginationCallbacks)
            .setLoadingTriggerThreshold(2)
            .addLoadingListItem(true)
            .build()
    }

    private fun setLoading(isLoading: Boolean, isInitialPage: Boolean) {
        this.isLoading = isLoading

        if (isInitialPage) {
            binding?.progress?.isVisible = isLoading
            binding?.articles?.isVisible = !isLoading
        }

        if (!isLoading) {
            binding?.swipeRefresh?.isRefreshing = isLoading
        }
    }

    private fun showError() {
        binding?.root?.let { root ->
            Snackbar.make(root, R.string.articles_error_message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private var paginationCallbacks: Paginate.Callbacks = object : Paginate.Callbacks {
        override fun onLoadMore() {
            viewModel.loadArticlesPage(adapter.getArticles().last().publishTimestamp)
        }

        override fun isLoading() = this@ArticlesFragment.isLoading

        override fun hasLoadedAllItems() = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}