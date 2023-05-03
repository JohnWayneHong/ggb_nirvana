package com.ggb.nirvanaclub.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.SearchArticleBean
import com.ggb.nirvanaclub.bean.SearchArticleListBean

class SearchArticleListAdapter: BaseQuickAdapter<SearchArticleBean, BaseViewHolder>(R.layout.item_search_list){
    override fun convert(helper: BaseViewHolder?, item: SearchArticleBean?) {
        helper?.setText(R.id.tv_article_title,item?.title)
        helper?.setText(R.id.tv_article_count,item?.introduction)
    }
}