package kr.tutorials.yguniv_anonymous_post

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private var btnAdmin: Button? = null
    private var textTitle: TextInputEditText? = null

    // api 요소
    private var url: String = "http://10.0.2.2:8080"
    private var retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var api = retrofit.create(PostAPI::class.java)

    // 게시글 리스트 값
    private var posts = MutableLiveData<ResultGetPosts>()

    // 첫시작
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 게시글 전체 조회 (최신순)
        getPosts("최신순")

        // 홈화면 정렬 선택지
        spinner = findViewById(R.id.spinner)
        spinner?.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        )

        // 서버 url 설정 버튼
        btnAdmin = findViewById(R.id.btnAdmin)
        btnAdmin?.setOnClickListener {
            val editText = EditText(this)
            editText.gravity = Gravity.CENTER
            editText.hint = "서버 API 주소 입력"
            modalUpdateServerUrl(editText)
        }


        // 제목 검색 버튼
        textTitle = findViewById(R.id.textInputTitle)
        btnTitleSearch = findViewById(R.id.btnTitleSearch)
        btnTitleSearch?.setOnClickListener {
            getTitlePosts(textTitle?.text.toString())
            Toast.makeText(this, "${textTitle?.text} 조회", Toast.LENGTH_SHORT).show()
        }

        // 정렬 검색 버튼
        btnOrderBySearch = findViewById(R.id.btnOrderBySearch)
        btnOrderBySearch?.setOnClickListener {
            getPosts(spinner?.selectedItem.toString())
            Toast.makeText(this, "${spinner?.selectedItem.toString()} 정렬 전체 조회", Toast.LENGTH_SHORT).show()
        }
    }

    // 대화상자를 열어서 서버 URL 변경
    private fun modalUpdateServerUrl(editText: EditText) {
        AlertDialog.Builder(this)
            .setTitle("서버 API 주소를 입력하세요")
            .setMessage("기본값 : http://10.0.2.2:8080\n현재값 : $url")
            .setView(editText)
            .setPositiveButton("설정", DialogInterface.OnClickListener { dialog, which ->
                validateServerUrl(editText)
            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "서버 URL 변경 취소", Toast.LENGTH_SHORT).show()
            }).show()
    }

    // 사용자가 작성한 서버 URL 검증
    private fun validateServerUrl(editText: EditText) {
        if (editText.text.length > 3 && editText.text.substring(0..3) == "http") {
            Log.i("INFO", "올바른 서버 URL 입니다.")
            url = editText.text.toString()
            renewServerUrl()
            Toast.makeText(this, "서버 URL이 변경되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Log.w("WARNING", "올바르지 않은 서버 URL 입니다.")
            Toast.makeText(this, "올바르지 않은 서버 URL입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // 서버 URL 변경
    private fun renewServerUrl() {
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(PostAPI::class.java)
    }

    // 게시글 제목 검색
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
                Log.e("RESPONSE", "실패 : $t")
            }
        })
    }

    // 게시글 전체 조회 (정렬순)
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
                Log.e("RESPONSE", "실패 : $t")
            }
        })
    }

    // 홈화면 게시글 리스트 갱신
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
