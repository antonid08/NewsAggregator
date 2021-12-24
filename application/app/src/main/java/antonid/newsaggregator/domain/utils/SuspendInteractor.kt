package antonid.newsaggregator.domain.utils

interface SuspendInteractor<T> {
    suspend fun execute(): T
}