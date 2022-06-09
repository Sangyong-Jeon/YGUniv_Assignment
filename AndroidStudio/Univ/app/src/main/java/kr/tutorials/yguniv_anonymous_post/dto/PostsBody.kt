package kr.tutorials.yguniv_anonymous_post.dto

data class PostsBody(
    var id: Long,
    var title: String,
    var createDate: String,
    var viewCount: Int
)
