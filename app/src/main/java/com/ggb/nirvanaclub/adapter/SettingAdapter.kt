package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.MeOptionBean
import com.ggb.nirvanaclub.bean.SettingBean
import com.lihang.ShadowLayout

class SettingAdapter: BaseQuickAdapter<SettingBean, BaseViewHolder>(R.layout.item_setting) {

    override fun convert(helper: BaseViewHolder?, item: SettingBean) {
        helper?.setText(R.id.tv_settings_title,item.title)

        val contentView = helper?.getView<LinearLayout>(R.id.ll_setting_child)
        contentView?.removeAllViews()
        item.childList.forEach {
            val cView = View.inflate(mContext,R.layout.item_setting_child,null)
            val tChildTitle = cView.findViewById<TextView>(R.id.tv_settings_child_title)
            val tChildSubTitle = cView.findViewById<TextView>(R.id.tv_settings_child_sub_title)
            tChildTitle.text = it.childTitle
            if (it.childSubTitle.isNotEmpty()){
                tChildSubTitle.text = it.childSubTitle
            }
            contentView?.addView(cView)
        }
    }
}