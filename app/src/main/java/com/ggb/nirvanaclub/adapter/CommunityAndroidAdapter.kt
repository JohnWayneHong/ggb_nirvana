package com.ggb.nirvanaclub.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.CommunityAndroidListBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil


class CommunityAndroidAdapter : BaseQuickAdapter<CommunityAndroidListBean, BaseViewHolder>(R.layout.item_community_android_list) {

    override fun convert(holder: BaseViewHolder, item: CommunityAndroidListBean) {
        val authorStr = if (item.author.isNotEmpty()) item.author else item.shareUser
        holder.setText(R.id.tv_article_android_title, Html.fromHtml(item.title))
            .setText(R.id.tv_article_author, authorStr)
            .setText(R.id.tv_article_date, item.niceDate)
            .setImageResource(
                R.id.iv_like,
                if (item.collect) R.drawable.ic_community_like else R.drawable.ic_community_like_not
            )
        val chapterName = when {
            item.superChapterName.isNotEmpty() and item.chapterName.isNotEmpty() -> {
                "${item.superChapterName} / ${item.chapterName}"
            }
            item.superChapterName.isNotEmpty() -> {
                item.superChapterName
            }
            item.chapterName.isNotEmpty() -> {
                item.chapterName
            }
            else -> ""
        }
        holder.setText(R.id.tv_article_chapterName, chapterName)
        if (item.envelopePic.isNotEmpty()) {
            holder.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.VISIBLE
            ImageLoaderUtil().displayImage(mContext, item.envelopePic, holder.getView(R.id.iv_article_thumbnail))
//            ImageLoader.load(context, item.envelopePic, holder.getView(R.id.iv_article_thumbnail))
        } else {
            holder.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.GONE
        }
        val tv_fresh = holder.getView<TextView>(R.id.tv_article_fresh)
        if (item.fresh) {
            tv_fresh.visibility = View.VISIBLE
        } else {
            tv_fresh.visibility = View.GONE
        }
        val tv_top = holder.getView<TextView>(R.id.tv_article_top)
        if (item.top == "1") {
            tv_top.visibility = View.VISIBLE
        } else {
            tv_top.visibility = View.GONE
        }
        val tv_article_tag = holder.getView<TextView>(R.id.tv_article_tag)
        if (item.tags.size > 0) {
            tv_article_tag.visibility = View.VISIBLE
            tv_article_tag.text = item.tags[0].name
        } else {
            tv_article_tag.visibility = View.GONE
        }
    }
}
