package com.example.blinknotes.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinknotes.data.DataImage
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    var image by mutableStateOf<List<DataImage>>(emptyList())
init {
    fetchImage()
}
    private fun fetchImage(){
        viewModelScope.launch {
            try {
                val res = listOf(
                  "https://i.pinimg.com/736x/42/a1/06/42a1068eb0bcdb65da6e60bf49af66fe.jpg",
                   "https://internetviettel.vn/wp-content/uploads/2017/05/H%C3%ACnh-%E1%BA%A3nh-minh-h%E1%BB%8Da.jpg",
                   "https://didongviet.vn/dchannel/wp-content/uploads/2023/08/dong-vat-hinh-nen-iphone-doc-dep-didongviet-23@2x-min-1-576x1024.jpg",
                   "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/hinh-nen-4k-20.jpg",
                    "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/hinh-nen-4k-21.jpg"
                    )
                image = res.map { DataImage(it) }
            }catch (e: Exception){
                Log.e("aaa","error ${e.localizedMessage}")
            }
        }
    }

}