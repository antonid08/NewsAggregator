package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.model.Article
import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Test
import kotlin.time.ExperimentalTime

// Use `runBlocking` instead of `runTest` for this test because it looks pretty hard to set up
// flow testing with internal scope (in `GetUpdatesInteractor`) using `runTest`.
class GetUpdatesInteractorTest {

    private val oldArticle = mockk<Article> {
        every { publishTimestamp } returns 1L
    }

    private val newArticle = mockk<Article> {
        every { publishTimestamp } returns 2L
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test
    fun `Verify flow emitted if fresh articles available`() = runBlocking {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(oldArticle)
            coEvery { loadPage(any(), any()) } returns listOf(newArticle)
        }

        val interactor = GetUpdatesInteractor(repository, 50L)
        val flow = interactor.execute()

        flow.test(100L) {
            awaitEvent()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test(expected = TimeoutCancellationException::class)
    fun `Verify flow not emitted before interval even if fresh articles available`() = runBlocking {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(oldArticle)
            coEvery { loadPage(any(), any()) } returns listOf(newArticle)
        }

        val interactor = GetUpdatesInteractor(repository, 100L)
        val flow = interactor.execute()

        flow.test(50L) {
            awaitEvent()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test(expected = TimeoutCancellationException::class)
    fun `Verify flow not emitted if fresh articles unavailable`() = runBlocking {
        val repository = mockk<ArticlesRepository> {
            coEvery { getCachedArticles() } returns listOf(newArticle)
            coEvery { loadPage(any(), any()) } returns listOf(oldArticle)
        }

        val interactor = GetUpdatesInteractor(repository, 50L)
        val flow = interactor.execute()

        flow.test(100L) {
            awaitEvent()
        }
    }


}