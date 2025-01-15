package com.example.blinknotes.ui.detaill

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blinknotes.R
import com.example.blinknotes.navigation.Graph
import com.example.blinknotes.navigation.Screens
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun DetaillScreen(navController: NavController, imageUrl: String,
) {
    val decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8.toString())

    Scaffold(
        bottomBar = { BottomBarDetail() },
        topBar = { TopAppDetaill(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 75.dp)
                .padding(paddingValues)
        ) {
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
}
@Composable
fun TopAppDetaill( navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )
        Text(
            text = "User name",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color.Green)
                .clickable {}
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Follow",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
        }
    }
}
@Composable
fun BottomBarDetail(

){
    var comment = remember { mutableStateOf("") }
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(56.dp)
    ){
        TextField(
            value = "comment",
            onValueChange = {comment },
            label = { Text("Thêm bình luận") },
            modifier = Modifier.fillMaxWidth(),

        )
        Row {
            Column {
                IconButton(
                    onClick = {}
                ) { }
                Text("")
            }
            Column {
                IconButton(
                    onClick = {}
                ) { }
                Text("")

            }
            Column {
                IconButton(
                    onClick = {}
                ) { }
                Text("")

            }
            Column {
                IconButton(
                    onClick = {}
                ) { }
                Text("")


            }
        }
    }
}