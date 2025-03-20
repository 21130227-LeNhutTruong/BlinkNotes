package com.example.blinknotes.ui.detaill

import android.util.Log
import com.example.blinknotes.R
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.blinknotes.ui.home.ExploreScreenViewModel
import com.example.blinknotes.ui.home.LoadingAnimation
import com.example.blinknotes.ui.home.Post
import com.example.blinknotes.ui.home.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetaillScreen(navController: NavController,
                  viewModel: ExploreScreenViewModel = viewModel(),
                  postId: String,
                  ) {

    var user by remember { mutableStateOf<User?>(null) }
    var post by remember { mutableStateOf<Post?>(null) }

    LaunchedEffect(postId) {
        Log.d("DetailScreen", "Fetching post with ID: $postId")
        viewModel.getPostByPostId(postId) { fetchedPost ->
            post = fetchedPost
            if (fetchedPost != null) {
                Log.d("DetailScreen", "Post fetched: $fetchedPost")
                Log.d("DetailScreen", "Fetching user with ID: ${fetchedPost.userId}")

                // Khi có bài post, lấy user theo userId
                viewModel.getUserById(fetchedPost.userId) { fetchedUser ->
                    user = fetchedUser
                    if (fetchedUser != null) {
                        Log.d("DetailScreen", "User fetched: $fetchedUser")
                    } else {
                        Log.e("DetailScreen", "User not found for ID: ${fetchedPost.userId}")
                    }
                }
            } else {
                Log.e("DetailScreen", "Post not found for ID: $postId")
            }
        }
    }

    Scaffold(
        bottomBar = {
            Divider()
            BottomBarDetail()
                    },
        topBar = {
            HeaderDetaill(
                navController = navController,
                username = user?.username ?: "Loading...",
                profileImage = user?.profileImage ?: ""
            )
        }
    ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Divider()
                if (post == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingAnimation()
                    }
                } else {
                    var timestamp = viewModel.getTimeAgo(post!!.createdAt)
                    ContentDetail(
                        imageUrls = post!!.imageUrls,
                        pageCount = post!!.imageUrls.size,
                        caption = post!!.caption,
                        content = post!!.content,
                        time = timestamp

                    )
                }
        }
    }
}
@Composable
fun ContentDetail(
    imageUrls: List<String>,
    pageCount: Int,
    caption:String,
    content:String,
    time:String
){

    val pagerState = rememberPagerState(pageCount = {pageCount })
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        HorizontalPager(state = pagerState,
            modifier = Modifier
                .background(color = colorResource(id = R.color.cornsilk))
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 400.dp)
            )
        }
        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .size( 6.dp)
                        .background(
                            if (index == pagerState.currentPage) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = caption,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 12.dp))
            Text(
                text = time,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 12.dp
                    , top = 12.dp, bottom = 12.dp))
        }
    }
}
@Composable
fun CommentContent(
    numComent: String
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "${numComent} bình luận ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}
@Composable
fun CommentItems(
    avatarUserComent: String
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            AsyncImage(
                model = avatarUserComent,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                
            }
        }
    }
}
@Composable
fun HeaderDetaill( navController: NavController, profileImage: String, username: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }
            AsyncImage(
                model = profileImage,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
            )

            Text(
                text = username,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.teal_200)),
            shape = RoundedCornerShape(24.dp),
            onClick = { /* Thêm logic Follow */ }
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
            onClick = { /* Thêm logic Search */ },
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
@Composable
fun ItemsImageDetail(
    painter: Painter = rememberAsyncImagePainter(null),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(Color.Gray)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

    }
}
