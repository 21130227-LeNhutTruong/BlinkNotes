package com.example.blinknotes.data.helper

import android.util.Log
import com.example.blinknotes.ui.home.Post
import com.example.blinknotes.ui.home.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query

object FirestoreHelper {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // 1. Thêm người dùng vào Firestore
    fun addUser(userId: String, username: String, email: String, profileImage: String, callback: (Boolean, String) -> Unit) {
        val user = mapOf(
            "username" to username,
            "email" to email,
            "profileImage" to profileImage,
            "followers" to emptyList<String>(),
            "following" to emptyList<String>(),
            "createdAt" to System.currentTimeMillis()
        )
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener { callback(true, "Registration Successful") }
            .addOnFailureListener { e -> callback(false, "Error saving profile: ${e.localizedMessage}") }
    }

    // 2. Đăng bài viết
    fun addPost(postId: String, userId: String, imageUrl: String, caption: String, content: String) {
        val post = mapOf(
            "userId" to userId,
            "imageUrl" to imageUrl,
            "caption" to caption,
            "content" to content,
            "createdAt" to System.currentTimeMillis(),
            "likesCount" to 0,
            "commentsCount" to 0,
            "visibility" to "public",
            "tags" to listOf("travel", "food")
        )
        db.collection("posts").document(postId)
            .set(post)
            .addOnFailureListener { e -> Log.e("Firestore", "Error adding post: $e") }
    }

    // 3. Cập nhật bài viết
    fun updatePost(postId: String, caption: String, visibility: String) {
        db.collection("posts").document(postId)
            .update(mapOf("caption" to caption, "visibility" to visibility))
            .addOnFailureListener { e -> Log.e("Firestore", "Error updating post: $e") }
    }

    // 4. Xóa bài viết
    fun deletePost(postId: String) {
        db.collection("posts").document(postId)
            .delete()
            .addOnFailureListener { e -> Log.e("Firestore", "Error deleting post: $e") }
    }

    // 5. Lấy danh sách bài viết
    fun getPostsByUser(userId: String, onResult: (List<Map<String, Any>>) -> Unit) {
        db.collection("posts").whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result -> onResult(result.documents.mapNotNull { it.data }) }
            .addOnFailureListener { e -> Log.e("Firestore", "Error getting posts: $e") }
    }

    fun getAllPosts(lastPost: Post?,callback: (List<Post>) -> Unit) {
        var query = FirebaseFirestore.getInstance().collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            //.limit(pageSize.toLong())

        // Nếu có bài viết cuối cùng, dùng nó làm điểm bắt đầu cho trang tiếp theo
        lastPost?.let {
            query = query.startAfter(it.createdAt)
        }

        query.get()
            .addOnSuccessListener { result ->
                val postsList = result.documents.mapNotNull { doc ->
                    val id = doc.id
                    val userId = doc.getString("userId") ?: ""
                    val imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList()
                    val firstImageUrl = imageUrls.firstOrNull() ?: ""
                    val caption = doc.getString("caption") ?: ""
                    val content = doc.getString("content") ?: ""
                    val createdAt = doc.getLong("createdAt") ?: 0L
                    val likesCount = doc.getLong("likesCount")?.toInt() ?: 0
                    val commentsCount = doc.getLong("commentsCount")?.toInt() ?: 0
                    val visibility = doc.getString("visibility") ?: "public"
                    val tags = doc.get("tags") as? List<String> ?: emptyList()

                    Post(id, userId, imageUrls, firstImageUrl, caption, content, createdAt, likesCount, commentsCount, visibility, tags)
                }

                callback(postsList)
            }
            .addOnFailureListener { e ->
                callback(emptyList()) // Trả về danh sách rỗng nếu có lỗi
                Log.e("Firestore", "Lỗi khi tải dữ liệu: ${e.message}")
            }
    }


    // 6. Thêm bình luận
    fun addComment(postId: String, userId: String, content: String) {
        val comment = mapOf(
            "postId" to postId,
            "userId" to userId,
            "content" to content,
            "createdAt" to System.currentTimeMillis(),
            "likesCount" to 0
        )
        db.collection("comments").add(comment)
            .addOnFailureListener { e -> Log.e("Firestore", "Error adding comment: $e") }
    }

    // 7. Lấy bình luận
    fun getComments(postId: String, onResult: (List<Map<String, Any>>) -> Unit) {
        db.collection("comments").whereEqualTo("postId", postId)
            .get()
            .addOnSuccessListener { result -> onResult(result.documents.mapNotNull { it.data }) }
            .addOnFailureListener { e -> Log.e("Firestore", "Error getting comments: $e") }
    }

    // 8. Thích bài viết
    fun likePost(postId: String, userId: String) {
        db.collection("likes").add(mapOf("postId" to postId, "userId" to userId, "createdAt" to System.currentTimeMillis()))
            .addOnFailureListener { e -> Log.e("Firestore", "Error liking post: $e") }
    }

    // 9. Hủy thích bài viết
    fun unlikePost(postId: String, userId: String) {
        db.collection("likes").whereEqualTo("postId", postId).whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach { doc ->
                    db.collection("likes").document(doc.id).delete()
                        .addOnFailureListener { e -> Log.e("Firestore", "Error unliking post: $e") }
                }
            }
            .addOnFailureListener { e -> Log.e("Firestore", "Error fetching likes: $e") }
    }

    // 10. Lấy số lượng lượt thích
    fun getLikesCount(postId: String, onResult: (Int) -> Unit) {
        db.collection("likes").whereEqualTo("postId", postId).get()
            .addOnSuccessListener { result -> onResult(result.size()) }
            .addOnFailureListener { e -> Log.e("Firestore", "Error getting likes count: $e") }
    }

    // 11. Quản lý theo dõi
    fun updateFollowers(userId: String, followerId: String, add: Boolean) {
        val fieldOp = if (add) FieldValue.arrayUnion(followerId) else FieldValue.arrayRemove(followerId)
        db.collection("users").document(userId)
            .update("followers", fieldOp)
            .addOnFailureListener { e -> Log.e("Firestore", "Error updating followers: $e") }
    }

    // 12. Tìm kiếm bài viết theo thẻ tag
    fun searchPostsByTag(tag: String, onResult: (List<Map<String, Any>>) -> Unit) {
        db.collection("posts").whereArrayContains("tags", tag)
            .get()
            .addOnSuccessListener { result -> onResult(result.documents.mapNotNull { it.data }) }
            .addOnFailureListener { e -> Log.e("Firestore", "Error searching posts: $e") }
    }

    // 13. Lấy thông tin người dùng
    fun getUser(userId: String, onResult: (User?) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)?.copy(userId = document.id)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { e -> Log.e("Firestore", "Error getting user: ${e.message}") }
    }
}
