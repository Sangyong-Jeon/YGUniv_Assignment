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
import kr.tutorials.yguniv_anonymous_post.api.*
import androidx.activity.viewModels

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var adapter = MainRvAdapter()
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerView()
        binding.homeBtnPostAdd.setOnClickListener(this)// 게시글 등록 페이지 전환
        binding.homeBtnTitleSearch.setOnClickListener(this)// 제목 검색 버튼
        binding.homeBtnOrderBySearch.setOnClickListener(this)// 정렬 검색 버튼
        binding.homeBtnAdmin.setOnClickListener(this)// 서버 url 설정 버튼
        binding.homeSpinner.adapter = ArrayAdapter.createFromResource(// 홈화면 정렬 선택지
            this,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        )

        // posts 상태변경시 실행
        viewModel.posts.observe(this) {
            viewModel.posts.value?.body?.let { it -> adapter.setData(it) }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart()")
        viewModel.getPosts("최신순")
    }

    // RecyclerView 초기화
    private fun initRecyclerView() {
        val manager = LinearLayoutManager(this)
        binding.homeRecyclerView.layoutManager = manager
        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.adapter = adapter
        adapter.setListener { _, position ->
            val data = adapter.getItem(position)
            changeViewPostDetail(data.id)
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
            .setMessage("기본값 : http://10.0.2.2:8080\n현재값 : ${SpringServer.url}")
            .setView(editText)
            .setPositiveButton("설정") { _, _ ->
                validateServerUrl(editText)
            }
            .setNegativeButton("취소") { _, _ ->
                Toast.makeText(this, "서버 URL 변경 취소", Toast.LENGTH_SHORT).show()
            }.show()
    }

    // 사용자가 작성한 서버 URL 검증 후 변경
    private fun validateServerUrl(editText: EditText) {
        if (editText.text.length > 3 && editText.text.substring(0..3) == "http") {
            SpringServer.url = editText.text.toString()
            SpringServer.renewServerUrl()
            Toast.makeText(this, "서버 URL이 변경되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "올바르지 않은 서버 URL입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
