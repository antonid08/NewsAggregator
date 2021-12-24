package antonid.newsaggregator.domain.model

/**
 * Represents short description of news article.
 *
 * @param source name of news site/API from where the article received.
 */
data class Article(
   val title: String,
   val imageUrl: String,
   val source: String,
   val publishTimestamp: Long,
)