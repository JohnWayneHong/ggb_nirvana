package com.ggb.nirvanaclub.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.bean.UserInfoFakeBean

class UserInfoAdapter:BaseQuickAdapter<UserInfoFakeBean,BaseViewHolder>(R.layout.item_user_info) {
    override fun convert(helper: BaseViewHolder?, item: UserInfoFakeBean) {
        helper?.setText(R.id.tv_user_info_name,item.tagName)
        helper?.getView<TextView>(R.id.tv_user_info_name)?.isSelected = item.isSelected
    }

    fun selectItem (position:Int){
        if(position == -1){
            data.forEach {
                it.isSelected = false
            }
            notifyDataSetChanged()
        }else{
            data.forEach {
                it.isSelected = false
            }
            data[position].isSelected = true
            notifyDataSetChanged()
        }
    }
}