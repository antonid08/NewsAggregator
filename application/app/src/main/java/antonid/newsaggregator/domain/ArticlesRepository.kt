package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.model.Article

interface ArticlesRepository {

    suspend fun getCachedArticles(): List<Article>

    suspend fun loadPage(timestamp: Long, count: Int): List<Article>

}