package antonid.newsaggregator.data.remote.newsapi

import antonid.newsaggregator.data.remote.newsapi.converter.NewsApiArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsapi.retrofit.NewsApi
import antonid.newsaggregator.domain.model.Article
import java.text.SimpleDateFormat
import java.util.*

class NewsApiOrgRemoteDataSource(
    private val newsApi: NewsApi,
    private val newsApiArticleRetrofitConverter: NewsApiArticleRetrofitConverter,
) {

    suspend fun getNews(timestamp: Long, count: Int): List<Article> {
        val dateTo =
            SimpleDateFormat(NEWSAPI_DATE_FORMAT, Locale.getDefault()).format(Date(timestamp))
        val response = newsApi.topHeadlines(dateTo, count, "us")
        if (response.isSuccessful && response.body()?.status == NEWSAPI_SUCCESSFULL_STATUS) {
            return response.body()!!.articles.map(newsApiArticleRetrofitConverter::convert)
        } else {
            throw Exception("NewsApi 'everything' network call failed. Response: '$response'")
        }
    }

    companion object {
        private const val NEWSAPI_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
        private const val NEWSAPI_SUCCESSFULL_STATUS = "ok"
    }
}