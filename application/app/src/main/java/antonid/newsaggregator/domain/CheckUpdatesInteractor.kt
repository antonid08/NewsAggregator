package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.utils.SuspendInteractor

/**
 * Checks if there are new articles in remote source comparing to local source.
 */
class CheckUpdatesInteractor(
    private val repository: ArticlesRepository,
): SuspendInteractor<Boolean> {

    override suspend fun execute(): Boolean {
        val newestCachedArticle = repository.getCachedArticles(1).firstOrNull()
        val newestLoadedArticle = repository.loadPage(System.currentTimeMillis(), 1).firstOrNull()

        if (newestCachedArticle == null) {
            return newestLoadedArticle != null
        }

        if (newestLoadedArticle == null) {
            return false
        }

        return (newestLoadedArticle.publishTimestamp > newestCachedArticle.publishTimestamp)
    }

}