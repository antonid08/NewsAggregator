package antonid.newsaggregator.data.remote

import antonid.newsaggregator.data.ArticlesRepositoryImpl
import antonid.newsaggregator.data.local.ArticlesLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticlesRepositoryImplTest {

    @Test
    fun `Verify loaded items saved to cache`() = runTest(UnconfinedTestDispatcher()) {
        val remoteRepository = mockk<ArticlesRemoteRepository> {
            coEvery { loadPage(any(), any()) } returns listOf()
        }
        val localDataSource = mockk<ArticlesLocalDataSource>() {
            coEvery { save(any()) } returns Unit
        }
        ArticlesRepositoryImpl(remoteRepository, localDataSource).loadPage(0, 0)
        coVerify { localDataSource.save(any()) }
    }
}