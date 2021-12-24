package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.utils.Interactor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Returns flow that emit when fresh articles are available
 *
 * @param updatesIntervalMs how often updates should be checked.
 */
class GetUpdatesInteractor(
    private val repository: ArticlesRepository,
    private val updatesIntervalMs: Long = 60_000_000L,
): Interactor<Flow<Unit>> {

    private val updatesScope = CoroutineScope(Dispatchers.Default)

    private val updatesFlow = MutableSharedFlow<Unit>()

    override fun execute(): Flow<Unit> {
        updatesScope.launch {
            while (isActive) {
                checkUpdates()
                delay(updatesIntervalMs)
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