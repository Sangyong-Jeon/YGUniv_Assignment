package kr.tutorials.yguniv_anonymous_post.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostAPI {
    // GET /posts 전체조회
    @GET("/api/posts")
    fun getPosts(
        @Query("sort") sort: String
    ): Call<ResultGetPosts>

    @GET("/api/posts/{postId}")
    fun getPost(@Path("postId") postId:Int):Call<ResultGetPost>
}