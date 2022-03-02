package antonid.newsaggregator.domain

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Test
import kotlin.time.ExperimentalTime

// Use `runBlocking` instead of `runTest` for this test because it looks pretty hard to set up
// flow testing with internal scope (in `GetUpdatesInteractor`) using `runTest`.
@ExperimentalCoroutinesApi
@ExperimentalTime
class GetUpdatesFlowInteractorTest {

    @Test
    fun `Verify flow emitted if fresh articles available`() = runBlocking {

        val checkUpdatesInteractor = mockk<CheckUpdatesInteractor> {
            coEvery { execute() } returns true
        }

        val interactor = GetUpdatesFlowInteractor(checkUpdatesInteractor, 50L)
        val flow = interactor.execute()

        flow.test(100L) {
            awaitEvent()
        }
    }

    @Test(expected = TimeoutCancellationException::class)
    fun `Verify flow not emitted before interval even if fresh articles available`() = runBlocking {
        val checkUpdatesInteractor = mockk<CheckUpdatesInteractor> {
            coEvery { execute() } returns true
        }

        val interactor = GetUpdatesFlowInteractor(checkUpdatesInteractor, 100L)
        val flow = interactor.execute()

        flow.test(50L) {
            awaitEvent()
        }
    }

    @Test(expected = TimeoutCancellationException::class)
    fun `Verify flow not emitted if fresh articles unavailable`() = runBlocking {
        val checkUpdatesInteractor = mockk<CheckUpdatesInteractor> {
            coEvery { execute() } returns false
        }

        val interactor = GetUpdatesFlowInteractor(checkUpdatesInteractor, 50L)
        val flow = interactor.execute()

        flow.test(100L) {
            awaitEvent()
        }
    }

    @Test
    fun `Verify flow stops checking updates after cancel `() = runBlocking {
        val checkUpdatesInteractor = mockk<CheckUpdatesInteractor> {
            coEvery { execute() } returns true
        }

        val interactor = GetUpdatesFlowInteractor(checkUpdatesInteractor, 50L)
        val flow = interactor.execute()

        flow.test(1000L) {
            cancel()
            delay(150L)
            expectNoEvents()
        }

    }

}