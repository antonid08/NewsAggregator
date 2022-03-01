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
import java.util.*

class ArticlesViewModel(
    private val articlesRepository: ArticlesRepository,
): ViewModel() {

    companion object {
        private const val CACHE_SIZE = 50
        private const val PAGE_SIZE = 10
    }

    private val articlesUpdatesFlow = MutableSharedFlow<List<Article>>()

    private val isFirstPageLoadingFlow = MutableSharedFlow<Boolean>()
    private val isNextPagesLoadingFlow = MutableSharedFlow<Boolean>()

    fun loadInitialArticles() {
        viewModelScope.launch {
            isFirstPageLoadingFlow.emit(true)
            runCatching {
                val cachedArticles = GetCachedArticlesInteractor(CACHE_SIZE, articlesRepository).execute()
                if (cachedArticles.isNotEmpty()) {
                    cachedArticles
                } else {
                    LoadArticlesInteractor(Calendar.getInstance().timeInMillis, PAGE_SIZE, articlesRepository).execute()
                }
            }.onSuccess {
                articlesUpdatesFlow.emit(it)
            }.onFailure {
                Log.e(TAG, "", it)
                //todo show error
            }
            isFirstPageLoadingFlow.emit(false)
        }
    }

    fun loadArticlesPage(timestampBefore: Long) {
        viewModelScope.launch {
            isNextPagesLoadingFlow.emit(true)
            runCatching {
                LoadArticlesInteractor(timestampBefore, PAGE_SIZE, articlesRepository).execute()
            }.onSuccess {
                articlesUpdatesFlow.emit(it)
            }.onFailure {
                Log.e(TAG, "", it)
                //todo show error
            }
            isNextPagesLoadingFlow.emit(false)
        }
    }

    fun getArticlesUpdates(): Flow<List<Article>> = articlesUpdatesFlow.asSharedFlow()

    fun getFirstPageLoadingFlow(): Flow<Boolean> = isFirstPageLoadingFlow.asSharedFlow()
    fun getNextPageLoadingFLow(): Flow<Boolean> = isNextPagesLoadingFlow.asSharedFlow()


}