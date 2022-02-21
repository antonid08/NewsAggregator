package antonid.newsaggregator.data.remote

import android.util.Log
import antonid.newsaggregator.data.remote.newsapi.NewsApiOrgRemoteDataSource
import antonid.newsaggregator.data.remote.newsdata.TheNewsApiComRemoteDataSource
import antonid.newsaggregator.domain.model.Article
import antonid.newsaggregator.utils.TAG
import kotlinx.coroutines.*
import java.lang.IllegalStateException

/**
 * This repository for remote news data.
 * Its purpose is combine data from several remote data sources.
 */
class ArticlesRemoteRepository(
    private val newsApiOrgRemoteDataSource: NewsApiOrgRemoteDataSource,
    private val theNewsApiComRemoteDataSource: TheNewsApiComRemoteDataSource,
){

    /**
     * Loads articles from all passed sources.
     *
     * @param timestampBefore defines publish timestamp of the newest article.
     * @param count defines how many articles will be returned
     *
     * @return list of articles sorted by publish date (newest at the beginning of the list)
     * @throws IllegalStateException if all sources calls failed. Doesn't throw (only log) anything
     * if at least one call succeed.
     */
    suspend fun loadPage(timestampBefore: Long, count: Int): List<Article> = supervisorScope {
        val countPerSource  = count / 2 + 1


        val newsApiOrgCall =
            async { newsApiOrgRemoteDataSource.getNews(timestampBefore, countPerSource) }
        val theNewsApiComCall =
            async { theNewsApiComRemoteDataSource.getNews(timestampBefore, countPerSource) }

        val newsApiOrgResult = runCatching {
            newsApiOrgCall.await()
        }.onFailure {
            Log.e(TAG, "Error of NewsApi.org call.", it)
        }

        val theNewsApiComResult = runCatching {
            theNewsApiComCall.await()
        }.onFailure {
            Log.e(TAG, "Error of TheNewsApi.com call.", it)
        }

        if (newsApiOrgResult.isFailure && theNewsApiComResult.isFailure) {
            throw IllegalStateException("Both API calls failed!")
        }

        (newsApiOrgResult.getOrDefault(emptyList()) + theNewsApiComResult.getOrDefault(emptyList()))
            .sortedByDescending { it.publishTimestamp }
            .take(count)
    }

}