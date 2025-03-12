package com.example.blinknotes.data.helper

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

// 1. Thêm người dùng vào Firestore
fun addUser(userId: String, username: String, email: String, profileImage: String, callback:(Boolean, String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val user = hashMapOf(
        "username" to username,
        "email" to email,
        "profileImage" to profileImage,
        "followers" to emptyList<String>(),
        "following" to emptyList<String>(),
        "createdAt" to System.currentTimeMillis()
    )

    db.collection("users").document(userId)
        .set(user)
        .addOnSuccessListener {
            callback(true, "Registration Successful")
        }
        .addOnFailureListener { e ->
            callback(false, "User registered, but failed to save profile: ${e.localizedMessage}")
        }
}

// 2. Đăng bài viết
fun addPost(postId: String, userId: String, imageUrl: String, caption: String) {
    val db = FirebaseFirestore.getInstance()
    val post = hashMapOf(
        "userId" to userId,
        "imageUrl" to imageUrl,
        "caption" to caption,
        "createdAt" to System.currentTimeMillis(),
        "likesCount" to 0,
        "commentsCount" to 0,
        "visibility" to "public",
        "tags" to listOf("travel", "food")
    )

    db.collection("posts").document(postId)
        .set(post)
        .addOnSuccessListener { println("Post added successfully") }
        .addOnFailureListener { e -> println("Error adding post: $e") }
}

// 3. Cập nhật bài viết (caption, visibility...)
fun updatePost(postId: String, caption: String, visibility: String) {
    val db = FirebaseFirestore.getInstance()
    val postRef = db.collection("posts").document(postId)

    postRef.update(
        "caption", caption,
        "visibility", visibility
    )
        .addOnSuccessListener { println("Post updated successfully") }
        .addOnFailureListener { e -> println("Error updating post: $e") }
}

// 4. Xóa bài viết
fun deletePost(postId: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("posts").document(postId)
        .delete()
        .addOnSuccessListener { println("Post deleted successfully") }
        .addOnFailureListener { e -> println("Error deleting post: $e") }
}

// 5. Lấy danh sách bài viết của người dùng
fun getPostsByUser(userId: String, onResult: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("posts")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { result ->
            val posts = result.documents.map { it.data!! }
            onResult(posts)
        }
        .addOnFailureListener { e -> println("Error getting posts: $e") }
}

// 6. Thêm bình luận vào bài viết
fun addComment(postId: String, userId: String, content: String) {
    val db = FirebaseFirestore.getInstance()
    val comment = hashMapOf(
        "postId" to postId,
        "userId" to userId,
        "content" to content,
        "createdAt" to System.currentTimeMillis(),
        "likesCount" to 0
    )

    db.collection("comments").add(comment)
        .addOnSuccessListener { println("Comment added successfully") }
        .addOnFailureListener { e -> println("Error adding comment: $e") }
}

// 7. Lấy bình luận của bài viết
fun getComments(postId: String, onResult: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("comments")
        .whereEqualTo("postId", postId)
        .get()
        .addOnSuccessListener { result ->
            val comments = result.documents.map { it.data!! }
            onResult(comments)
        }
        .addOnFailureListener { e -> println("Error getting comments: $e") }
}

// 8. Thích bài viết
fun likePost(postId: String, userId: String) {
    val db = FirebaseFirestore.getInstance()
    val like = hashMapOf(
        "postId" to postId,
        "userId" to userId,
        "createdAt" to System.currentTimeMillis()
    )

    db.collection("likes").add(like)
        .addOnSuccessListener { println("Post liked successfully") }
        .addOnFailureListener { e -> println("Error liking post: $e") }
}

// 9. Hủy thích bài viết
fun unlikePost(postId: String, userId: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("likes")
        .whereEqualTo("postId", postId)
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { result ->
            result.documents.forEach { doc ->
                db.collection("likes").document(doc.id).delete()
                    .addOnSuccessListener { println("Post unliked successfully") }
                    .addOnFailureListener { e -> println("Error unliking post: $e") }
            }
        }
        .addOnFailureListener { e -> println("Error fetching likes: $e") }
}

// 10. Lấy số lượng lượt thích của bài viết
fun getLikesCount(postId: String, onResult: (Int) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("likes")
        .whereEqualTo("postId", postId)
        .get()
        .addOnSuccessListener { result ->
            onResult(result.size())
        }
        .addOnFailureListener { e -> println("Error getting likes count: $e") }
}

// 11. Thêm người theo dõi
fun addFollower(userId: String, followerId: String) {
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("users").document(userId)

    userRef.update("followers", FieldValue.arrayUnion(followerId))
        .addOnSuccessListener { println("Follower added successfully") }
        .addOnFailureListener { e -> println("Error adding follower: $e") }
}

// 12. Hủy theo dõi
fun removeFollower(userId: String, followerId: String) {
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("users").document(userId)

    userRef.update("followers", FieldValue.arrayRemove(followerId))
        .addOnSuccessListener { println("Follower removed successfully") }
        .addOnFailureListener { e -> println("Error removing follower: $e") }
}

// 13. Tìm kiếm bài viết theo thẻ tag
fun searchPostsByTag(tag: String, onResult: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("posts")
        .whereArrayContains("tags", tag)
        .get()
        .addOnSuccessListener { result ->
            val posts = result.documents.map { it.data!! }
            onResult(posts)
        }
        .addOnFailureListener { e -> println("Error searching posts: $e") }
}
// 14. Lấy thông tin người dùng
fun getUser(userId: String, onResult: (Map<String, Any>?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                onResult(document.data)
            } else {
                println("User not found")
                onResult(null)
            }
        }
        .addOnFailureListener { e -> println("Error getting user: $e") }
}
