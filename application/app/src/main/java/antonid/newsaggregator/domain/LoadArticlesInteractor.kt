package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.model.Article
import antonid.newsaggregator.domain.utils.SuspendInteractor

/**
 * Load articles that published before [timestampBefore] from all data sources.
 * Newest articles at the top of the list.
 */
class LoadArticlesInteractor(
    private val timestampBefore: Long,
    private val count: Int,
    private val repository: ArticlesRepository,
): SuspendInteractor<List<Article>> {

    override suspend fun execute(): List<Article> =
        repository.loadPage(timestampBefore, count)
}