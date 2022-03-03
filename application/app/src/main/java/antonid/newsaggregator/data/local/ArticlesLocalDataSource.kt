package antonid.newsaggregator.data.local

import antonid.newsaggregator.data.local.room.ArticleRoomEntityConverter
import antonid.newsaggregator.data.local.room.ArticlesDao
import antonid.newsaggregator.domain.model.Article

/**
 * Local store.
 * Size is restricted with @param cacheSize
 */
class ArticlesLocalDataSource(
    private val cacheSize: Int,
    private val articlesDao: ArticlesDao,
    private val articleRoomEntityConverter: ArticleRoomEntityConverter,
) {

    suspend fun getLatest(): List<Article> {
        return articlesDao.getLatest().map(articleRoomEntityConverter::convert)
    }

    suspend fun save(articles: List<Article>) {
        articlesDao.insertWithReplace(articles.map(articleRoomEntityConverter::convert))
        clearRedundantCache()
    }

    private suspend fun clearRedundantCache() {
        val cachedArticles = getLatest()
        if (cachedArticles.size >= cacheSize) {
            articlesDao.removeOlderThan(cachedArticles[cacheSize - 1].publishTimestamp)
        }
    }

}