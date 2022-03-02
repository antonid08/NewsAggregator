package antonid.newsaggregator.ui.articles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import antonid.newsaggregator.domain.*
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

    private val initialArticlesPageFlow = MutableSharedFlow<Outcome<List<Article>>>()

    /**
     * Emits in 2 cases:
     * 1) Single time when **initial** page loaded after fragment start.
     * 2) When articles were refreshed.
     *
     * In both cases emitted articles should replace all previous data.
     */
    fun getInitialArticlesPageFlow(): Flow<Outcome<List<Article>>> = initialArticlesPageFlow.asSharedFlow()

    private val nextArticlesPageFlow = MutableSharedFlow<Outcome<List<Article>>>()

    /**
     * Emits when new portion of articles loaded. Emitted value: new articles page.
     */
    fun getNextArticlesPageFlow(): Flow<Outcome<List<Article>>> = nextArticlesPageFlow.asSharedFlow()


    private val updatesAvailableFlow = MutableSharedFlow<Unit>()

    /**
     * Emits when new articles available.
     */
    fun getUpdatesAvailableFlow(): Flow<Unit> = updatesAvailableFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            GetUpdatesFlowInteractor(
                CheckUpdatesInteractor(articlesRepository),
            ).execute().collect {
                updatesAvailableFlow.emit(Unit)
            }
        }
    }

    fun loadInitialArticles() {
        viewModelScope.launch {
            initialArticlesPageFlow.emit(Outcome.Progress(true))
            runCatching {
                val cachedArticles = GetCachedArticlesInteractor(CACHE_SIZE, articlesRepository).execute()
                if (cachedArticles.isNotEmpty()) {
                    launch {
                        if (CheckUpdatesInteractor(articlesRepository).execute()) {
                            updatesAvailableFlow.emit(Unit)
                        }
                    }
                    cachedArticles
                } else {
                    LoadArticlesInteractor(Calendar.getInstance().timeInMillis, PAGE_SIZE, articlesRepository).execute()
                }
            }.onSuccess {
                initialArticlesPageFlow.emit(Outcome.Success(it))
            }.onFailure {
                Log.e(TAG, "", it)
                initialArticlesPageFlow.emit(Outcome.Failure())
            }
            initialArticlesPageFlow.emit(Outcome.Progress(false))
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            runCatching {
                LoadArticlesInteractor(Calendar.getInstance().timeInMillis, PAGE_SIZE, articlesRepository).execute()
            }.onSuccess {
                initialArticlesPageFlow.emit(Outcome.Success(it))
            }.onFailure {
                Log.e(TAG, "", it)
                initialArticlesPageFlow.emit(Outcome.Failure())
            }
            initialArticlesPageFlow.emit(Outcome.Progress(false))
        }
    }

    fun loadArticlesPage(timestampBefore: Long) {
        viewModelScope.launch {
            nextArticlesPageFlow.emit(Outcome.Progress(true))
            runCatching {
                LoadArticlesInteractor(timestampBefore, PAGE_SIZE, articlesRepository).execute()
            }.onSuccess {
                nextArticlesPageFlow.emit(Outcome.Success(it))
            }.onFailure {
                Log.e(TAG, "", it)
                nextArticlesPageFlow.emit(Outcome.Failure())
            }
            nextArticlesPageFlow.emit(Outcome.Progress(false))
        }
    }



}