package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexArticleInfoBean
import com.ggb.nirvanaclub.bean.IndexArticleInfoListBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil

class IndexArticleInfoPagingAdapter : BaseQuickAdapter<IndexArticleInfoListBean, BaseViewHolder>(R.layout.item_article_info) {

    override fun convert(helper: BaseViewHolder?, item: IndexArticleInfoListBean) {
        helper?.setText(R.id.tv_article_authorName,item.authorName)
        helper?.setText(R.id.tv_article_authorName_down,item.authorName)
        helper?.setText(R.id.tv_article_title,item.title)
        helper?.setText(R.id.tv_article_readCount,item.readCount)
        helper?.setText(R.id.tv_article_commentCount,item.commentCount)
        helper?.setText(R.id.tv_article_likeCount,item.likeCount)
        ImageLoaderUtil().displayHeadImage(mContext,item.authorAvatar,helper?.getView(R.id.rci_article_author))

        if (item.img1.isNullOrEmpty() && item.img2.isNullOrEmpty() &&item.img3.isNullOrEmpty()){
            helper?.getView<LinearLayout>(R.id.ll_article_0_img)?.visibility = View.VISIBLE
            helper?.getView<LinearLayout>(R.id.ll_article_1_img)?.visibility = View.GONE
            helper?.getView<LinearLayout>(R.id.ll_article_3_img)?.visibility = View.GONE
            helper?.setText(R.id.tv_article_zero_content,item.content)
        }
        if (item.img1?.isNotEmpty() == true && item.img2.isNullOrEmpty() && item.img3.isNullOrEmpty()){
            helper?.getView<LinearLayout>(R.id.ll_article_0_img)?.visibility = View.GONE
            helper?.getView<LinearLayout>(R.id.ll_article_1_img)?.visibility = View.VISIBLE
            helper?.getView<LinearLayout>(R.id.ll_article_3_img)?.visibility = View.GONE
            helper?.setText(R.id.tv_article_one_content,item.content)
            ImageLoaderUtil().displayHeadImage(mContext,item.img1,helper?.getView(R.id.rci_article_one_img))
        }
        if (item.img1?.isNotEmpty() == true && item.img2?.isNotEmpty() == true && item.img3?.isNotEmpty() == true){
            helper?.getView<LinearLayout>(R.id.ll_article_0_img)?.visibility = View.GONE
            helper?.getView<LinearLayout>(R.id.ll_article_1_img)?.visibility = View.GONE
            helper?.getView<LinearLayout>(R.id.ll_article_3_img)?.visibility = View.VISIBLE
            ImageLoaderUtil().displayHeadImage(mContext,item.img1,helper?.getView(R.id.rci_article_img1))
            ImageLoaderUtil().displayHeadImage(mContext,item.img2,helper?.getView(R.id.rci_article_img2))
            ImageLoaderUtil().displayHeadImage(mContext,item.img3,helper?.getView(R.id.rci_article_img3))
        }
    }
}