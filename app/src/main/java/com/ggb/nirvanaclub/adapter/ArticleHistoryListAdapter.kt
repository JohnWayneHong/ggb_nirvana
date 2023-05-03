package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daimajia.swipe.SwipeLayout
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.ArticleHistoryBean

class ArticleHistoryListAdapter: BaseQuickAdapter<ArticleHistoryBean, BaseViewHolder>(R.layout.item_history_list){

    private val mUnCloseList: ArrayList<SwipeLayout> = ArrayList()

    override fun convert(helper: BaseViewHolder?, item: ArticleHistoryBean?) {

        val sl = helper?.getView<SwipeLayout>(R.id.sl_history)
        sl?.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onStartOpen(layout: SwipeLayout) {
                closeAll(layout)
            }

            override fun onOpen(layout: SwipeLayout) {
                mUnCloseList.add(layout)
            }

            override fun onStartClose(layout: SwipeLayout) {}
            override fun onClose(layout: SwipeLayout) {
                mUnCloseList.remove(layout)
            }

            override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
            override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
        })

        helper?.setText(R.id.tv_article_title,item?.blog?.title)
        helper?.setText(R.id.tv_article_count,item?.viewTime)

        helper?.addOnClickListener(R.id.tv_history_copy)
        helper?.addOnClickListener(R.id.tv_history_open)
        helper?.addOnClickListener(R.id.tv_history_delete)
        helper?.addOnClickListener(R.id.ll_history_content)
    }
    fun closeAll(layout: SwipeLayout?) {
        for (swipeLayout in mUnCloseList) {
            if (layout === swipeLayout) {
                continue
            }
            if (swipeLayout.openStatus != SwipeLayout.Status.Open) {
                continue
            }
            swipeLayout.close()
        }
    }
}