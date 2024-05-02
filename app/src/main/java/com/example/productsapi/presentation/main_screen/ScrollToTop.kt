package com.example.productsapi.presentation.main_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScrollToTop(
    lazyListState: LazyListState, scope: CoroutineScope
) {
    val showButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 2 && !lazyListState.isScrollInProgress
        }
    }
    AnimatedVisibility(
        visible = showButton,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ScrollToTopButton(onClick = {
            scope.launch {
                lazyListState.animateScrollToItem(0)
            }
        })
    }
}

@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp), Alignment.BottomCenter
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .shadow(10.dp, shape = CircleShape)
                .size(65.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                "arrow up",
            )
        }
    }
}