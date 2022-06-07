package kr.tutorials.yguniv_anonymous_post.rest

data class ResultGetPosts(var header: Header, var body: ArrayList<PostsBody>)

data class Header(var status: Int, var code: String, var message: String)

data class PostsBody(var id: Int, var title: String, var createDate: String, var viewCount: Int)