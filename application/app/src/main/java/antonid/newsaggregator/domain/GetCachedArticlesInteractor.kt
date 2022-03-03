package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.model.Article
import antonid.newsaggregator.domain.utils.SuspendInteractor

/**
 * Returns cached articles sorted by publish date.
 * Newest articles at the top of the list.
 */
class GetCachedArticlesInteractor(
    private val repository: ArticlesRepository,
): SuspendInteractor<List<Article>> {

    override suspend fun execute(): List<Article> =
        repository.getCachedArticles()
}