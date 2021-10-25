package com.learning.moviesbrowser.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.learning.moviesbrowser.network.NoConnectionInterceptor
import com.learning.moviesbrowser.utils.State
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.nio.channels.NoConnectionPendingException

abstract class NetworkRepository<RESULT, REQUEST> {

    fun asFlow() = flow<State<RESULT>>{
            emit(State.loading())

        try {
            //Emit database content first
            //emit(State.success(fetchFromLocal().first()))

            // fetch latest data from the server
            val apiResponse = fetchFromRemote()

            // parse the response
            val remotePosts = apiResponse.body()

            // check for success
            if(apiResponse.isSuccessful && remotePosts !=null){
                // save the data into the database
                saveRemoteData(remotePosts)
            }else{
                // emit the error message if the API call fails.
                emit(State.error(apiResponse.message()))
            }
        }catch (noInternet: NoConnectionInterceptor.NoInternetException){
            emit(State.error("Network error! No Internet"))
        }
        catch (e: Exception){
            // if an exception occured emit the error message.
            emit(State.error("Network error! Can't get latest movies."))
            e.printStackTrace()
        }

        // Retrieved post from persistance storage and emit
        emitAll(fetchFromLocal().map {
            State.success<RESULT>(it)
        })
    }

    /**
     * Saves the remote data into the local storage
     */
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Retrieves all data from persistance storage.
     */
    @MainThread
    protected abstract suspend fun fetchFromLocal(): Flow<RESULT>

    /**
     * Fetches from the remote end point.
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}