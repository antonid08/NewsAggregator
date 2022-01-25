package antonid.newsaggregator.data.remote.newsapi.retrofit

import antonid.newsaggregator.data.remote.newsapi.retrofit.model.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Represents API of https://newsapi.org/docs
 * You can find all docs there.
 */
interface NewsApi {

    @GET("top-headlines")
    suspend fun topHeadlines(
        @Query("to") to: String,
        @Query("pageSize") pageSize: Int,
        @Query("country") country: String,
    ): Response<ArticlesResponse>

}