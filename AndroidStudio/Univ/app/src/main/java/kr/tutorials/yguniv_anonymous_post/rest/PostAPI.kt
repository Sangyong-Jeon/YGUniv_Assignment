package kr.tutorials.yguniv_anonymous_post.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostAPI {
    // 전체조회 (정렬순)
    @GET("/api/posts")
    fun getPosts(
        @Query("sort") sort: String
    ): Call<ResultGetPosts>

    // 게시글 상세 조회
    @GET("/api/posts/{postId}")
    fun getPost(@Path("postId") postId: Int): Call<ResultGetPost>

    // 게시글 검색
    @GET("/api/posts/search")
    fun getTitlePost(@Query("search") search: String): Call<ResultGetPosts>
}