package antonid.newsaggregator.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import antonid.newsaggregator.domain.ArticlesRepository
import antonid.newsaggregator.domain.LoadArticlesInteractor
import antonid.newsaggregator.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ArticlesViewModel(
    private val articlesRepository: ArticlesRepository,
): ViewModel() {

    private val articlesFlow = MutableSharedFlow<List<Article>>()

    init {
        viewModelScope.launch {
            runCatching {
                LoadArticlesInteractor(Calendar.getInstance().timeInMillis, 10, articlesRepository).execute()
            }.onSuccess {
                articlesFlow.emit(it)
            }.onFailure {
                //todo show error
            }
        }
    }

    fun getArticlesUpdates(): Flow<List<Article>> = articlesFlow.asSharedFlow()


}