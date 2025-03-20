package com.example.blinknotes.navigation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.IconButton
import com.example.blinknotes.R
import com.example.blinknotes.ui.addPhoto.AddPhotoScreenViewModel


data class NavigationItem(
    val icon: ImageVector,
    val iconOutline : ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<NavigationItem>, viewModel: AddPhotoScreenViewModel = viewModel()) {
    val context = LocalContext.current

    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }
//    val selectedImages = remember { mutableStateListOf<Uri>() }
//    val imagePickerLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
//            if (uris.isNotEmpty()) {
//                selectedImages.addAll(uris)
//                val imageUris = selectedImages.joinToString(",") { it.toString() }
//                navController.navigate("${Screens.AddPhotoScreen.route}?imageUris=${Uri.encode(imageUris)}")
//            }
//        }
    val selectedImages by viewModel.selectedImages.collectAsState()

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            viewModel.addSelectedImages( uris)
        }
    Box (
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                    ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Box(
                        modifier = Modifier.size(45.dp)
                            .background(
                                color = Color.Blue,
                                shape = CircleShape
                            )
                            .border(3.dp, Color.White, CircleShape)
                            .shadow(8.dp, shape = CircleShape)
                            .align(alignment = Alignment.Top),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                       // imagePickerLauncher.launch("image/*")
                                        navController.navigate(Screens.AddPhotoScreen.route)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.plus),
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(32.dp),
                                contentDescription = null,
                                alignment = Alignment.Center
                            )
                        }
                    }

                }
                IconButton(
                    onClick = {
                        selected.value = item.icon
                        navController.navigate(item.route) {
                            popUpTo(Screens.HomeScreen.route) { inclusive = false }
                        }
                    },
                ) {
                    Icon(
                        imageVector = if (selected.value == item.icon) item.icon else item.iconOutline,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = Color.Black

                    )
                }
            }
        }
    }
}