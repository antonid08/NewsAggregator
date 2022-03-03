package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.model.Article
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckUpdatesInteractorTest {

    private val oldArticle = mockk<Article> {
        every { publishTimestamp } returns 1L
    }

    private val newArticle = mockk<Article> {
        every { publishTimestamp } returns 2L
    }

    @Test
    fun `Verify returns true if both articles present and loaded article is newer`() = runTest(UnconfinedTestDispatcher()) {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(oldArticle)
            coEvery { loadPage(any(), any()) } returns listOf(newArticle)
        }

        val result = CheckUpdatesInteractor(repository).execute()
        Assert.assertTrue(result)
    }

    @Test
    fun `Verify returns false if both articles present and loaded article is older`() = runTest(UnconfinedTestDispatcher()) {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(newArticle)
            coEvery { loadPage(any(), any()) } returns listOf(oldArticle)
        }

        val result = CheckUpdatesInteractor(repository).execute()
        Assert.assertFalse(result)
    }

    @Test
    fun `Verify returns false if both articles present and they have same publish time`() = runTest(UnconfinedTestDispatcher()) {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(newArticle)
            coEvery { loadPage(any(), any()) } returns listOf(newArticle)
        }

        val result = CheckUpdatesInteractor(repository).execute()
        Assert.assertFalse(result)
    }

    @Test
    fun `Verify returns false only cached article present`() = runTest(UnconfinedTestDispatcher()) {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(newArticle)
            coEvery { loadPage(any(), any()) } returns listOf()
        }

        val result = CheckUpdatesInteractor(repository).execute()
        Assert.assertFalse(result)
    }

    @Test
    fun `Verify returns true only loaded article present`() = runTest(UnconfinedTestDispatcher()) {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf()
            coEvery { loadPage(any(), any()) } returns listOf(newArticle)
        }

        val result = CheckUpdatesInteractor(repository).execute()
        Assert.assertTrue(result)
    }

    @Test
    fun `Verify returns false both if there are no articles present`() = runTest(UnconfinedTestDispatcher()) {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf()
            coEvery { loadPage(any(), any()) } returns listOf()
        }

        val result = CheckUpdatesInteractor(repository).execute()
        Assert.assertFalse(result)
    }


}