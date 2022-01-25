package antonid.newsaggregator.data.remote

import antonid.newsaggregator.data.remote.newsapi.NewsApiOrgRemoteDataSource
import antonid.newsaggregator.data.remote.newsdata.NewsDataIoRemoteDataSource
import antonid.newsaggregator.domain.model.Article

/**
 * This repository for remote news data.
 * Its purpose is combine data from several remote data sources.
 */
class ArticlesRemoteRepository(
    private val newsApiOrgRemoteDataSource: NewsApiOrgRemoteDataSource,
    private val newsDataIoRemoteDataSource: NewsDataIoRemoteDataSource,
){

    suspend fun loadPage(timestamp: Long, count: Int): List<Article> {
        return newsApiOrgRemoteDataSource.getNews(timestamp, count)
    }

}