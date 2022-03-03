package antonid.newsaggregator.domain

import antonid.newsaggregator.domain.utils.Interactor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Returns flow that emit when fresh articles are available.
 * Cancel collecting flow to stop updates checks.
 *
 * @param updatesIntervalMs how often updates should be checked.
 */
class GetUpdatesAvailableFlowInteractor(
    private val checkUpdatesInteractor: CheckUpdatesInteractor,
    private val updatesIntervalMs: Long = 10_000L,
): Interactor<Flow<Unit>> {

    override fun execute(): Flow<Unit> = flow  {
        while (currentCoroutineContext().isActive) {
            delay(updatesIntervalMs)
            if (checkUpdatesInteractor.execute()) {
                emit(Unit)
            }
        }
    }

}