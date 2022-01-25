package antonid.newsaggregator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import antonid.newsaggregator.R
import antonid.newsaggregator.data.ArticlesRepositoryImpl
import antonid.newsaggregator.data.local.ArticlesLocalDataSource
import antonid.newsaggregator.data.remote.ArticlesRemoteRepository
import antonid.newsaggregator.data.remote.newsapi.converter.NewsApiArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsapi.NewsApiOrgRemoteDataSource
import antonid.newsaggregator.data.remote.newsapi.retrofit.NewsApiProvider
import antonid.newsaggregator.data.remote.newsdata.NewsDataIoRemoteDataSource
import antonid.newsaggregator.domain.LoadArticlesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this block is only for testing. it will be removed.
        coroutineScope.launch {
            val articlesRepository = ArticlesRepositoryImpl(ArticlesRemoteRepository(
                NewsApiOrgRemoteDataSource(NewsApiProvider.api, NewsApiArticleRetrofitConverter()),
                NewsDataIoRemoteDataSource()
            ), ArticlesLocalDataSource())
            val articles = LoadArticlesInteractor(Calendar.getInstance().timeInMillis, 10, articlesRepository).execute()
        }
    }
}