package com.ggb.nirvanaclub.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.DataConfigBean

class ItemSelectAdapter: BaseQuickAdapter<DataConfigBean, BaseViewHolder>(R.layout.item_select_value_view){
    override fun convert(helper: BaseViewHolder?, item: DataConfigBean) {
        helper?.setText(R.id.tv_item_value,if(item.key == 0 && item.value.isNullOrEmpty()) mContext.resources.getString(R.string.all) else item.value)
        helper?.setTextColor(R.id.tv_item_value, mContext.resources.getColor(if(item.key==0) R.color.main_color else R.color.text_main_color))

        if(helper?.adapterPosition == data.size-1){
            helper.getView<View>(R.id.v_sv_line_1).visibility = View.GONE
            helper.getView<View>(R.id.v_sv_line_2).visibility = View.GONE
        }else{
            helper?.getView<View>(R.id.v_sv_line_1)?.visibility = View.VISIBLE
            helper?.getView<View>(R.id.v_sv_line_2)?.visibility = View.VISIBLE
        }
    }
}