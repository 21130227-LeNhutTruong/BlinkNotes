package com.example.blinknotes.ui.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.IconButton
import coil.compose.AsyncImage
import com.example.blinknotes.R
import com.example.blinknotes.data.DataImage
import com.example.blinknotes.ui.eventClick.handleClick
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen( navController: NavHostController, viewModel: HomeScreenViewModel) {
    val viewModels = remember { viewModel }
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
                .padding(bottom = 30.dp)
        ) {
            TabContent(pagerState = pagerState, scope = scope  ,
            selectedTab = selectedTab,
            onTabSelected = { tabIndex -> selectedTab = tabIndex },
            onSearchClick = {
                println("Search button clicked!")
            })
            if (userSignedIn) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    when (page) {
                        0 -> ExploreScreen(navController, viewModel = viewModels )
                        1 -> if (userSignedIn) FollowScreen(navController, viewModel = viewModels)
                    }
                }
            }

        }
}


@Composable
fun ImageListItem(
    imageUrl: List<DataImage>,
    onClick: (DataImage) -> Unit,
    isLoading: Boolean,
    onLoadMore: () ->Unit
) {
    val viewModel = HomeScreenViewModel()
    val coroutineScope = rememberCoroutineScope()
    val handleClick = handleClick()
    val imageListState = rememberLazyStaggeredGridState()
    val state by viewModel.uiState.collectAsState()
    val isAtBottom by remember {
            derivedStateOf {
                val lastVisibleItemIndex = imageListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                val totalItems = imageListState.layoutInfo.totalItemsCount
                lastVisibleItemIndex >= totalItems - 1 && totalItems > 0
            }
        }
    LaunchedEffect(isAtBottom) {
        if (isAtBottom && !isLoading) {
            onLoadMore()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyVerticalStaggeredGrid(
            state = imageListState,
            flingBehavior = ScrollableDefaults.flingBehavior() ,
            reverseLayout = false,
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 1.dp,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            content = {
                items(imageUrl, key = { it.img_url }) { image ->
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
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
                                .padding(end = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            AsyncImage(
                                model = image.img_url,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(8.dp)),
                                placeholder = painterResource(id = R.drawable.splash),
                                onSuccess = {
                                    Log.d(
                                        "Screen1",
                                        "Image loaded successfully  ${image.img_url}"
                                    )
                                },

                                )
                            Text(
                                text = "Cố gắng từng ngày vì bản thân,Cố gắng từng ngày vì bản thân",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 10.dp)
                                    .align(alignment = Alignment.Start),
                                fontStyle = FontStyle.Normal,
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 16.sp,
                            )
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
                                        .size(18.dp)
                                        .clip(CircleShape)
                                )

                                Text(
                                    text = "User name @ abcde xs",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .widthIn(max = 60.dp),
                                )

                                Text(
                                    text = "99 N",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .padding(start = 2.dp)
                                        .widthIn(max = 60.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    tint = Color.Red,
                                    contentDescription = "Favorite",
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                        }
                    }


                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding()
        )
        if (isAtBottom) {
            LaunchedEffect(isAtBottom) {
                !isLoading
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center

            ) {
                if (isLoading)
                    LoadingAnimation()
            }
        }
    }

}
@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel
    ){

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()


    val handleClick = handleClick()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.list.isEmpty() && state.isLoading) {
                LoadingAnimation()
        } else {
            if(state.list.isNotEmpty()) {
                ImageListItem(imageUrl = state.list ,
                    isLoading = state.isLoading,
                    onLoadMore = {
                        viewModel.loadMoreIfNeeded(currentIndex = state.list.size - 1)
                    },
                    onClick = { dataImage ->
                        val encodedUrl =
                            URLEncoder.encode(dataImage.img_url, StandardCharsets.UTF_8.toString())
                        navController.navigate("detail/$encodedUrl") {
                            launchSingleTop = true

                        }
                        Log.d("Navigate", "Navigating to: detail/$encodedUrl")
                        Log.d("123", "${state.list}")
                    })
            }
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
viewModel: HomeScreenViewModel
    ) {

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    val handleClick = handleClick()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (uiState.list.isEmpty()) {
            Text("No images found")
        } else {
            ImageListItemFolow(imageUrl = uiState.list, onClick = { dataImage ->

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,

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
                            onTabSelected(index)

                        }

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
@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 12.dp,
    circleColor: Color = Color.Magenta,
    spaceBetween: Dp = 5.dp,
    travelDistance: Dp = 10.dp
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
        }
    }

}
