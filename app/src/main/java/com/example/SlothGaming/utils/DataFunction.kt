package com.example.SlothGaming.utils

import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun <T,A> performFetchingAndSaving(localDbFetch: () -> Flow<T>,
                                   remoteDbFetch: suspend () ->Resource<A>,
                                   localDbSave: suspend (A) -> Unit) : Flow<Resource<T>> = flow{

        emit(Resource.loading())

        val localData = localDbFetch().first()
        emit(Resource.success(localData))

        val fetchResource = remoteDbFetch()

        if(fetchResource.status is Success){
            localDbSave(fetchResource.status.data!!)
            emitAll(localDbFetch().map { Resource.success(it) })
        }
        else if(fetchResource.status is Error){
            emit(Resource.error(fetchResource.status.message,localData))
            emitAll(localDbFetch().map { Resource.success(it) })
        }
    }.flowOn(Dispatchers.IO)