package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.modules.tag.IndexTagSettingActivity

class IndexTagSettingChoseAdapter(itemTouchHelper:ItemTouchHelper):BaseQuickAdapter<IndexTagBean,BaseViewHolder>(R.layout.item_index_setting_chose_tag){

    private var ss = itemTouchHelper
    private var canChange = false

    override fun convert(helper: BaseViewHolder?, item: IndexTagBean) {
        if (item.tagId=="-1"||item.tagId=="-2"){
            helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.text = item.tagName
            helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.setTextColor(mContext.resources.getColor(R.color.gray_text_color))
            helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.setOnLongClickListener {
                false
            }
            helper?.getView<ImageView>(R.id.iv_index_tag_setting_item_remove)?.visibility = View.GONE
        }else{
            helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.text = item.tagName
            helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.setTextColor(mContext.resources.getColor(R.color.text_main_color))
            helper?.getView<Button>(R.id.btn_index_tag_setting_item)?.setOnLongClickListener {
                ss.startDrag(helper)
                true
            }
            helper?.getView<ImageView>(R.id.iv_index_tag_setting_item_remove)?.visibility = if (canChange) View.VISIBLE else View.GONE
        }

        helper?.addOnClickListener(R.id.iv_index_tag_setting_item_remove)
    }
    fun isEnableChange(canChange:Boolean){
        this.canChange = canChange
        notifyDataSetChanged()
    }
    fun getEnableChange():Boolean{
        return canChange
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