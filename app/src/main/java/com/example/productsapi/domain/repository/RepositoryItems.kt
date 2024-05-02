package com.example.productsapi.domain.repository

import android.util.Log
import com.example.productsapi.data.model.Products
import com.example.productsapi.data.repository.Repository
import com.example.productsapi.data.remote.Api
import com.example.productsapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryItems @Inject constructor(private val api: Api) : Repository {
    override suspend fun getItemsList(): Flow<Resource<Products>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getItemList()
            if (response.isSuccessful)
                response.body()?.let {
                    emit(Resource.Success(it))
                    Log.i("DebugNetworkARepoLet", response.toString())
                }
            else
                emit(Resource.Error(response.code().toString()))
        } catch (E: HttpException) {
            emit(Resource.Error("Could not load data"))
        } catch (E: IOException) {
            emit(Resource.Error("Check internet"))
        }
    }
}