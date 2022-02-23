package antonid.newsaggregator.ui.articles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import antonid.newsaggregator.domain.ArticlesRepository
import antonid.newsaggregator.domain.GetCachedArticlesInteractor
import antonid.newsaggregator.domain.LoadArticlesInteractor
import antonid.newsaggregator.domain.model.Article
import antonid.newsaggregator.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ArticlesViewModel(
    private val articlesRepository: ArticlesRepository,
): ViewModel() {

    companion object {
        private const val CACHE_SIZE = 50
        private const val PAGE_SIZE = 10
    }

    private val articlesFlow = MutableSharedFlow<List<Article>>()

    init {
        viewModelScope.launch {
            runCatching {
                val cachedArticles = GetCachedArticlesInteractor(CACHE_SIZE, articlesRepository).execute()
                if (cachedArticles.isNotEmpty()) {
                    cachedArticles
                } else {
                    LoadArticlesInteractor(Calendar.getInstance().timeInMillis, PAGE_SIZE, articlesRepository).execute()
                }
            }.onSuccess {
                articlesFlow.emit(it)
            }.onFailure {
                Log.e(TAG, "", it)
                //todo show error
            }
        }
    }

    fun getArticlesUpdates(): Flow<List<Article>> = articlesFlow.asSharedFlow()


}