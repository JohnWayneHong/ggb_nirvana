package com.ggb.nirvanaclub.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.DevelopJokesSubscriptListBean
import com.ggb.nirvanaclub.bean.LaboratoryListBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil

class CommunitySubscriptAdapter: BaseQuickAdapter<DevelopJokesSubscriptListBean, BaseViewHolder>(R.layout.item_community_subscript){
    override fun convert(helper: BaseViewHolder?, item: DevelopJokesSubscriptListBean) {
        ImageLoaderUtil().displayImage(mContext,item.avatar,helper?.getView(R.id.iv_community_subscript_imageView))
        helper?.getView<TextView>(R.id.tv_community_subscript_name)?.text = item.nickname
        helper?.getView<TextView>(R.id.tv_community_subscript_info)?.text = mContext.resources.getString(R.string.develop_community_subscript_fans,item.jokesNum,item.fansNum)
        helper?.getView<TextView>(R.id.tv_community_subscript)?.isSelected = !item.isAttention
        helper?.getView<TextView>(R.id.tv_community_subscript)?.text = if (item.isAttention) "取消关注" else "+关注"

        helper?.addOnClickListener(R.id.tv_community_subscript)
    }

    fun selectItem (position:Int){
        if(position == -1){
            data.forEach {
                it.isAttention = true
            }
            notifyDataSetChanged()
        }else{
            data[position].isAttention = !data[position].isAttention
            notifyDataSetChanged()
        }
    }
}