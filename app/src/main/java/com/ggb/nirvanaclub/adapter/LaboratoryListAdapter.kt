package com.ggb.nirvanaclub.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.LaboratoryListBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil

class LaboratoryListAdapter: BaseQuickAdapter<LaboratoryListBean, BaseViewHolder>(R.layout.item_laboratory_list){
    override fun convert(helper: BaseViewHolder?, item: LaboratoryListBean?) {
        helper?.setText(R.id.tv_laboratory_name,item?.name)
        ImageLoaderUtil().displayImage(mContext,item?.image,helper?.getView(R.id.iv_laboratory_imageView))

    }
}