package antonid.newsaggregator.data.remote

import antonid.newsaggregator.data.remote.newsapi.NewsApiOrgRemoteDataSource
import antonid.newsaggregator.data.remote.newsdata.TheNewsApiComRemoteDataSource
import antonid.newsaggregator.domain.model.Article
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
class ArticlesRemoteRepositoryTest {

    private val testArticles = listOf(
        Article("", "", "", 1),
        Article("", "", "", 3),
        Article("", "", "", 2),
    )


    @Test
    fun `Verify returned list does not exceed requested size`() = runTest(UnconfinedTestDispatcher()) {
        val newsApiOrgRemoteDataSource = mockk<NewsApiOrgRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns testArticles.take(2)
        }
        val theNewsApiComRemoteDataSource = mockk<TheNewsApiComRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns listOf(testArticles[2])
        }

        val result = ArticlesRemoteRepository(newsApiOrgRemoteDataSource, theNewsApiComRemoteDataSource)
            .loadPage(4, 2)
        assert(result.size == 2)
    }

    @Test
    fun `Verify returned list is sorted by publish time by descending`() = runTest(UnconfinedTestDispatcher()) {
        val newsApiOrgRemoteDataSource = mockk<NewsApiOrgRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns listOf(testArticles[0])
        }
        val theNewsApiComRemoteDataSource = mockk<TheNewsApiComRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns listOf(testArticles[1])
        }
        val result = ArticlesRemoteRepository(newsApiOrgRemoteDataSource, theNewsApiComRemoteDataSource)
            .loadPage(4, 2)
        assert(result[0].publishTimestamp > result[1].publishTimestamp)
    }

    @Test
    fun `Verify sorting is correct when data sources return more than required articles`() = runTest(UnconfinedTestDispatcher()) {
        val newsApiOrgRemoteDataSource = mockk<NewsApiOrgRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns testArticles.take(2)
        }
        val theNewsApiComRemoteDataSource = mockk<TheNewsApiComRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns listOf(testArticles[2])
        }
        val result = ArticlesRemoteRepository(newsApiOrgRemoteDataSource, theNewsApiComRemoteDataSource)
            .loadPage(4, 2)
        assert(result[0].publishTimestamp > result[1].publishTimestamp)
    }

    @Test
    fun `Verify that execution completes successfully if one of calls failed`() = runTest(UnconfinedTestDispatcher()) {
        val newsApiOrgRemoteDataSource = mockk<NewsApiOrgRemoteDataSource> {
            coEvery { getNews(any(), any()) } returns listOf(testArticles[0])
        }
        val theNewsApiComRemoteDataSource = mockk<TheNewsApiComRemoteDataSource> {
            coEvery { getNews(any(), any()) } throws(IllegalStateException())
        }
        val result = ArticlesRemoteRepository(newsApiOrgRemoteDataSource, theNewsApiComRemoteDataSource)
            .loadPage(4, 2)
        assert(result.size == 1)
    }


    @Test(expected = IllegalStateException::class)
    fun `Verify that execution completes unsuccessfully if both calls failed`() = runTest(UnconfinedTestDispatcher()) {
        val newsApiOrgRemoteDataSource = mockk<NewsApiOrgRemoteDataSource> {
            coEvery { getNews(any(), any()) } throws (IllegalStateException())
        }
        val theNewsApiComRemoteDataSource = mockk<TheNewsApiComRemoteDataSource> {
            coEvery { getNews(any(), any()) } throws (IllegalStateException())
        }

        ArticlesRemoteRepository(newsApiOrgRemoteDataSource, theNewsApiComRemoteDataSource)
            .loadPage(4, 2)
    }
}