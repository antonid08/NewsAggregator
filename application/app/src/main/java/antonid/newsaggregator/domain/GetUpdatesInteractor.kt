package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.utils.Interactor
import antonid.newsaggregator.domain.utils.SuspendInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Returns flow that emit when fresh articles are available
 *
 * @param updatesIntervalMs how often updates should be checked.
 */
class GetUpdatesInteractor(
    private val repository: ArticlesRepository,
    private val updatesIntervalMs: Long = 60_000L,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
): Interactor<Flow<Unit>> {

    private val updatesFlow = MutableSharedFlow<Unit>()

    override fun execute(): Flow<Unit> {
        scope.launch {
            while (isActive) {
                delay(updatesIntervalMs)
                checkUpdates()
            }
        }

        return updatesFlow
    }

    private suspend fun checkUpdates() {
        val newestCachedArticle = repository.getCachedArticles()[0]
        val newestLoadedArticle = repository.loadPage(System.currentTimeMillis(), 1)[0]
        if (newestLoadedArticle.publishTimestamp > newestCachedArticle.publishTimestamp) {
            updatesFlow.emit(Unit)
        }
    }

}