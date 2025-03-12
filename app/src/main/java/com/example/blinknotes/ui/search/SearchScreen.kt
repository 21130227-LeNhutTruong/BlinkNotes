package com.example.blinknotes.ui.search


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blinknotes.R

@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchScreenViewModelFactory){

    Scaffold (
        modifier = Modifier
            .padding(12.dp)
    ){ paddingValues ->
        SearchScreen(modifier = Modifier
            .padding(paddingValues = paddingValues))
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier
) {
    var searchText by remember { mutableStateOf("") }
    val historySearches = remember { mutableStateListOf("áo sơ mi nam đẹp", "thẻ booyah tháng 12", "review phim full trọn bộ") }
    val suggestions = mapOf(
        "Ẩm thực" to listOf("táo kẹp sữa lạc đà", "gà rán chiên giòn", "sữa ngâm mắm nhĩ"),
        "Trang phục" to listOf("style đơn giản mà sang", "pijama tiểu thư", "kính mắt mèo nữ"),
        "Thú cưng" to listOf("mèo xinh", "chuột hamster", "chó cỏ")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Thanh tìm kiếm
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Xử lý back */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nhập từ khóa tìm kiếm") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            if (searchText.isNotEmpty()) {
                IconButton(onClick = { searchText = "" }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lịch sử tìm kiếm
        historySearches.forEach { query ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { searchText = query }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(R.drawable.timer), contentDescription = "History", tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = query, fontSize = 16.sp, modifier = Modifier.weight(1f))
                IconButton(onClick = { historySearches.remove(query) }) {
                    Icon(Icons.Default.Close, contentDescription = "Remove")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gợi ý theo danh mục
        suggestions.forEach { (category, items) ->
            Text(text = category, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            items.forEach { suggestion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { searchText = suggestion }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = suggestion, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
