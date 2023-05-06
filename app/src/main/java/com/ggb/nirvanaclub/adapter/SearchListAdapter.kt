package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.IndexTagBean

class SearchListAdapter:BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_search_tag) {

    private var canChange = false

    override fun convert(helper: BaseViewHolder?, item: String) {
        helper?.getView<Button>(R.id.btn_search_item)?.text = item
        helper?.getView<ImageView>(R.id.iv_search_remove)?.visibility = if (canChange) View.VISIBLE else View.GONE

        helper?.addOnClickListener(R.id.btn_search_item)
        helper?.addOnClickListener(R.id.iv_search_remove)
    }

    fun isEnableDelete(canChange:Boolean){
        this.canChange = canChange
        notifyDataSetChanged()
    }
}