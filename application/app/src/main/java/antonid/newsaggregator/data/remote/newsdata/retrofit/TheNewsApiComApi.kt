package antonid.newsaggregator.data.remote.newsdata.retrofit

import antonid.newsaggregator.data.remote.newsdata.retrofit.model.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Documentation to this API:
 * https://www.thenewsapi.com/documentation
 */
interface TheNewsApiComApi {

    @GET("news/all")
    suspend fun getAll(
        @Query("published_before") publishedBefore: String,
        @Query("language") language: String,
        @Query("limit") limit: Int,
    ): Response<ArticlesResponse>

}