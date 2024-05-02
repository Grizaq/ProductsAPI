package com.example.productsapi.data.repository

import com.example.productsapi.data.model.Products
import com.example.productsapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getItemsList(): Flow<Resource<Products>>
}