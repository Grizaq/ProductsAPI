package com.example.productsapi.presentation.details_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.lerp
import com.example.productsapi.presentation.common.AppsTopAppBar
import com.example.productsapi.presentation.ui.theme.Dimensions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun DetailPage(
    itemImg: Array<String>, itemTitle: String,
//    itemCategory: String,
    itemDesc: String,
//    itemPrice: Int,
//    itemRating: Double,
//    itemStock: Int,
    navigator: DestinationsNavigator
) {
    val dimens = Dimensions
    Scaffold(topBar = { AppsTopAppBar(text = itemTitle, navigator = navigator) }, content = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(dimens.spacerMed))
            val pagerState = rememberPagerState(initialPage = 0)
            LaunchedEffect(Unit) {
                while (true) {
                    yield()
                    delay(5000)
                    pagerState.animateScrollToPage(
                        page = (pagerState.currentPage + 1) % (pagerState.pageCount)
                    )
                }
            }
            HorizontalPager(
                count = itemImg.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = dimens.pagerHorizontal),
                modifier = Modifier
                    .height(dimens.mainCardHeight)
                    .fillMaxWidth()
            ) { page ->
                Card(shape = RoundedCornerShape(dimens.roundingPercentHalf),
                    modifier = Modifier.graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.95f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        alpha = lerp(
                            start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
                    GlideImage(
                        imageModel = itemImg[page],
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
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

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(), Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(dimens.extraReducedWidth),
                            text = itemTitle,
                            minLines = 1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            fontSize = dimens.textDefault,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(dimens.defaultReducedWidth),
                            text = itemDesc,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Default
                        )
                    }
                }
            }
        }
    })
}