package kr.tutorials.yguniv_anonymous_post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kr.tutorials.yguniv_anonymous_post.rest.PostAPI
import kr.tutorials.yguniv_anonymous_post.rest.ResultGetPosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // layout 요소
    private var spinner: Spinner? = null
    private var mRecyclerView: RecyclerView? = null
    private var btnTitleSearch: Button? = null
    private var btnOrderBySearch: Button? = null

    // api 요소
    private var url: String = "http://10.0.2.2:8080"
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(PostAPI::class.java)

    // 전체 게시판 값
    private var posts = MutableLiveData<ResultGetPosts>()

    // 첫시작
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 게시글 전체 조회 (최신순)
        getPosts("최신순")

        // spinner
        spinner = findViewById(R.id.spinner)
        spinner?.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        )

        // 제목 검색 버튼
        val textTitle = findViewById<TextInputEditText>(R.id.textInputTitle)
        btnTitleSearch = findViewById(R.id.btnTitleSearch)
        btnTitleSearch?.setOnClickListener {
            getTitlePosts(textTitle.text.toString())
            Toast.makeText(this, "${textTitle.text} 조회", Toast.LENGTH_SHORT)
                .show()
        }

        // 정렬 검색 버튼
        btnOrderBySearch = findViewById(R.id.btnOrderBySearch)
        btnOrderBySearch?.setOnClickListener {
            getPosts(spinner?.selectedItem.toString())
            Toast.makeText(this, "${spinner?.selectedItem.toString()} 정렬 조회", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getTitlePosts(title: String) {
        api.getTitlePost(title).enqueue(object : Callback<ResultGetPosts> {
            override fun onResponse(
                call: Call<ResultGetPosts>,
                response: Response<ResultGetPosts>
            ) {
                posts.value = response.body()
                Log.d("RESPONSE", "성공 : ${response.raw()}")
                showPosts()
            }

            override fun onFailure(call: Call<ResultGetPosts>, t: Throwable) {
                Log.d("RESPONSE", "실패 : $t")
            }
        })
    }

    private fun getPosts(orderBy: String) {
        val queryString: String = when (orderBy) {
            "최신순" -> "createdDateTime,desc"
            else -> "viewCount,desc"
        }

        api.getPosts(queryString).enqueue(object : Callback<ResultGetPosts> {
            override fun onResponse(
                call: Call<ResultGetPosts>,
                response: Response<ResultGetPosts>
            ) {
                posts.value = response.body()
                Log.d("RESPONSE", "성공 : ${response.raw()}")
                showPosts()
            }

            override fun onFailure(call: Call<ResultGetPosts>, t: Throwable) {
                Log.d("RESPONSE", "실패 : $t")
            }
        })
    }

    private fun showPosts() {
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