package kr.tutorials.yguniv_anonymous_post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.tutorials.yguniv_anonymous_post.dto.PostsBody
import kotlin.collections.ArrayList

// RecyclerView 내부를 채우는 각 항목을 제공하는 역할
class MainRvAdapter : RecyclerView.Adapter<MainRvAdapter.Holder>() {

    private var listener: OnItemClickListener? = null
    private var data: ArrayList<PostsBody> = ArrayList()

    fun interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(data: ArrayList<PostsBody>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getItem(position: Int): PostsBody {
        return data[position]
    }

    // Data 개수 알려준다. 만약 0을 반환하면 RecyclerView에는 아무것도 그려지지 않는다.
    override fun getItemCount(): Int {
        return data.size
    }

    // Data보단 적지만 RecyclerView를 가득 채우고 스크롤 할 수 있을 만큼 ViewHolder를 생성하고, 어떤 xml 파일을 inflate 할 지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_rv_item, parent, false)
        return Holder(view, listener)
    }

    // ViewHolder에 Data 내용 넣는 작업
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]
        holder.postsId.text = item.id.toString()
        holder.postsTitle.text = item.title
        holder.postsCreatedDate.text = item.createDate
        holder.postsViewCount.text = item.viewCount.toString()
    }

    // RecyclerView.ViewHolder를 상속받는 클래스로 커스텀 ViewHolder이다.
    class Holder(view: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(view) {
        val postsId: TextView = view.findViewById(R.id.home_postsId)
        val postsTitle: TextView = view.findViewById(R.id.home_postsTitle)
        val postsCreatedDate: TextView = view.findViewById(R.id.home_postsCreatedDate)
        val postsViewCount: TextView = view.findViewById(R.id.home_postsViewCount)

        init {
            view.setOnClickListener {
                listener?.onItemClick(view, this.layoutPosition)
            }
        }
    }
}