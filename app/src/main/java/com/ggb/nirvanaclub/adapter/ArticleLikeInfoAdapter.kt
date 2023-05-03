package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.SwipeLayout.SwipeListener
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.ArticleCollectionInfoBean
import com.ggb.nirvanaclub.bean.ArticleLikeInfoBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil

class ArticleLikeInfoAdapter : BaseQuickAdapter<ArticleLikeInfoBean, BaseViewHolder>(R.layout.item_article_collection_info) {

    private val mUnCloseList: ArrayList<SwipeLayout> = ArrayList()

    override fun convert(helper: BaseViewHolder?, item: ArticleLikeInfoBean) {

        val sl = helper?.getView<SwipeLayout>(R.id.sl_collection)
        sl?.addSwipeListener(object : SwipeListener {
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

        if (item.blog != null){
            helper?.getView<TextView>(R.id.tv_collection_delete)?.text = "取消点赞"

            helper?.setText(R.id.tv_article_authorName,"  "+item.likeTime)
            helper?.setText(R.id.tv_article_authorName_down,item.blog.authName)
            helper?.setText(R.id.tv_article_title,item.blog.title)
            helper?.setText(R.id.tv_article_readCount,item.blog.readCount.toString())
            helper?.setText(R.id.tv_article_commentCount,item.blog.commentCount.toString())
            helper?.setText(R.id.tv_article_likeCount,item.blog.likeCount.toString())
            //新版本无用户头像信息
            ImageLoaderUtil().displayHeadImage(mContext,item.blog.authPhoto,helper?.getView(R.id.rci_article_author))

            //新版本默认只有一个图片作为主图
            helper?.getView<LinearLayout>(R.id.ll_article_0_img)?.visibility = View.GONE
            helper?.getView<LinearLayout>(R.id.ll_article_1_img)?.visibility = View.VISIBLE
            helper?.getView<LinearLayout>(R.id.ll_article_3_img)?.visibility = View.GONE
            helper?.setText(R.id.tv_article_one_content,item.blog.introduction)
            ImageLoaderUtil().displayHeadImage(mContext,item.blog.img,helper?.getView(R.id.rci_article_one_img))
        }
        helper?.addOnClickListener(R.id.tv_collection_copy)
        helper?.addOnClickListener(R.id.tv_collection_open)
        helper?.addOnClickListener(R.id.tv_collection_delete)
        helper?.addOnClickListener(R.id.ll_collection_content)
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