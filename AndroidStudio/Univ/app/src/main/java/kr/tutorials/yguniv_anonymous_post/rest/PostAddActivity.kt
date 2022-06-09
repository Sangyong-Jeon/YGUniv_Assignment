package kr.tutorials.yguniv_anonymous_post.rest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import kr.tutorials.yguniv_anonymous_post.MainViewModel
import kr.tutorials.yguniv_anonymous_post.R
import kr.tutorials.yguniv_anonymous_post.databinding.ActivityPostAddBinding
import retrofit2.Response

class PostAddActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy {
        ActivityPostAddBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 현재 액티비티 종료
        binding.postAddBtnPrev.setOnClickListener(this)
        // 게시글 등록
        binding.postAddBtnAddPost.setOnClickListener(this)

        viewModel.isAddPost.observe(this) {
            when (viewModel.isAddPost.value) {
                true -> {
                    Toast.makeText(this, "게시글 등록 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
                false -> Toast.makeText(this, "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.postAdd_btnPrev -> finish()
            R.id.postAdd_btnAddPost -> {
                val title = binding.postAddInputTitle.text.toString()
                val content = binding.postAddInputContent.text.toString()
                val password = binding.postAddInputPassword.text.toString()
                if (validatePostAdd(title, content, password)) {
                    viewModel.addPost(title, content, password)
                } else {
                    Toast.makeText(this, "제목, 내용, 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 공백 또는 화이트스페이스에 해당하는지 검증
    private fun validatePostAdd(title: String, content: String, password: String): Boolean {
        return when {
            title.isBlank() || content.isBlank() || password.isBlank() -> false
            else -> true
        }
    }
}