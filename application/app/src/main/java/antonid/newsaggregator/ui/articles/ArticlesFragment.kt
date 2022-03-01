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
import antonid.newsaggregator.databinding.ArticlesFragmentBinding
import kotlinx.coroutines.launch
import com.paginate.Paginate




class ArticlesFragment: Fragment() {

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
            viewModel.getFirstPageLoadingFlow().collect { isLoading ->
                this@ArticlesFragment.isLoading = isLoading
                binding?.progress?.isVisible = isLoading
                binding?.articles?.isVisible = !isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNextPageLoadingFLow().collect { isLoading ->
                this@ArticlesFragment.isLoading = isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticlesUpdates().collect {
                adapter.addArticles(it)
            }
        }

        viewModel.loadInitialArticles()

        Paginate.with(binding?.articles, paginationCallbacks)
            .setLoadingTriggerThreshold(2)
            .addLoadingListItem(true)
            .build()
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