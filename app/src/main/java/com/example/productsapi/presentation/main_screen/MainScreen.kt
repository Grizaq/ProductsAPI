package com.example.productsapi.presentation.main_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productsapi.R
import com.example.productsapi.data.model.Product
import com.example.productsapi.domain.utils.Resource
import com.example.productsapi.presentation.common.AppsTopAppBar
import com.example.productsapi.presentation.destinations.DetailPageDestination
import com.example.productsapi.presentation.ui.theme.Dimensions
import com.example.productsapi.presentation.view_models.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@RootNavGraph(start = true)
@Destination
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: ViewModel = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.itemList.collectAsState()
    val itemList = uiState.data?.products ?: emptyList()
    viewModel.lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val lazyListState = viewModel.lazyListState
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.getItemList() })
    Scaffold(topBar = { AppsTopAppBar(text = "Shop", navigator = navigator) },
        content = { padding ->
            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .padding(padding)
            ) {
                when (uiState) {
                    is Resource.Success -> {
                        SuccessfulResponse(
                            lazyListState,
                            itemList,
                            navigator,
                            isRefreshing,
                            pullRefreshState,
                            scope,
                            Modifier.Companion.align(Alignment.TopCenter)
                        )
                    }

                    is Resource.Loading -> {
                        ShowLoadingIndicator()
                    }

                    is Resource.Error -> {
                        uiState.message?.let {
                            ShowErrorMessage(
                                message = it, viewModel = viewModel
                            )
                        }
                        val messageReload = stringResource(R.string.standard_reload_error_message)
                        LaunchedEffect(key1 = "") {
                            delay(5 * 1000)
                            viewModel.getItemList()
                            Log.i("MainScreen", messageReload)
                        }
                    }
                }
            }
        })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SuccessfulResponse(
    lazyListState: LazyListState,
    itemList: List<Product>,
    navigator: DestinationsNavigator,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    scope: CoroutineScope,
    modifier: Modifier
) {
    MainBody(lazyListState, itemList, navigator)
    PullToRefresh(
        isRefreshing,
        pullRefreshState,
        modifier
    )
    ScrollToTop(lazyListState, scope)
}

@Composable
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
private fun MainBody(
    lazyListState: LazyListState,
    itemList: List<Product>,
    navigator: DestinationsNavigator
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), state = lazyListState
    ) {
        val dimens = Dimensions
        items(itemList) { item ->
            val imgList = item.images.toTypedArray()
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.mainCardPadding),
                elevation = CardDefaults.cardElevation(dimens.mainCardElevation),
                onClick = {
                    navigator.navigate(
                        DetailPageDestination.invoke(
                            itemTitle = item.title,
//                          itemCategory = item.category,
                            itemDesc = item.description,
                            itemImg = imgList,
//                          itemPrice = item.price,
//                          itemRating = item.rating,
//                          itemStock = item.stock
                        )
                    )
                }) {
                Spacer(modifier = Modifier.height(dimens.spacerMed))
                Box {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val pagerState = rememberPagerState(initialPage = 0)
                        HorizontalPager(
                            count = item.images.size,
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = dimens.pagerHorizontal),
                            modifier = Modifier
                                .height(dimens.mainCardHeight)
                                .fillMaxWidth()
                        ) { page ->
                            Card(shape = RoundedCornerShape(dimens.spacerMed),
                                modifier = Modifier.graphicsLayer {
                                    val pageOffset =
                                        calculateCurrentOffsetForPage(page).absoluteValue
                                    lerp(
                                        start = 0.85f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(
                                            0f, 1f
                                        )
                                    ).also { scale ->
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(
                                            0f, 1f
                                        )
                                    )
                                }) {
                                GlideImage(
                                    imageModel = item.images[page],
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(dimens.roundingPercent)
                                        )
                                        .fillMaxHeight()
                                        .fillMaxWidth(dimens.defaultReducedWidth),
                                )
                            }
                        }
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(dimens.pagerHorizontal)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(dimens.defaultReducedWidth),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier.fillMaxWidth(dimens.defaultReducedWidth),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.title,
                                        minLines = 1,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        fontSize = dimens.textSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = item.description,
                                    maxLines = 2,
                                    textAlign = TextAlign.Center,
                                    fontFamily = FontFamily.Default
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(dimens.spacerMed))
                    }
                }
            }
        }
    }
}

