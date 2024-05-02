package com.example.productsapi.presentation.view_models

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapi.data.model.Products
import com.example.productsapi.data.repository.Repository
import com.example.productsapi.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _itemList: MutableStateFlow<Resource<Products>> =
        MutableStateFlow(Resource.Loading())
    val itemList: StateFlow<Resource<Products>> = _itemList

    init {
        getItemList()
    }

    fun getItemList() = viewModelScope.launch {
        repo.getItemsList().collectLatest { _itemList.emit(it) }
        Log.i("DebugnetworkAVM", itemList.value.data?.products.toString())
    }

    var lazyListState = LazyListState()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
}

