package kr.tutorials.yguniv_anonymous_post

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import kr.tutorials.yguniv_anonymous_post.databinding.ActivityPostUpdateBinding
import kr.tutorials.yguniv_anonymous_post.rest.PostForm
import kr.tutorials.yguniv_anonymous_post.rest.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var id: Long? = null
    private var title: String? = null
    private var content: String? = null
    private var password: String = ""
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityPostUpdateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setResult(Activity.RESULT_CANCELED)

        with(intent) {
            id = getLongExtra("id", 0)
            title = getStringExtra("title")
            content = getStringExtra("content")
        }

        // 수정할 게시글 값 넣어놓기
        binding.postUpdateInputTitle.setText(title)
        binding.postUpdateInputContent.setText(content)

        // 게시글 수정 요청할 때마다 실행
        viewModel.isUpdatePost.observe(this) {
            if (viewModel.isUpdatePost.value == true) {
                intent.putExtra("result", false)
                setResult(RESULT_OK, intent)
                Toast.makeText(this, "게시글 수정을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "게시글 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 이전 화면 전환 (게시글 상세 조회)
        binding.postUpdateBtnPrev.setOnClickListener(this)
        // 게시글 수정 요청
        binding.postUpdateBtnUpdatePost.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.postUpdate_btnPrev -> finish()
            R.id.postUpdate_btnUpdatePost -> {
                title = binding.postUpdateInputTitle.text.toString()
                content = binding.postUpdateInputContent.text.toString()
                password = binding.postUpdateInputPassword.text.toString()
                viewModel.updatePost(id!!, title!!, content!!, password!!)
            }
        }
    }
}