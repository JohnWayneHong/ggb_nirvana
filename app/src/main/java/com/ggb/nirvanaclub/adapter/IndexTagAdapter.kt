package com.ggb.nirvanaclub.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexTagBean

class IndexTagAdapter:BaseQuickAdapter<IndexTagBean,BaseViewHolder>(R.layout.item_index_tag) {
    override fun convert(helper: BaseViewHolder?, item: IndexTagBean) {
        helper?.setText(R.id.tv_tag_name,item.tagName)
        helper?.getView<TextView>(R.id.tv_tag_name)?.isSelected = item.isSelected
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