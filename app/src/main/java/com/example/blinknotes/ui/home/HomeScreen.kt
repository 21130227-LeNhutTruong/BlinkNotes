package com.example.blinknotes.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blinknotes.data.DataImage
import com.example.blinknotes.ui.utils.handleClick
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(
    navController: NavController,
) {

    val viewModel: HomeScreenViewModel = viewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val images = viewModel.image

  //  val isPerformingAction = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clickable{
                        handleClick (
                            coroutineScope = coroutineScope,
                            action = {
                                snackbarHostState.showSnackbar("Hello")

                            })
                }
                .height(50.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF4CAF50), Color(0xFF8BC34A))
                    )
                )
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = true
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Test",
                modifier = Modifier.padding(8.dp),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                color = Color.White
            )
        }


        if (images.isEmpty()) {
            Text("No images found")
        } else {
            ImageListItem(imageUrl = images, onClick =  { dataImage ->

                val encodedUrl = URLEncoder.encode(dataImage.img_url, StandardCharsets.UTF_8.toString())
                navController.navigate("detail/$encodedUrl"){
                    launchSingleTop = true

                }
                Log.d("Navigate", "Navigating to: detail/$encodedUrl")

            })
        }
    }
    SnackbarHost(
        hostState = snackbarHostState,
        )
}

@Composable
fun ImageListItem(
    imageUrl: List<DataImage>,
    onClick: (DataImage) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()


    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(imageUrl) { imageUrl ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable {
                            handleClick(coroutineScope) {
                                val encodedUrl = URLEncoder.encode(
                                    imageUrl.img_url,
                                    StandardCharsets.UTF_8.toString()
                                )
                                onClick(imageUrl)
                                Log.d(
                                    "Navigate",
                                    "Điều hướng tới: detail/$encodedUrl"
                                )
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = imageUrl.img_url,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(4.dp)),
                            onSuccess = { Log.d("Screen1", "Image loaded successfully  ${imageUrl.img_url}") },

                            )
                    }
                }
            }
        }, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    )
    Divider(modifier = Modifier.padding(vertical = 8.dp))

}

