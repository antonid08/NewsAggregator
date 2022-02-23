package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.model.Article

interface ArticlesRepository {

    suspend fun getCachedArticles(count: Int): List<Article>
    suspend fun clearCache()

    suspend fun loadPage(timestamp: Long, count: Int): List<Article>

}