package com.ggb.nirvanaclub.adapter


import android.view.LayoutInflater
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.CommunityKnowledgeBean
import com.google.android.flexbox.FlexboxLayout
import java.util.*


class CommunityKnowledgeAdapter : BaseQuickAdapter<CommunityKnowledgeBean, BaseViewHolder>(R.layout.item_community_knowledge) {

    private val mFlexItemTextViewCaches: Queue<TextView> = LinkedList()
    private var mInflater: LayoutInflater? = null

    private var mOnItemClickListener: OnItemClickListener? = null
    internal fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onClick(bean: CommunityKnowledgeBean, pos: Int)
    }

    override fun convert(helper: BaseViewHolder, item: CommunityKnowledgeBean) {
        helper.getView<TextView>(R.id.tv_community_knowledge).text = item.name

        val fbl: FlexboxLayout = helper.getView(R.id.fl_community_knowledge) as FlexboxLayout
        //防止重复添加
        fbl.removeAllViews()
        item.children.let {
            for (i in 0 until it.size) {
                val childTextView = createOrGetCacheFlexItemTextView(fbl)
                childTextView.text = it[i].name
                childTextView.setOnClickListener {
                    mOnItemClickListener?.onClick(item, i)
                }
                fbl.addView(childTextView)
            }
        }
    }

    private fun createOrGetCacheFlexItemTextView(fbl: FlexboxLayout): TextView {
        val tv = mFlexItemTextViewCaches.poll()
        return tv ?: createFlexItemTextView(fbl)
    }

    private fun createFlexItemTextView(fbl: FlexboxLayout): TextView {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(fbl.context)
        }
        return mInflater!!.inflate(R.layout.item_knowledge_text, fbl, false) as TextView
    }
}
