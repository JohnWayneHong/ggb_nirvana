package com.ggb.nirvanaclub.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.utils.DensityUtils

class SearchGuessAdapter:BaseQuickAdapter<IndexTagBean,BaseViewHolder>(R.layout.item_search_guess) {
    override fun convert(helper: BaseViewHolder?, item: IndexTagBean) {
        helper?.setText(R.id.tv_search_guess_name,item.tagName)
        helper?.getView<TextView>(R.id.tv_search_guess_name)?.setTextColor(DensityUtils.randomColor())
        helper?.addOnClickListener(R.id.tv_search_guess_name)
    }
}