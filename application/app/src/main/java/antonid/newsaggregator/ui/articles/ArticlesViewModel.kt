package antonid.newsaggregator.ui.articles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import antonid.newsaggregator.domain.ArticlesRepository
import antonid.newsaggregator.domain.GetCachedArticlesInteractor
import antonid.newsaggregator.domain.LoadArticlesInteractor
import antonid.newsaggregator.domain.model.Article
import antonid.newsaggregator.ui.utils.Outcome
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

    private val initialArticlesFlow = MutableSharedFlow<Outcome<List<Article>>>()
    private val articlesUpdatesFlow = MutableSharedFlow<Outcome<List<Article>>>()


    fun loadInitialArticles() {
        viewModelScope.launch {
            initialArticlesFlow.emit(Outcome.Progress(true))
            runCatching {
                val cachedArticles = GetCachedArticlesInteractor(CACHE_SIZE, articlesRepository).execute()
                if (cachedArticles.isNotEmpty()) {
                    cachedArticles
                } else {
                    LoadArticlesInteractor(Calendar.getInstance().timeInMillis, PAGE_SIZE, articlesRepository).execute()
                }
            }.onSuccess {
                initialArticlesFlow.emit(Outcome.Success(it))
            }.onFailure {
                Log.e(TAG, "", it)
                initialArticlesFlow.emit(Outcome.Failure())
            }
            initialArticlesFlow.emit(Outcome.Progress(false))
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            runCatching {
                LoadArticlesInteractor(Calendar.getInstance().timeInMillis, PAGE_SIZE, articlesRepository).execute()
            }.onSuccess {
                initialArticlesFlow.emit(Outcome.Success(it))
            }.onFailure {
                Log.e(TAG, "", it)
                initialArticlesFlow.emit(Outcome.Failure())
            }
            initialArticlesFlow.emit(Outcome.Progress(false))
        }
    }

    fun loadArticlesPage(timestampBefore: Long) {
        viewModelScope.launch {
            articlesUpdatesFlow.emit(Outcome.Progress(true))
            runCatching {
                LoadArticlesInteractor(timestampBefore, PAGE_SIZE, articlesRepository).execute()
            }.onSuccess {
                articlesUpdatesFlow.emit(Outcome.Success(it))
            }.onFailure {
                Log.e(TAG, "", it)
                articlesUpdatesFlow.emit(Outcome.Failure())
            }
            articlesUpdatesFlow.emit(Outcome.Progress(false))
        }
    }

    fun getInitialArticlesUpdates(): Flow<Outcome<List<Article>>> = initialArticlesFlow.asSharedFlow()
    fun getArticlesUpdates(): Flow<Outcome<List<Article>>> = articlesUpdatesFlow.asSharedFlow()



}