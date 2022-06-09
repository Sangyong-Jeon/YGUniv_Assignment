package kr.tutorials.yguniv_anonymous_post.dto

data class PostBody(
    var id: Long,
    var title: String,
    var content: String,
    var createDate: String,
    var updateDate: String,
    var viewCount: Int
)