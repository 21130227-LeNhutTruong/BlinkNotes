package com.example.blinknotes.ui.detaill

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blinknotes.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun DetaillScreen(navController: NavController, imageUrl: String,
) {
    val decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 75.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Image Detail",
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                fontSize = 20.sp
            )
        }

        AsyncImage(
            model = decodedUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "Image",
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(id = R.drawable.splash),
            onSuccess = { Log.d("Screen2", "Image loaded successfully  ${decodedUrl}") },

            onError = { exception -> Log.e("Screen2", "Failed to load image: ${decodedUrl}") }

            )
    }
}