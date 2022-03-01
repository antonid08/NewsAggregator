package antonid.newsaggregator.ui.utils

sealed class Outcome<T> {

    data class Progress<T>(val isLoading: Boolean) : Outcome<T>()
    data class Success<T>(val data: T) : Outcome<T>()
    class Failure<T> : Outcome<T>()

}
