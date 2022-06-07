package kr.tutorials.yguniv_anonymous_post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.tutorials.yguniv_anonymous_post.rest.PostsBody

// 1. Adapter의 파라미터에 람다식 itemClick을 넣는다.
class MainRvAdapter(
    val context: Context,
    val postList: ArrayList<PostsBody>?,
    val itemClick: (PostsBody) -> Unit
) : RecyclerView.Adapter<MainRvAdapter.Holder>() {

    // 화면을 최초 로딩하여 만들어진 View가 없는 경우, xml파일을 inflategㅏ여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, p0, false)
        // 4. Holder의 파라미터가 하나 더 추가됐으므로, 이곳에도 추가해준다.
        return Holder(view, itemClick)
    }

    // RecyclerView로 만들어지는 item의 총 개수를 반환한다.
    override fun getItemCount(): Int {
        postList?.let { return postList.size }
        return 0
    }

    // 위의 onCreateViewHolder에서 만든 view의 실제 입력되는 각각의 데이터를 연결한다.
    override fun onBindViewHolder(p0: Holder, position: Int) {
        postList?.get(position)?.let { p0?.bind(it, context) }
    }

    // 2. Holder에서 클릭에 대한 처리를 할 것이므로 Holder의 파라미터에 람다식 itemClick을 넣는다.
    inner class Holder(itemView: View?, itemClick: (PostsBody) -> Unit) :
        RecyclerView.ViewHolder(itemView!!) {
        val postsId = itemView?.findViewById<TextView>(R.id.postsId)
        val postsTitle = itemView?.findViewById<TextView>(R.id.postsTitle)
        val postsCreatedDate = itemView?.findViewById<TextView>(R.id.postsCreatedDate)
        val postsViewCount = itemView?.findViewById<TextView>(R.id.postsViewCount)

        fun bind(post: PostsBody, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            postsId?.text = post.id.toString()
            postsTitle?.text = post.title
            postsCreatedDate?.text = post.createDate
            postsViewCount?.text = post.viewCount.toString()

            // 3. itemView가 클릭됐을 때 처리할 일을 itemClick으로 설정한다.
            // (Dog) -> Unit 에 대한 함수는 나중에 MainActivity.kt 클래스에서 작성한다.
            itemView.setOnClickListener { itemClick(post) }
        }
    }


}