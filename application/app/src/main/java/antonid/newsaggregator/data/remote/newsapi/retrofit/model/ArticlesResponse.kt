package antonid.newsaggregator.data.remote.newsapi.retrofit.model

data class ArticlesResponse(
    val status: String,
    val articles: List<Article>
)