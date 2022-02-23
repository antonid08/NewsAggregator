package antonid.newsaggregator.data.local

import antonid.newsaggregator.data.local.room.ArticleRoomEntityConverter
import antonid.newsaggregator.data.local.room.ArticlesDao
import antonid.newsaggregator.domain.model.Article

class ArticlesLocalDataSource(
    private val articlesDao: ArticlesDao,
    private val articleRoomEntityConverter: ArticleRoomEntityConverter,
) {

    suspend fun getLatest(count: Int): List<Article> {
        return articlesDao.getLatest(count).map(articleRoomEntityConverter::convert)
    }

    suspend fun save(articles: List<Article>) {
        articlesDao.insertWithReplace(articles.map(articleRoomEntityConverter::convert))
    }

    suspend fun clear() {
        articlesDao.removeAll()
    }
}