package antonid.newsaggregator.data.local.room

import antonid.newsaggregator.data.local.room.entity.ArticleRoomEntity
import antonid.newsaggregator.domain.model.Article

class ArticleRoomEntityConverter {

    fun convert(roomArticle: ArticleRoomEntity): Article =
        Article(
            roomArticle.title,
            roomArticle.imageUrl,
            roomArticle.source,
            roomArticle.publishTimestamp,
        )

    fun convert(article: Article): ArticleRoomEntity =
        ArticleRoomEntity(
            article.title,
            article.imageUrl,
            article.source,
            article.publishTimestamp,
        )
}