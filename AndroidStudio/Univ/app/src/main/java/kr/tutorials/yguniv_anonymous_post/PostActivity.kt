package kr.tutorials.yguniv_anonymous_post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import kr.tutorials.yguniv_anonymous_post.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity(), View.OnClickListener {

    private var isCheck: Boolean = true
    private var id: Long? = null
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityPostBinding.inflate(layoutInflater) }
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            isCheck = it.data?.getBooleanExtra("result", true) == true
        }

    override fun onResume() {
        super.onResume()
        if (isCheck) viewModel.getPost(id!!)
        else finish()
        Log.i("PostActivity", "onResume()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("PostActivity", "onCreate()")
        setContentView(binding.root)

        // 이전 화면 전환 (홈화면)
        binding.postBtnPrev.setOnClickListener(this)
        // 게시글 수정 요청
        binding.postBtnUpdatePost.setOnClickListener(this)
        // 게시글 삭제 요청
        binding.postBtnDeletePost.setOnClickListener(this)

        with(intent) {
            id = getLongExtra("id", 0)
        }

        viewModel.post.observe(this) {
            showPost()
        }

        viewModel.isDeletePost.observe(this) {
            when (viewModel.isDeletePost.value) {
                true -> {
                    Toast.makeText(this, "게시글 삭제 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
                false -> Toast.makeText(this, "게시글 삭제 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.post_btnPrev -> finish()
            R.id.post_btnUpdatePost -> changeViewPostUpdate()
            R.id.post_btnDeletePost -> {
                val editText = EditText(this)
                editText.gravity = Gravity.CENTER
                editText.hint = "비밀번호 입력"
                modalDeletePostPw(editText, viewModel.post.value?.body?.id!!)
            }
        }
    }

    // 게시글 수정 화면 전환
    private fun changeViewPostUpdate() {
        val title = viewModel.post.value?.body?.title
        val content = viewModel.post.value?.body?.content
        val intent = Intent(this, PostUpdateActivity::class.java).apply {
            putExtra("id", id)
            putExtra("title", title)
            putExtra("content", content)
        }
        getResult.launch(intent)
    }

    // 대화상자 열어서 비밀번호 입력
    private fun modalDeletePostPw(editText: EditText, id: Long) {
        AlertDialog.Builder(this)
            .setTitle("비밀번호를 입력하세요")
            .setMessage("게시글을 작성했을 당시의 비밀번호를 입력하세요.")
            .setView(editText)
            .setPositiveButton("입력") { _, _ -> viewModel.deletePost(id, editText.text.toString()) }
            .setNegativeButton("취소") { _, _ -> }
            .show()
    }

    // 게시글 상세 조회 페이지 갱신
    private fun showPost() {
        var postBody = viewModel.post.value?.body
        binding.postTvTitle.text = postBody?.title
        binding.postTvContent.text = postBody?.content
        binding.postTvViewCount.text = postBody?.viewCount.toString()
        binding.postTvCreatedDate.text = postBody?.createDate
        binding.postTvUpdatedDate.text = postBody?.updateDate
    }
}