package kr.tutorials.yguniv_anonymous_post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kr.tutorials.yguniv_anonymous_post.databinding.ActivityMainBinding
import kr.tutorials.yguniv_anonymous_post.rest.*
import androidx.activity.viewModels

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity","onStart()")
        viewModel.getPosts("최신순")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity","onStop()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity","onCreate()")
        setContentView(binding.root)

        // 게시글 등록 페이지 전환
        binding.homeBtnPostAdd.setOnClickListener(this)
        // 제목 검색 버튼
        binding.homeBtnTitleSearch.setOnClickListener(this)
        // 정렬 검색 버튼
        binding.homeBtnOrderBySearch.setOnClickListener(this)
        // 서버 url 설정 버튼
        binding.homeBtnAdmin.setOnClickListener(this)
        // 홈화면 정렬 선택지
        binding.homeSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        )

        // RecyclerView의 각 item들을 배치하고, item이 더이상 보이지 않을때 재사용할것인지 결정하는 역할을 한다.
        val lm = LinearLayoutManager(this)
        binding.homeMRecyclerView.layoutManager = lm
        binding.homeMRecyclerView.setHasFixedSize(true)

        // posts가 바뀔때마다 실행
        viewModel.posts.observe(this) {
            // 람다식 { (Dog) -> Unit } 부분을 추가하여 itemView의 setOnClickListener에서 어떤 액션을 취할지 설정해준다.
            val mAdapter = MainRvAdapter(this, viewModel.posts.value?.body) { post ->
                changeViewPostDetail(post.id)
            }
            binding.homeMRecyclerView.adapter = mAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_btnPostAdd -> changeViewPostAdd()
            R.id.home_btnTitleSearch -> {
                viewModel.getTitlePosts(binding.homeTextInputTitle.text.toString())
                Toast.makeText(this, "${binding.homeTextInputTitle.text} 조회", Toast.LENGTH_SHORT)
                    .show()
            }
            R.id.home_btnOrderBySearch -> {
                viewModel.getPosts(binding.homeSpinner.selectedItem.toString())
                Toast.makeText(
                    this,
                    "${binding.homeSpinner.selectedItem} 정렬 전체 조회",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.home_btnAdmin -> {
                val editText = EditText(this)
                editText.gravity = Gravity.CENTER
                editText.hint = "서버 API 주소 입력"
                modalUpdateServerUrl(editText)
            }
        }
    }

    // === 게시글 상세조회 페이지 === //
    private fun changeViewPostDetail(id: Long) {
        val intent = Intent(this, PostActivity::class.java).apply {
            putExtra("id", id)
        }
        startActivity(intent)
    }

    // === 게시글 등록 페이지 === //
    private fun changeViewPostAdd() {
        val intent = Intent(this, PostAddActivity::class.java)
        startActivity(intent)
    }

    // 대화상자를 열어서 서버 URL 변경
    private fun modalUpdateServerUrl(editText: EditText) {
        AlertDialog.Builder(this)
            .setTitle("서버 API 주소를 입력하세요")
            .setMessage("기본값 : http://10.0.2.2:8080\n현재값 : ${JsServer.url}")
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
            JsServer.url = editText.text.toString()
            JsServer.renewServerUrl()
            Toast.makeText(this, "서버 URL이 변경되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "올바르지 않은 서버 URL입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
