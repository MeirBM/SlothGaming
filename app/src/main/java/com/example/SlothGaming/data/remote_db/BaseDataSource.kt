package com.example.SlothGaming.data.remote_db

import android.util.Log
import com.example.SlothGaming.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T>
            getResult(call : suspend () -> Response<T>) : Resource<T> {

        try {
            val result  = call()
            Log.d("API_DEBUG", "$result")

            if(result.isSuccessful) {
                val body = result.body()
                Log.d("API_DEBUG", "$body")

                if(body != null) return  Resource.success(body)
            }
            return Resource.error("Network call has failed for the following reason: " +
                    "${result.message()} ${result.code()}")
        }catch (e : Exception) {
            return Resource.error("Network call has failed for the following reason: "
                    + (e.localizedMessage ?: e.toString()))
        }
    }
}