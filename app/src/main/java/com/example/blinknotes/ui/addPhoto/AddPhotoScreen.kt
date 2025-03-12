package com.example.blinknotes.ui.addPhoto


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.IconButton
import coil.compose.rememberAsyncImagePainter
import com.example.blinknotes.R
import com.example.blinknotes.navigation.Screens
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import java.util.UUID

@Composable
fun AddPhotoScreen(navController: NavHostController, imageUris: String?) {
    var caption by remember { mutableStateOf("") }
    val images = remember { mutableStateListOf<Uri>() }

    LaunchedEffect(imageUris) {
        imageUris?.split(",")?.mapNotNull { Uri.parse(it) }?.let { images.addAll(it) }
    }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            images.addAll(uris)
        }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(R.drawable.icon_back),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier
                    )
                }
                Text(
                    text = "Bản nháp",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {

                        },
                )
                Text(
                    text = "Đăng",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = colorResource(R.color.azure))
                        .clickable (
                            indication = null,
                            interactionSource = null
                        ) {

                        }
                )
            }
        }
    ){ paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(paddingValues = paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .padding(8.dp)
            ) {
                items(images) { uri ->
                    ItemsImage(
                        painter = rememberAsyncImagePainter(uri),
                        onEdit = {},
                        onDelete = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .clip(RoundedCornerShape(10.dp))
                            .aspectRatio(2f/3f)
                            .background(Color.LightGray)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = "Add Photo",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = caption,
                onValueChange = { caption = it },
                placeholder = { Text("Nhập nội dung bài viết...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(8.dp),
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
        }
    }

}
@Composable
fun ItemsImage(
    painter: Painter = rememberAsyncImagePainter(null),
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(Color.Gray)
    ) {
        // Hiển thị ảnh
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Nút xóa (X góc trên bên phải)
        IconButton(
            onClick = { onDelete() },
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Xóa ảnh",
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
            )
        }

        // Nút sửa (góc dưới bên phải)
        IconButton(
            onClick = { onEdit() },
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.BottomEnd)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Chỉnh sửa ảnh",
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
            )
        }
    }
}

fun uploadMultipleImagesToFirebase(imageUris: List<String>, caption: String, context: Context, onSuccess: () -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val firestore = FirebaseFirestore.getInstance()
    val uploadedImageUrls = mutableListOf<String>()

    imageUris.forEachIndexed { index, imageUri ->
        val fileRef = storageRef.child("uploads/${UUID.randomUUID()}.jpg")

        fileRef.putFile(Uri.parse(imageUri))
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    uploadedImageUrls.add(downloadUri.toString())

                    // Khi đã upload xong tất cả ảnh, lưu vào Firestore
                    if (uploadedImageUrls.size == imageUris.size) {
                        val post = hashMapOf(
                            "imageUrls" to uploadedImageUrls,
                            "caption" to caption,
                            "timestamp" to System.currentTimeMillis()
                        )

                        firestore.collection("posts").add(post)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Đăng bài thành công!", Toast.LENGTH_SHORT).show()
                                onSuccess()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Lỗi lưu dữ liệu!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi upload ảnh!", Toast.LENGTH_SHORT).show()
            }
    }
}

