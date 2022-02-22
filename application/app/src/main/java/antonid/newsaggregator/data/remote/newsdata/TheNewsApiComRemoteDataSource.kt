package antonid.newsaggregator.data.remote.newsdata

import antonid.newsaggregator.data.remote.newsdata.converter.TheNewsApiComArticleRetrofitConverter
import antonid.newsaggregator.data.remote.newsdata.retrofit.TheNewsApiComApi
import antonid.newsaggregator.domain.model.Article
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class TheNewsApiComRemoteDataSource(
    private val theNewsApiComApi: TheNewsApiComApi,
    private val theNewsApiArticleArticleRetrofitConverter: TheNewsApiComArticleRetrofitConverter,
) {

    suspend fun getNews(timestamp: Long, count: Int): List<Article> {
        val dateTo =
            SimpleDateFormat(THE_NEWS_API_COM_DATE_FORMAT, Locale.getDefault()).format(Date(timestamp))
        val response = theNewsApiComApi.getAll(dateTo, "en", count)

        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!.map(theNewsApiArticleArticleRetrofitConverter::convert)
        } else {
            throw Exception("TheNewsApiCom 'getAll' network call failed. Response: '$response'. " +
                    "Error body: ${response.errorBody()}")
        }
    }

    companion object {
        private const val THE_NEWS_API_COM_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
    }
}