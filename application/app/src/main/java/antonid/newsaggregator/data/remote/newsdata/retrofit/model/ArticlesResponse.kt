package antonid.newsaggregator.data.remote.newsdata.retrofit.model

data class ArticlesResponse(
    val data: List<Article>?,
    val error: Error?,
)