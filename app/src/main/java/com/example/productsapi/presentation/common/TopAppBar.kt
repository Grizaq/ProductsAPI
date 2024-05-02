package com.example.productsapi.presentation.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.productsapi.R
import com.example.productsapi.presentation.ui.theme.Dimensions
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

//@Preview(showBackground = true)
@Composable
fun AppsTopAppBar(text: String, navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val eMail = stringResource(
        R.string.message_about,
        text
    )
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .offset(x = -Dimensions.mainCardPadding),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                        )
                    }
                    Text(
                        text = text,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .background(Color.Transparent),
                        textAlign = TextAlign.Center,
                        fontSize = Dimensions.textLarge,
                        color = Color.White
                    )
                    IconButton(onClick = {
                        context.sendMail(to = "example@gmail.com", subject = eMail)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.back_button_desc),
                        )
                    }
                }
            }
        },
    )
}

fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
        Log.e("TopAppBar", "error sending message: $e")
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
        Log.e("TopAppBar", "sending message throws: $t")
    }
}
