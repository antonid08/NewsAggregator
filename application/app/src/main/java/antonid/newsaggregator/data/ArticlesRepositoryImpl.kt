package antonid.newsaggregator.data

import antonid.newsaggregator.data.local.ArticlesLocalDataSource
import antonid.newsaggregator.data.remote.ArticlesRemoteRepository
import antonid.newsaggregator.domain.ArticlesRepository
import antonid.newsaggregator.domain.model.Article

class ArticlesRepositoryImpl(
    private val remoteRepository: ArticlesRemoteRepository,
    private val localDataSource: ArticlesLocalDataSource,
): ArticlesRepository {

    override suspend fun getCachedArticles(count: Int): List<Article> =
        localDataSource.getLatest(count)

    override suspend fun clearCache() {
        localDataSource.clear()
    }

    override suspend fun loadPage(timestamp: Long, count: Int): List<Article> {
        val remoteArticles = remoteRepository.loadPage(timestamp, count)
        localDataSource.save(remoteArticles)
        return remoteArticles
    }
}