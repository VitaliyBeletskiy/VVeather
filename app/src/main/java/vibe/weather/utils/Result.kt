package vibe.weather.utils

sealed class Result<out T, out E>

data class Success<out T>(val value: T) : Result<T, Nothing>() {
    override fun toString(): String = "Success($value)"
}

data class Failure<out E>(val error: E) : Result<Nothing, E>() {
    override fun toString(): String = "Failure($error)"
}

fun <T> T.asSuccess(): Success<T> = Success(this)

fun <E> E.asFailure(): Failure<E> = Failure(this)

fun <T, E> Result<T, E>.isSuccess() = this is Success

fun <T, E> Result<T, E>.isFailure() = this is Failure

/**
 * Applies `ifSuccess` if this is a [Success] or `ifFailure` if this is a [Failure]
 *
 * Example:
 * ```
 * val result: Result<Value, Exception> = possiblyFailingOperation()
 * result.fold(
 *     { log("operation succeeded with $it) },
 *     { log("operation failed with $it) }
 * )
 * ```
 *
 * @param ifSuccess the function to apply if this is a [Success]
 * @param ifFailure the function to apply if this is a [Failure]
 * @return the result of applying the function
 */
inline fun <T, E, R> Result<T, E>.fold(
    ifSuccess: (T) -> R,
    ifFailure: (E) -> R,
): R = when (this) {
    is Success -> ifSuccess(value)
    is Failure -> ifFailure(error)
}
