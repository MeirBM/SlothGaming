package com.example.SlothGaming.data.remote_db

import android.util.Log
import com.example.SlothGaming.utils.Resource
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseDataSource {

    protected suspend fun <T>
            getResult(call : suspend () -> Response<T>) : Resource<T> {

        try {
            val result  = call()

            if(result.isSuccessful) {
                val body = result.body()

                if(body != null) return  Resource.success(body)
            }
            return Resource.error("Network call has failed for the following reason: " +
                    "${result.message()} ${result.code()}")
        }catch (e : CancellationException) {
            throw e
        }catch (e : Exception) {
            return Resource.error("Network call has failed for the following reason: "
                    + (e.localizedMessage ?: e.toString()))
        }
    }
}