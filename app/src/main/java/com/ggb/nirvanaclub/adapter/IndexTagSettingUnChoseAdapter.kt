package com.ggb.nirvanaclub.adapter

import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexTagBean

class IndexTagSettingUnChoseAdapter:BaseQuickAdapter<IndexTagBean,BaseViewHolder>(R.layout.item_index_setting_chose_tag) {
    override fun convert(helper: BaseViewHolder?, item: IndexTagBean) {
        helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.text = item.tagName
        helper?.addOnClickListener(R.id.btn_index_tag_setting_item)
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