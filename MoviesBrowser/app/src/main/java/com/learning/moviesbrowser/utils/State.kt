package com.learning.moviesbrowser.utils

sealed class State<T> {
    class Loading<T>: State<T>()

    data class Success<T>(val data: T): State<T>()

    data class Error<T>(val message: String): State<T>()

    companion object{
        /**
         * Returns loading state instance
         */
        fun<T> loading() = Loading<T>()

        /**
         * Returns success state instance along with the required data
         */
        fun<T> success(data: T)=
            Success(data)

        /**
         * Returns error state instance along with the error message
         */

        fun<T> error(message: String) =
            Error<T>(message)
    }
}