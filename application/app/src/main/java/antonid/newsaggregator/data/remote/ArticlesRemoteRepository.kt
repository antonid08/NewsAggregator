package antonid.newsaggregator.data.remote

import antonid.newsaggregator.domain.model.Article

class ArticlesRemoteRepository(
    private val mediaStackComRemoteDataSource: MediaStackComRemoteDataSource,
    private val newsDataIoRemoteDataSource: NewsDataIoRemoteDataSource,
){

    fun loadPage(timestamp: Long, count: Int): List<Article> {
        //todo stub
        return listOf()
    }

}