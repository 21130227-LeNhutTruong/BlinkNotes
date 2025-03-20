package com.example.blinknotes.ui.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinknotes.data.helper.FirestoreHelper
import com.example.blinknotes.data.helper.FirestoreHelper.getAllPosts
import com.example.blinknotes.data.helper.FirestoreHelper.getUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Post(
    val id: String = "",
    val userId: String = "",
    val imageUrls: List<String> = emptyList(),
    val firstImageUrl:String="",
    val caption: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val visibility: String = "public",
    val tags: List<String> = emptyList()
)
data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val profileImage: String = "",
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
class ExploreScreenViewModel : ViewModel() {
    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> = _selectedImages

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _users = MutableStateFlow<Map<String, User?>>(emptyMap())
    val users: StateFlow<Map<String, User?>> = _users

    private val _userCache = mutableMapOf<String, User?>()

    private var lastVisiblePost: Post? = null
    internal var isLoading = false
    private val pageSize = 5

    init {
        loadMorePosts()
    }

    fun fetchUser(userId: String) {
        if (_userCache.containsKey(userId)) {
            _users.value = _users.value + (userId to _userCache[userId])
            return
        }
        viewModelScope.launch {
            getUser(userId) { fetchedUser ->
                _userCache[userId] = fetchedUser
                _users.value = _users.value + (userId to fetchedUser)
                if (fetchedUser != null) {
                    Log.d("FirestoreUser", "User loaded: $fetchedUser")
                } else {
                    Log.e("Firestore", "User not found")
                }
            }
        }
    }
    fun getUserById(userId: String, callback: (User?) -> Unit) {
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)?.copy(userId = document.id)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching user: ${e.message}")
                callback(null)
            }
    }
    fun getPostByPostId(postId: String, callback: (Post?) -> Unit) {
        FirebaseFirestore.getInstance().collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val post = document.toObject(Post::class.java)?.copy(id = document.id)
                    callback(post)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching post: ${e.message}")
                callback(null)
            }
    }
    fun loadMorePosts() {
        viewModelScope.launch {
            delay(1000L)
            getAllPosts(lastVisiblePost) { newPosts ->
                if (newPosts.isNotEmpty()) {
                    lastVisiblePost = newPosts.last()
                    _posts.value = _posts.value + newPosts
                }
            }
        }
    }
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7

        return when {
            minutes < 1 -> "Vừa xong"
            minutes < 60 -> "$minutes phút trước"
            hours < 24 -> "$hours giờ trước"
            days < 7 -> "$days ngày trước"
            weeks < 4 -> "$weeks tuần trước"
            else -> {
                // Nếu hơn 1 tháng, hiển thị dạng "30 tháng ba"
                val sdf = SimpleDateFormat("dd 'tháng' MM", Locale("vi"))
                sdf.format(Date(timestamp))
            }
        }
    }


}
