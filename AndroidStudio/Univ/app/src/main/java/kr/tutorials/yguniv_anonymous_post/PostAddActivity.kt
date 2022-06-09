package kr.tutorials.yguniv_anonymous_post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import kr.tutorials.yguniv_anonymous_post.databinding.ActivityPostAddBinding

class PostAddActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityPostAddBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.postAddBtnPrev.setOnClickListener(this)// 현재 액티비티 종료
        binding.postAddBtnAddPost.setOnClickListener(this)// 게시글 등록

        viewModel.isAddPost.observe(this) {
            if (viewModel.isAddPost.value == true) {
                Toast.makeText(this, "게시글 등록 완료", Toast.LENGTH_SHORT).show()
                finish()
            } else Toast.makeText(this, "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.postAdd_btnPrev -> finish()
            R.id.postAdd_btnAddPost -> requestAddPost()
        }
    }

    // 게시글 등록 요청
    private fun requestAddPost() {
        val title = binding.postAddInputTitle.text.toString()
        val content = binding.postAddInputContent.text.toString()
        val password = binding.postAddInputPassword.text.toString()
        if (validatePostAdd(title, content, password)) {
            viewModel.addPost(title, content, password)
        } else {
            Toast.makeText(this, "제목, 내용, 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 공백 또는 화이트스페이스에 해당하는지 검증
    private fun validatePostAdd(title: String, content: String, password: String): Boolean {
        return title.isNotBlank() || content.isNotBlank() || password.isNotBlank()
    }
}