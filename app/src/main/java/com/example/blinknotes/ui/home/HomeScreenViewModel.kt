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
                    "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/hinh-nen-4k-21.jpg",
                    "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/hinh-dep-5.jpg",
                    "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/anh-dep-40.jpg",
                    "https://mega.com.vn/media/news/1706_hinh-nen-dien-thoai-4k96.jpg",
                    "https://m.yodycdn.com/blog/anh-dep-3d-yodyvn.jpg",
                    "https://m.yodycdn.com/blog/anh-dep-3d-yodyvn10.jpg",
                    "https://m.yodycdn.com/blog/anh-dep-3d-yodyvn1.jpg",
                    "https://mensfolio.vn/wp-content/uploads/2023/04/04102023_MFonline_kpiai3dnew1.jpg",
                    "https://thoitrangdongphuc.com.vn/wp-content/uploads/2022/06/hinh-in-3d-dep-phi-hanh-gia.jpg",
                    "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/09/hinh-nen-phi-hanh-gia-15.jpg",
                    "https://hinhanhonline.com/Hinhanh/images11/AnhAL/hinh-nen-dien-thoai-4k-danh-cho-cac-game-thu.jpg",
                    "https://cdn-media.sforum.vn/storage/app/media/wp-content/uploads/2022/02/BG-4.jpg",
                    "https://mega.com.vn/media/news/1706_hinh-nen-phi-hanh-gia37.jpg",
                    "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/09/hinh-nen-phi-hanh-gia-30.jpg",
                    "https://i.pinimg.com/originals/21/4a/86/214a86b204193e3c25c5e5ff7e17837b.webp",
                    "https://demoda.vn/wp-content/uploads/2022/02/anh-phi-hanh-gia-dep-nhat.jpg",

                    )
                image = res.map { DataImage(it) }
            }catch (e: Exception){
                Log.e("aaa","error ${e.localizedMessage}")
            }
        }
    }

}