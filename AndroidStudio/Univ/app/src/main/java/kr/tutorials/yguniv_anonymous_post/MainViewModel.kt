package kr.tutorials.yguniv_anonymous_post

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.tutorials.yguniv_anonymous_post.rest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    var error = MutableLiveData<String>()
    var post = MutableLiveData<ResponseData<PostBody>>()
    var posts = MutableLiveData<ResponseData<ArrayList<PostsBody>>>()
    var isAddPost = MutableLiveData<Boolean>()
    var isDeletePost = MutableLiveData<Boolean>()
    var isUpdatePost = MutableLiveData<Boolean>()
    lateinit var request: Call<ResponseData<ArrayList<PostsBody>>>

    fun getPosts(orderBy: String) {
        val queryString: String = when (orderBy) {
            "최신순" -> "createdDateTime,desc"
            else -> "viewCount,desc"
        }
        val request = JsServer.postApi.getPosts(queryString)
        request.enqueue(object : Callback<ResponseData<ArrayList<PostsBody>>> {
            override fun onResponse(
                call: Call<ResponseData<ArrayList<PostsBody>>>,
                response: Response<ResponseData<ArrayList<PostsBody>>>
            ) {
                posts.value = response.body()
                Log.i("RESPONSE", "게시글 전체 조회 성공 : ${response.raw()}")
            }

            override fun onFailure(call: Call<ResponseData<ArrayList<PostsBody>>>, t: Throwable) {
                error.value = t.localizedMessage
                Log.e("RESPONSE", "게시글 전체 조회 실패 : $t")
            }
        })
    }

    fun getPost(id: Long) {
        val request = JsServer.postApi.getPost(id)
        request.enqueue(object : Callback<ResponseData<PostBody>> {
            override fun onResponse(
                call: Call<ResponseData<PostBody>>,
                response: Response<ResponseData<PostBody>>
            ) {
                post.value = response.body()
                Log.i("RESPONSE", "게시글 상세 조회 성공 : ${response.raw()}")
            }

            override fun onFailure(call: Call<ResponseData<PostBody>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 상세 조회 실패 : $t")
            }
        })
    }

    fun addPost(title: String, content: String, password: String) {
        val postForm = PostForm(title, content, password)
        val request = JsServer.postApi.addPost(postForm)

        request.enqueue(object : Callback<ResponseData<String>> {
            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                isAddPost.value = true
                Log.i("RESPONSE", "게시글 등록 성공 : ${response.raw()}")
            }

            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                isAddPost.value = false
                Log.e("RESPONSE", "게시글 등록 실패 : $t")
            }
        })
    }

    fun deletePost(id: Long, password: String) {
        val request = JsServer.postApi.deletePost(id, password)
        request.enqueue(object : Callback<ResponseData<String>> {
            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                if (response.code() == 200) {
                    isDeletePost.value = true
                    Log.i("deletePost", "게시글 삭제 성공 : ${response.raw()}")
                } else {
                    isDeletePost.value = false
                    Log.e("deletePost", "게시글 삭제 실패 : ${response.errorBody()?.string()!!}")
                }
            }

            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                isDeletePost.value = false
                Log.e("RESPONSE", "게시글 삭제 실패 : $t")
            }
        })
    }

    fun updatePost(id: Long, title: String, content: String, password: String) {
        val postForm = PostForm(title, content, password)

        val request = JsServer.postApi.updatePost(id, postForm)
        request.enqueue(object : Callback<ResponseData<String>> {
            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                if (response.code() == 200) {
                    isUpdatePost.value = true
                    Log.i("updatePost", "게시글 수정 성공 : ${response.raw()}")
                } else {
                    isUpdatePost.value = false
                    Log.e("updatePost", "게시글 수정 실패 : ${response.errorBody()?.string()!!}")
                }
            }

            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                isUpdatePost.value = false
                Log.e("RESPONSE", "게시글 수정 실패 : $t")
            }
        })
    }

    // 게시글 제목 검색
    fun getTitlePosts(title: String) {
        val request = JsServer.postApi.getTitlePost(title)
        request.enqueue(object : Callback<ResponseData<ArrayList<PostsBody>>> {
            override fun onResponse(
                call: Call<ResponseData<ArrayList<PostsBody>>>,
                response: Response<ResponseData<ArrayList<PostsBody>>>
            ) {
                posts.value = response.body()
                Log.i("RESPONSE", "게시글 제목 검색 성공 : ${response.raw()}")
            }

            override fun onFailure(call: Call<ResponseData<ArrayList<PostsBody>>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 제목 검색 실패 : $t")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        if (::request.isInitialized) request.cancel()
    }
}