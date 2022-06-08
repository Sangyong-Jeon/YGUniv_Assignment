package kr.tutorials.yguniv_anonymous_post

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
import kr.tutorials.yguniv_anonymous_post.rest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // 홈화면 layout 요소
    private var homeSpinner: Spinner? = null
    private var homeMRecyclerView: RecyclerView? = null
    private var homeBtnTitleSearch: Button? = null
    private var homeBtnOrderBySearch: Button? = null
    private var homeBtnAdmin: Button? = null
    private var homeBtnPostAdd: Button? = null
    private var homeTextInputTitle: TextInputEditText? = null

    // 게시글 등록 페이지 layout 요소
    private var postAddBtnPrev: Button? = null
    private var postAddInputTitle: TextInputEditText? = null
    private var postAddInputContent: TextInputEditText? = null
    private var postAddInputPassword: EditText? = null
    private var postAddBtnPostAdd: Button? = null

    // 게시글 상세 조회 페이지 layout 요소
    private var postTvTitle: TextView? = null
    private var postTvContent: TextView? = null
    private var postTvViewCount: TextView? = null
    private var postTvCreatedDate: TextView? = null
    private var postTvUpdatedDate: TextView? = null
    private var postBtnPrev: Button? = null
    private var postBtnUpdatePost: Button? = null
    private var postBtnDeletePost: Button? = null

    // 게시글 수정 페이지 layout 요소
    private var postUpdateInputTitle: TextInputEditText? = null
    private var postUpdateInputContent: TextInputEditText? = null
    private var postUpdateInputPassword: EditText? = null
    private var postUpdateBtnPrev: Button? = null
    private var postUpdateBtnUpdatePost: Button? = null

    // api 요소
    private var url: String = "http://10.0.2.2:8080"
    private var retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var api = retrofit.create(PostAPI::class.java)

    // 게시글 리스트 값
    private var posts = MutableLiveData<ResponseData<ArrayList<PostsBody>>>()

    // 게시글 값
    private var post = MutableLiveData<ResponseData<PostBody>>()

    // 첫시작
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeViewHome()
    }

    // === 홈화면 페이지 === //
    private fun changeViewHome() {
        setContentView(R.layout.activity_main)
        getPosts("최신순")

        // 게시글 등록 페이지 전환
        homeBtnPostAdd = findViewById(R.id.home_btnPostAdd)
        homeBtnPostAdd?.setOnClickListener {
            changeViewPostAdd()
        }

        // 홈화면 정렬 선택지
        homeSpinner = findViewById(R.id.home_spinner)
        homeSpinner?.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        )

        // 서버 url 설정 버튼
        homeBtnAdmin = findViewById(R.id.home_btnAdmin)
        homeBtnAdmin?.setOnClickListener {
            val editText = EditText(this)
            editText.gravity = Gravity.CENTER
            editText.hint = "서버 API 주소 입력"
            modalUpdateServerUrl(editText)
        }

        // 제목 검색 버튼
        homeTextInputTitle = findViewById(R.id.home_textInputTitle)
        homeBtnTitleSearch = findViewById(R.id.home_btnTitleSearch)
        homeBtnTitleSearch?.setOnClickListener {
            getTitlePosts(homeTextInputTitle?.text.toString())
            Toast.makeText(this, "${homeTextInputTitle?.text} 조회", Toast.LENGTH_SHORT).show()
        }

        // 정렬 검색 버튼
        homeBtnOrderBySearch = findViewById(R.id.home_btnOrderBySearch)
        homeBtnOrderBySearch?.setOnClickListener {
            getPosts(homeSpinner?.selectedItem.toString())
            Toast.makeText(
                this,
                "${homeSpinner?.selectedItem.toString()} 정렬 전체 조회",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    // === 게시글 등록 페이지 === //
    private fun changeViewPostAdd() {
        setContentView(R.layout.activity_post_add)
        postAddInputTitle = findViewById(R.id.postAdd_inputTitle)
        postAddInputContent = findViewById(R.id.postAdd_inputContent)
        postAddInputPassword = findViewById(R.id.postAdd_inputPassword)

        // 이전 화면 전환 (홈화면)
        postAddBtnPrev = findViewById(R.id.postAdd_btnPrev)
        postAddBtnPrev?.setOnClickListener {
            changeViewHome()
        }

        // 게시글 등록
        postAddBtnPostAdd = findViewById(R.id.postAdd_btnAddPost)
        postAddBtnPostAdd?.setOnClickListener {
            val title = postAddInputTitle?.text.toString()
            val content = postAddInputContent?.text.toString()
            val password = postAddInputPassword?.text.toString()

            if (validatePostAdd(title, content, password)) {
                addPost(title, content, password)
                Toast.makeText(this, "게시글 등록 완료", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "제목, 내용, 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // === 게시글 상세조회 페이지 === //
    private fun changeViewPostDetail(id: Long) {
        setContentView(R.layout.activity_post)
        postTvTitle = findViewById(R.id.post_tvTitle)
        postTvContent = findViewById(R.id.post_tvContent)
        postTvViewCount = findViewById(R.id.post_tvViewCount)
        postTvCreatedDate = findViewById(R.id.post_tvCreatedDate)
        postTvUpdatedDate = findViewById(R.id.post_tvUpdatedDate)

        // 이전 화면 전환 (홈화면)
        postBtnPrev = findViewById(R.id.post_btnPrev)
        postBtnPrev?.setOnClickListener {
            changeViewHome()
        }

        // 게시글 수정 요청
        postBtnUpdatePost = findViewById(R.id.post_btnUpdatePost)
        postBtnUpdatePost?.setOnClickListener {
            changeViewPostUpdate()
        }

        // 게시글 삭제 요청
        postBtnDeletePost = findViewById(R.id.post_btnDeletePost)
        postBtnDeletePost?.setOnClickListener {
            val editText = EditText(this)
            editText.gravity = Gravity.CENTER
            editText.hint = "비밀번호 입력"
            modalDeletePostPw(editText, id)

        }

        // 게시글 상세 조회 요청
        getPost(id)
    }

    // 대화상자 열어서 비밀번호 입력
    private fun modalDeletePostPw(editText: EditText, id: Long) {
        AlertDialog.Builder(this)
            .setTitle("비밀번호를 입력하세요")
            .setMessage("게시글을 작성했을 당시의 비밀번호를 입력하세요.")
            .setView(editText)
            .setPositiveButton("입력") { _, _ ->
                deletePost(id, editText.text.toString())
            }
            .setNegativeButton("취소") { _, _ -> }
            .show()
    }

    // 게시글 삭제 요청
    private fun deletePost(id: Long, password: String) {
        api.deletePost(id, password).enqueue(object : Callback<ResponseData<String>> {
            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                if (response.code() == 200) {
                    changeViewHome()
                    Log.i("deletePost", "게시글 삭제 성공 : ${response.raw()}")
                    Toast.makeText(this@MainActivity, "게시글 삭제 완료", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.e("deletePost", "게시글 삭제 실패 : ${response.errorBody()?.string()!!}")
                    Toast.makeText(this@MainActivity, "게시글 삭제 실패", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 삭제 실패 : $t")
            }
        })
    }

    // === 게시글 수정 페이지 === //
    private fun changeViewPostUpdate() {
        setContentView(R.layout.activity_post_update)
        postUpdateInputPassword = findViewById(R.id.postUpdate_inputPassword)

        // 게시글 상세 조회한 값 가져와서 수정 페이지에 사용
        var postBody = post.value?.body
        var title = postBody?.title.toString()
        var content = postBody?.content.toString()
        var password: String

        // 수정할 게시글 값들 넣어놓기
        postUpdateInputTitle = findViewById(R.id.postUpdate_inputTitle)
        postUpdateInputTitle?.setText(title)
        postUpdateInputContent = findViewById(R.id.postUpdate_inputContent)
        postUpdateInputContent?.setText(content)

        // 이전 화면 전환 (게시글 상세 조회)
        postUpdateBtnPrev = findViewById(R.id.postUpdate_btnPrev)
        postUpdateBtnPrev?.setOnClickListener {
            changeViewPostDetail(0L)
        }

        // 게시글 수정 요청
        postUpdateBtnUpdatePost = findViewById(R.id.postUpdate_btnUpdatePost)
        postUpdateBtnUpdatePost?.setOnClickListener {
            title = postUpdateInputTitle?.text.toString()
            content = postUpdateInputContent?.text.toString()
            password = postUpdateInputPassword?.text.toString()
            postBody?.let { updatePost(postBody.id, title, content, password) }
        }
    }

    // 게시글 수정 요청
    private fun updatePost(id: Long, title: String, content: String, password: String) {
        val postForm = PostForm(title, content, password)

        api.updatePost(id, postForm).enqueue(object : Callback<ResponseData<String>> {
            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                if (response.code() == 200) {
                    changeViewHome()
                    Log.i("updatePost", "게시글 수정 성공 : ${response.raw()}")
                    Toast.makeText(this@MainActivity, "게시글 수정 완료", Toast.LENGTH_SHORT).show()
                } else {
                    var errorMessage = response.errorBody()?.string()!!.substring(56, 67)
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 수정 실패 : $t")
            }
        })
    }

    // 공백 또는 화이트스페이스에 해당하는지 검증
    private fun validatePostAdd(title: String, content: String, password: String): Boolean {
        return when {
            title.isBlank() || content.isBlank() || password.isBlank() -> false
            else -> true
        }
    }

    // 대화상자를 열어서 서버 URL 변경
    private fun modalUpdateServerUrl(editText: EditText) {
        AlertDialog.Builder(this)
            .setTitle("서버 API 주소를 입력하세요")
            .setMessage("기본값 : http://10.0.2.2:8080\n현재값 : $url")
            .setView(editText)
            .setPositiveButton("설정") { _, _ ->
                validateServerUrl(editText)
            }
            .setNegativeButton("취소") { _, _ ->
                Toast.makeText(this, "서버 URL 변경 취소", Toast.LENGTH_SHORT).show()
            }.show()
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
        api.getTitlePost(title).enqueue(object : Callback<ResponseData<ArrayList<PostsBody>>> {
            override fun onResponse(
                call: Call<ResponseData<ArrayList<PostsBody>>>,
                response: Response<ResponseData<ArrayList<PostsBody>>>
            ) {
                posts.value = response.body()
                Log.i("RESPONSE", "게시글 제목 검색 성공 : ${response.raw()}")
                showPosts()
            }

            override fun onFailure(call: Call<ResponseData<ArrayList<PostsBody>>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 제목 검색 실패 : $t")
            }
        })
    }

    // 게시글 상세 조회
    private fun getPost(id: Long) {
        if (id != 0L) {
            api.getPost(id).enqueue(object : Callback<ResponseData<PostBody>> {
                override fun onResponse(
                    call: Call<ResponseData<PostBody>>,
                    response: Response<ResponseData<PostBody>>
                ) {
                    post.value = response.body()
                    showPost()
                    Log.i("RESPONSE", "게시글 상세 조회 성공 : ${response.raw()}")
                }

                override fun onFailure(call: Call<ResponseData<PostBody>>, t: Throwable) {
                    Log.e("RESPONSE", "게시글 상세 조회 실패 : $t")
                }

            })
        } else {
            showPost()
        }
    }

    // 게시글 상세 조회 페이지 갱신
    private fun showPost() {
        var postBody = post.value?.body
        postTvTitle?.text = postBody?.title
        postTvContent?.text = postBody?.content
        postTvViewCount?.text = postBody?.viewCount.toString()
        postTvCreatedDate?.text = postBody?.createDate
        postTvUpdatedDate?.text = postBody?.updateDate
    }

    // 게시글 전체 조회 (정렬순)
    private fun getPosts(orderBy: String) {
        val queryString: String = when (orderBy) {
            "최신순" -> "createdDateTime,desc"
            else -> "viewCount,desc"
        }

        api.getPosts(queryString).enqueue(object : Callback<ResponseData<ArrayList<PostsBody>>> {
            override fun onResponse(
                call: Call<ResponseData<ArrayList<PostsBody>>>,
                response: Response<ResponseData<ArrayList<PostsBody>>>
            ) {
                posts.value = response.body()
                Log.i("RESPONSE", "게시글 전체 조회 성공 : ${response.raw()}")
                showPosts()
            }

            override fun onFailure(call: Call<ResponseData<ArrayList<PostsBody>>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 전체 조회 실패 : $t")
            }
        })
    }

    // 홈화면 게시글 리스트 갱신
    private fun showPosts() {
        // 람다식 { (Dog) -> Unit } 부분을 추가하여 itemView의 setOnClickListener에서 어떤 액션을 취할지 설정해준다.
        val mAdapter = MainRvAdapter(this, posts.value?.body) { post ->
            changeViewPostDetail(post.id)
        }

        homeMRecyclerView = findViewById(R.id.home_mRecyclerView)
        homeMRecyclerView?.adapter = mAdapter

        // RecyclerView의 각 item들을 배치하고, item이 더이상 보이지 않을때 재사용할것인지 결정하는 역할을 한다.
        val lm = LinearLayoutManager(this)
        homeMRecyclerView?.layoutManager = lm
        homeMRecyclerView?.setHasFixedSize(true)
    }

    // 게시글 등록
    private fun addPost(title: String, content: String, password: String) {
        val postForm: PostForm = PostForm(title, content, password)
        api.addPost(postForm).enqueue(object : Callback<ResponseData<String>> {
            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                if (response.code() == 200) changeViewHome()
                Log.i("RESPONSE", "게시글 등록 성공 : ${response.raw()}")
            }

            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                Log.e("RESPONSE", "게시글 등록 실패 : $t")
            }
        })
    }
}
