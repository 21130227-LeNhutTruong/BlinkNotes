package com.example.blinknotes.ui.home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.IconButton
import coil.compose.AsyncImage
import com.example.blinknotes.R
import com.example.blinknotes.data.DataImage
import com.example.blinknotes.ui.eventClick.handleClick
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun HomeScreen( navController: NavHostController) {
    var userSignedIn by remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    userSignedIn = currentUser != null
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { if (userSignedIn) 2 else 1 }
    )
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 35.dp)

        ) {

            if (userSignedIn) {
                TabContent(pagerState = pagerState, scope = scope  ,
                    selectedTab = selectedTab,
                    onTabSelected = { tabIndex -> selectedTab = tabIndex },
                    onSearchClick = {
                    println("Search button clicked!")
                })
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 -> ExploreScreen(navController)
                    1 -> if (userSignedIn) FollowScreen(navController)
                }
            }

        }
}


@Composable
fun ImageListItem(
    imageUrl: List<DataImage>,
    onClick: (DataImage) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()
    val handleClick = handleClick()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(imageUrl, key = { it.img_url }) { image  ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable {
                            handleClick(coroutineScope) {
                                val encodedUrl = URLEncoder.encode(
                                    image.img_url,
                                    StandardCharsets.UTF_8.toString()
                                )
                                onClick(image)
                                Log.d(
                                    "Navigate",
                                    "Điều hướng tới: detail/$encodedUrl"
                                )
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = image.img_url,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                                .clip(RoundedCornerShape(4.dp)),
                            placeholder = painterResource(id = R.drawable.splash),
                            onSuccess = { Log.d("Screen1", "Image loaded successfully  ${image.img_url}") },

                            )
                        Text(
                            text = "Cố gắng từng ngày vì bản thân",
                            modifier = Modifier
                                .align(alignment = Alignment.Start),
                            fontStyle = FontStyle.Normal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Avatar hình tròn
                            Image(
                                painter = painterResource(id = R.drawable.logo_app), // Thay đổi id ảnh đại diện
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape)
                            )
                            Text(
                                text = "User name",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "99 N",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = Color.Red,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    )
    Divider(modifier = Modifier.padding(vertical = 8.dp))

}
@Composable
fun ExploreScreen(
    navController: NavController,

    ){
    val viewModel: HomeScreenViewModel = viewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val images = viewModel.image

    val handleClick = handleClick()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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


}
@Composable
fun ImageListItemFolow(
    imageUrl: List<DataImage>,
    onClick: (DataImage) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()
    val handleClick = handleClick()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(imageUrl, key = { it.img_url }) { image  ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .clickable {
                            handleClick(coroutineScope) {
                                val encodedUrl = URLEncoder.encode(
                                    image.img_url,
                                    StandardCharsets.UTF_8.toString()
                                )
                                onClick(image)
                                Log.d(
                                    "Navigate",
                                    "Điều hướng tới: detail/$encodedUrl"
                                )
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .border(4.dp, Color.Gray)
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(2.dp),

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_app),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape)
                            )
                            Column {
                                Text(
                                    text = "User name",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "@username123",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Gray)
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
                        }
                        AsyncImage(
                            model = image.img_url,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                                .clip(RoundedCornerShape(4.dp)),
                            placeholder = painterResource(id = R.drawable.splash),
                            onSuccess = { Log.d("Screen1", "Image loaded successfully  ${image.img_url}") },

                            )
                        Text(
                            text = "Cố gắng từng ngày vì bản thân",
                            modifier = Modifier
                                .align(alignment = Alignment.Start),
                            fontStyle = FontStyle.Normal,
                            fontSize = 16.sp,
                            color = Color.Black
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
@Composable
fun FollowScreen(
    navController: NavController,

    ) {
    val viewModel: HomeScreenViewModel = viewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val images = viewModel.image

    val handleClick = handleClick()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (images.isEmpty()) {
            Text("No images found")
        } else {
            ImageListItemFolow(imageUrl = images, onClick = { dataImage ->

                val encodedUrl =
                    URLEncoder.encode(dataImage.img_url, StandardCharsets.UTF_8.toString())
                navController.navigate("detail/$encodedUrl") {
                    launchSingleTop = true

                }
                Log.d("Navigate", "Navigating to: detail/$encodedUrl")

            })
        }
    }
}

@Composable
fun TabContent(pagerState: PagerState, scope: CoroutineScope, onSearchClick: () -> Unit,  selectedTab: Int,
               onTabSelected: (Int) -> Unit,){
    val tabs = listOf("Đề xuất", "Đã Follow")


    // Header layout
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tabs
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, title ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            onTabSelected(index) }
                ) {
                    Text(
                        text = title,
                        color = if (index == selectedTab) Color.Black else Color.Gray,
                        fontWeight = if (index == selectedTab) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 22.sp
                    )
                    if (index == selectedTab) {
                        WavyLineBox(
                            modifier = Modifier,
                            color = Color.Cyan
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        // Search Icon
        IconButton(
            onClick = onSearchClick,
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
fun WavyLineBox(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(
        modifier = modifier
            .height(4.dp)
            .width(60.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, size.height / 2)
            cubicTo(
                size.width * 0.65f, -size.height ,
                size.width * 0.65f, size.height * 2,
                size.width, size.height
            )
        }
        drawPath(path = path, color = color, style = Stroke(width = 4f, cap = StrokeCap.Round))
    }
}
