package antonid.newsaggregator.data.remote.newsdata.converter

import antonid.newsaggregator.data.remote.newsdata.retrofit.model.Article


class TheNewsApiComArticleRetrofitConverter {

    fun convert(newsApiComArticle: Article): antonid.newsaggregator.domain.model.Article =
        antonid.newsaggregator.domain.model.Article(
            newsApiComArticle.title,
            newsApiComArticle.imageUrl,
            newsApiComArticle.source,
            newsApiComArticle.publishedAt.time,
        )
}