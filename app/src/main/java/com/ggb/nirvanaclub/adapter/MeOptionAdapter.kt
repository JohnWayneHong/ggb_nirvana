package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.MeOptionBean
import com.lihang.ShadowLayout

class MeOptionAdapter: BaseQuickAdapter<MeOptionBean, BaseViewHolder>(R.layout.item_me_option) {

    override fun convert(helper: BaseViewHolder?, item: MeOptionBean) {
        helper?.getView<TextView>(R.id.tv_me_option)?.text = item.title
        if (item.subtitle.isNotEmpty()){
            helper?.getView<TextView>(R.id.tv_me_option_subtitle)?.text = item.subtitle
            helper?.getView<ShadowLayout>(R.id.user_options_subtitle)?.visibility = View.VISIBLE
        }

        helper?.addOnClickListener(R.id.ll_me_option)
    }
}