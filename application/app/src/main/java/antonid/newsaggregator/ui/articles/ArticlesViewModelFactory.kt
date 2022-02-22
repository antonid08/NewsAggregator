package antonid.newsaggregator.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import antonid.newsaggregator.data.ArticlesRepositoryImpl
import antonid.newsaggregator.data.local.ArticlesLocalDataSource
import antonid.newsaggregator.data.remote.ArticlesRemoteRepository
import antonid.newsaggregator.data.remote.newsapi.NewsApiOrgRemoteDataSource
import antonid.newsaggregator.data.remote.newsapi.converter.NewsApiArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsapi.retrofit.NewsApiProvider
import antonid.newsaggregator.data.remote.newsdata.TheNewsApiComRemoteDataSource
import antonid.newsaggregator.data.remote.newsdata.converter.TheNewsApiComArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsdata.retrofit.TheNewsApiComApiProvider
import java.lang.IllegalArgumentException

class ArticlesViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            val newsApiOrgRemoteDataSource = NewsApiOrgRemoteDataSource(NewsApiProvider.api, NewsApiArticleRetrofitConverter())
            val theNewsApiComRemoteDataSource = TheNewsApiComRemoteDataSource(
                TheNewsApiComApiProvider.api, TheNewsApiComArticleRetrofitConverter()
            )
            val articlesRepository = ArticlesRepositoryImpl(
                ArticlesRemoteRepository(newsApiOrgRemoteDataSource, theNewsApiComRemoteDataSource),
                ArticlesLocalDataSource()
            )
            return ArticlesViewModel(
                articlesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}