package com.example.blinknotes.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.blinknotes.R
import com.example.blinknotes.ui.eventClick.handleClick
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ImageListItem(
    listImageUrl: List<Feed>,
    numberHeart: Int?,
    imageLink: String?,
    avatarLink: String?,
    userName: String?,
    status: String?,
    onClickItems: () -> Unit
) {
    val imageListState = rememberLazyStaggeredGridState()

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
            contentPadding = PaddingValues(bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            content = {
                items(listImageUrl, key = { it }) { image ->
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable {
                                onClickItems()
//                                handleClick(coroutineScope) {
//                                    val encodedUrl = URLEncoder.encode(
//                                        image.img_url,
//                                        StandardCharsets.UTF_8.toString()
//                                    )
//                                    onClick(image)
//                                    Log.d(
//                                        "Navigate",
//                                        "Điều hướng tới: detail/$encodedUrl"
//                                    )
//
//                                }
                            }

                    ) {
                        ItemsFeed(
                            imageLink = imageLink,
                            avatarLink =avatarLink ,
                            numberHeart = numberHeart,
                            status = status ,
                            userName = userName )
                    }


                }

            },
            modifier = Modifier

        )

    }

}
@Composable
fun ItemsFeed(
    imageLink :String?,
    avatarLink :String?,
    numberHeart: Int?,
    status: String?,
    userName: String?
){
    var isFavorite by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val formattedTextnumberHeart = formatNumberHeart(numberHeart)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(end = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = imageLink,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(id = R.drawable.splash),
            onSuccess = {
                Log.d(
                    "Screen1",
                    "Image loaded successfully  ${imageLink}"
                )
            },

            )
        Text(
            text = status.toString(),
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.Start),
            fontStyle = FontStyle.Normal,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, start = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,


            ) {
                AsyncImage(
                    model = avatarLink,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.logo_app),
                )

                Text(
                    text = userName.toString(),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = formattedTextnumberHeart,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(end = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable (
                            interactionSource = interactionSource,
                            indication = null
                        ) {

                            isFavorite = !isFavorite

                        }
                )
            }


        }

    }
}
@Composable
fun ImageListItemFolow(
    listImageUrl: List<Feed>,
    onClick: (Feed) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()
    val handleClick = handleClick()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(listImageUrl, key = { it }) { image  ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .clickable {
                            handleClick(coroutineScope) {
                                val encodedUrl = URLEncoder.encode(
                                    image.imageLink,
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
                            model = image.imageLink,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                                .clip(RoundedCornerShape(4.dp)),
                            placeholder = painterResource(id = R.drawable.splash),
                            onSuccess = { Log.d("Screen1", "Image loaded successfully  ${image.imageLink}") },

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
    )
}
fun formatNumberHeart(number: Int?): String {
    if (number != null) {
        return when {
            number >= 10_000 -> String.format("%.1f N", number / 10000f)
            number >= 1000 -> String.format("%,.3f", number / 1000f)
            else -> number.toString()
        }
    }
    return TODO("Provide the return value")
}
