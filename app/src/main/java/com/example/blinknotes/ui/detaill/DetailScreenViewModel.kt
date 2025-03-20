package com.example.blinknotes.ui.detaill

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinknotes.data.helper.FirestoreHelper.getAllPosts
import com.example.blinknotes.ui.home.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel  : ViewModel() {
//    fun loadMorePosts() {
//        viewModelScope.launch {
//            delay(1000L)
//            getAllPosts(lastVisiblePost) { newPosts ->
//                if (newPosts.isNotEmpty()) {
//                    lastVisiblePost = newPosts.last()
//                    _posts.value = _posts.value + newPosts
//                }
//            }
//        }
//    }
fun getPostByPostId(postId: String, callback: (Post?) -> Unit) {
    FirebaseFirestore.getInstance().collection("posts")
        .document(postId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val post = document.toObject(Post::class.java)?.copy(id = document.id)
                callback(post)
            } else {
                callback(null)  // Không tìm thấy bài viết
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error fetching post: ${e.message}")
            callback(null)
        }
}

}