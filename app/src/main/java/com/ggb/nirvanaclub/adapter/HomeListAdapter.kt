package com.ggb.nirvanaclub.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.NewsListBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil

class HomeListAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home_list){

    override fun convert(helper: BaseViewHolder?, item: String) {
//        ImageLoaderUtil().displayImage(mContext,item,helper?.getView(R.id.iv_news_img))
        ImageLoaderUtil().displayHugeImage(mContext,item,helper?.getView(R.id.iv_news_img))
    }

}