package com.example.blinknotes.ui.addPhoto

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinknotes.ui.home.Feed
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddPhotoScreenViewModel: ViewModel() {
    var photos by mutableStateOf<List<Feed>>(emptyList())
init {

}

    fun fetchPhotos() {
        viewModelScope.launch {
            try {
               // val response = MarsApi.retrofitService.getPhotos()
                photos = photos
            } catch (e: Exception) {
            }
        }
    }
    fun addPhoto(photo: Feed, photoFile: File) {
        viewModelScope.launch {
            try {
                // Upload ảnh lên
               // val photoRequestBody = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
              //  val photoPart = MultipartBody.Part.createFormData("photo", photoFile.name, photoRequestBody)

               // MarsApi.retrofitService.addPhoto(photoPart, idRequestBody, urlRequestBody)

                // Thêm ảnh vào danh sách
                photos = photos + photo
            } catch (e: Exception) {
            }
        }
    }
}