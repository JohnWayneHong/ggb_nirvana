package com.ggb.nirvanaclub.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.SearchArticleBean
import com.ggb.nirvanaclub.bean.SearchArticleListBean
import java.math.BigDecimal
import kotlin.random.Random

class SearchArticleListAdapter: BaseQuickAdapter<SearchArticleBean, BaseViewHolder>(R.layout.item_search_list){

    /**
     * 1:默认是搜索发现
     * 2:搜索后的文章显示
     */
    private var type = 1
    override fun convert(helper: BaseViewHolder?, item: SearchArticleBean?) {
        when(type){
            1->{
                helper?.getView<LinearLayout>(R.id.ll_search_content)?.visibility = View.VISIBLE
                helper?.getView<LinearLayout>(R.id.ll_search_discover)?.visibility = View.GONE
                helper?.setText(R.id.tv_article_title,item?.title)
                helper?.setText(R.id.tv_article_count,item?.introduction)
            }
            2->{
                helper?.getView<LinearLayout>(R.id.ll_search_content)?.visibility = View.GONE
                helper?.getView<LinearLayout>(R.id.ll_search_discover)?.visibility = View.VISIBLE
                when(item?.selfLevel){
                    0->{
                        helper?.getView<ImageView>(R.id.iv_search_discover)?.setImageResource(R.mipmap.point_1)
                    }
                    1->{
                        helper?.getView<ImageView>(R.id.iv_search_discover)?.setImageResource(R.mipmap.point_4)
                    }
                    2->{
                        helper?.getView<ImageView>(R.id.iv_search_discover)?.setImageResource(R.mipmap.point_3)
                    }
                    3->{
                        helper?.getView<ImageView>(R.id.iv_search_discover)?.setImageResource(R.mipmap.point_2)
                    }
                }
                helper?.setText(R.id.tv_article_title_discover,item?.title)
                val fakeHotData = BigDecimal(item?.readCount.toString()+"2").multiply(
                    BigDecimal((1..50).random())).toString()+"w"
                helper?.setText(R.id.tv_article_title_hot,fakeHotData)

                when((1..3).random()){
                    1->{
                        helper?.getView<ImageView>(R.id.iv_article_title_status)?.setImageResource(R.mipmap.ic_search_up)
                    }
                    2->{
                        helper?.getView<ImageView>(R.id.iv_article_title_status)?.setImageResource(R.mipmap.ic_search_down)
                    }
                    3->{
                        helper?.getView<ImageView>(R.id.iv_article_title_status)?.setImageResource(R.mipmap.ic_search_dash)
                    }
                }

            }
        }

    }

    fun initDiscoverOrSearch(type:Int){
        this.type = type
    }
}