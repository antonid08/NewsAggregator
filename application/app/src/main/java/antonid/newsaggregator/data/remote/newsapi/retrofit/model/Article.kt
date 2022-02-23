package antonid.newsaggregator.data.remote.newsapi.retrofit.model

import java.util.*

data class Article(
    val title: String,
    val url: String,
    val urlToImage: String?,
    val source: Source,
    val publishedAt: Date,
)
