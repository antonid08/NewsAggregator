package antonid.newsaggregator.domain.utils

interface Interactor<T> {

    fun execute(): T
}