package antonid.newsaggregator.data.remote.newsapi.converter

import antonid.newsaggregator.data.remote.newsapi.retrofit.model.Article

class NewsApiArticleRetrofitConverter {

    fun convert(newsApiArticle: Article): antonid.newsaggregator.domain.model.Article =
        antonid.newsaggregator.domain.model.Article(
            newsApiArticle.title,
            newsApiArticle.urlToImage,
            newsApiArticle.source.name,
            newsApiArticle.publishedAt.time,
        )
}