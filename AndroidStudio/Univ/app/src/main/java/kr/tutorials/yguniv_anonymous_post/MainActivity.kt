package kr.tutorials.yguniv_anonymous_post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.tutorials.yguniv_anonymous_post.rest.PostAPI
import kr.tutorials.yguniv_anonymous_post.rest.ResultGetPosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Tag

class MainActivity : AppCompatActivity() {

//    var postList = arrayListOf<Post>(
//        Post("1", "제목1", "2022-01-01 13:13", 1),
//        Post("2", "제목2", "2022-01-01 13:13", 1),
//        Post("3", "제목3", "2022-01-01 13:13", 1),
//        Post("4", "제목4", "2022-01-01 13:13", 1),
//        Post("5", "제목5", "2022-01-01 13:13", 1),
//        Post("6", "제목6", "2022-01-01 13:13", 1),
//        Post("7", "제목7", "2022-01-01 13:13", 1),
//        Post("8", "제목8", "2022-01-01 13:13", 1),
//        Post("9", "제목9", "2022-01-01 13:13", 1),
//        Post("10", "제목10", "2022-01-01 13:13", 1),
//    )

    private var mRecyclerView: RecyclerView? = null
    private var url: String = "http://10.0.2.2:8080"
    private var posts = MutableLiveData<ResultGetPosts>()

    // API 요청 관련
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(PostAPI::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // api 관련
        api.getPosts("createdDateTime,desc").enqueue(object : Callback<ResultGetPosts> {
            override fun onResponse(
                call: Call<ResultGetPosts>,
                response: Response<ResultGetPosts>
            ) {
                println("====================================")
                posts.value = response.body()
                println(posts.value?.body)
                Log.d("RESPONSE", "성공 : ${response.raw()}")
                addPosts()
            }

            override fun onFailure(call: Call<ResultGetPosts>, t: Throwable) {
                Log.d("RESPONSE", "실패 : $t")
            }
        })


    }

    fun addPosts() {
        // 람다식 { (Dog) -> Unit } 부분을 추가하여 itemView의 setOnClickListener에서 어떤 액션을 취할지 설정해준다.
        val mAdapter = MainRvAdapter(this, posts.value?.body) { post ->
            Toast.makeText(
                this,
                "${post.id}번 | ${post.title} | ${post.createDate} | ${post.viewCount}",
                Toast.LENGTH_SHORT
            ).show()
        }

        mRecyclerView = findViewById(R.id.mRecyclerView)
        mRecyclerView?.adapter = mAdapter

        // RecyclerView의 각 item들을 배치하고, item이 더이상 보이지 않을때 재사용할것인지 결정하는 역할을 한다.
        val lm = LinearLayoutManager(this)
        mRecyclerView?.layoutManager = lm
        mRecyclerView?.setHasFixedSize(true)
    }
}