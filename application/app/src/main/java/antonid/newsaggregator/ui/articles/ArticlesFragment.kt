package antonid.newsaggregator.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import antonid.newsaggregator.data.ArticlesRepositoryImpl
import antonid.newsaggregator.data.remote.ArticlesRemoteRepository
import antonid.newsaggregator.data.remote.newsapi.NewsApiOrgRemoteDataSource
import antonid.newsaggregator.data.remote.newsapi.converter.NewsApiArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsapi.retrofit.NewsApiProvider
import antonid.newsaggregator.data.remote.newsdata.TheNewsApiComRemoteDataSource
import antonid.newsaggregator.data.remote.newsdata.converter.TheNewsApiComArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsdata.retrofit.TheNewsApiComApiProvider
import antonid.newsaggregator.databinding.ArticlesFragmentBinding
import antonid.newsaggregator.domain.LoadArticlesInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArticlesFragment: Fragment() {

    private var binding: ArticlesFragmentBinding? = null

    private val viewModel: ArticlesViewModel by viewModels(
        factoryProducer = { ArticlesViewModelFactory() }
    )

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

        val adapter = ArticlesRecyclerAdapter()
        binding?.articles?.adapter = adapter
        binding?.articles?.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticlesUpdates().collect {
                adapter.addArticles(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}