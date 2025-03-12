package com.example.blinknotes.ui.detaill

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
        bottomBar = {
            Divider()
            BottomBarDetail()
                    },
        topBar = {
            HeaderDetaill(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 75.dp)
                .padding(paddingValues)
        ) {
            Divider()
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
fun HeaderDetaill( navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
        )
        Text(
            text = "User name",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            onClick = { }
        ) {
            Text(
                text = "Follow",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarDetail() {
    val comment = remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = comment.value,
            onValueChange = { comment.value = it },
            placeholder = {
                Text(
                   text = "Bình luận...",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )},
            modifier = Modifier
                .weight(1f)
                .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(24.dp))
                .padding(start = 12.dp),
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            containerColor = Color.White
        ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /* Like action */ }) {
                    Icon(  imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable (
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                isFavorite = !isFavorite

                            })
                }
                Text("42,5K", fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /* Save action */ }) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Save")
                }
                Text("13,3K", fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /* Comment action */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Comment")
                }
                Text("477", fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /* Share action */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                Text("0", fontSize = 12.sp)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview (){
//   // HeaderDetaill(navController = NavController(context = LocalContext.current ))
//    BottomBarDetail()
//}