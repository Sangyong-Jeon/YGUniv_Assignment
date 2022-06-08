package kr.tutorials.yguniv_anonymous_post.rest

import retrofit2.Call
import retrofit2.http.*

interface PostAPI {

    // 전체조회 (정렬순)
    @GET("/api/posts")
    fun getPosts(@Query("sort") sort: String): Call<ResponseData<ArrayList<PostsBody>>>

    // 게시글 상세 조회
    @GET("/api/posts/{postId}")
    fun getPost(@Path("postId") postId: Long): Call<ResponseData<PostBody>>

    // 게시글 검색
    @GET("/api/posts/search")
    fun getTitlePost(@Query("search") search: String): Call<ResponseData<ArrayList<PostsBody>>>

    // 게시글 등록
    @POST("/api/posts")
    fun addPost(@Body postFormDto: PostForm): Call<ResponseData<String>>

    // 게시글 수정
    @PATCH("/api/posts/{postId}")
    fun updatePost(
        @Path("postId") postId: Long,
        @Body postFormDto: PostForm
    ): Call<ResponseData<String>>
}