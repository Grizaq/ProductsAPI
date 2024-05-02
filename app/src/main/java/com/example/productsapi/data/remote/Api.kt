package com.example.productsapi.data.remote

import com.example.productsapi.data.model.Products
import com.example.productsapi.data.utils.Util.END_POINT
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET(END_POINT)
    suspend fun getItemList(): Response<Products>
}