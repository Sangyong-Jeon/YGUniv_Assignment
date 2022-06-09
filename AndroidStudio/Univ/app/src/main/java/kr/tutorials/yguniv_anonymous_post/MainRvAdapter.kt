package kr.tutorials.yguniv_anonymous_post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.tutorials.yguniv_anonymous_post.rest.PostsBody
import java.util.*
import kotlin.collections.ArrayList

class MainRvAdapter : RecyclerView.Adapter<MainRvAdapter.Holder>() {
    fun interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    private var listener: OnItemClickListener? = null

    private var data: ArrayList<PostsBody> = ArrayList()

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(data: ArrayList<PostsBody>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getItem(position:Int): PostsBody {
        return data[position]
    }

    fun swapItem(from: Int, to: Int) {
        Collections.swap(data, from, to)
        notifyItemMoved(from, to)
    }

    fun removeItem(index:Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_rv_item, parent, false)
        return Holder(view, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]
        holder.postsId.text = item.id.toString()
        holder.postsTitle.text = item.title
        holder.postsCreatedDate.text = item.createDate
        holder.postsViewCount.text = item.viewCount.toString()
    }

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