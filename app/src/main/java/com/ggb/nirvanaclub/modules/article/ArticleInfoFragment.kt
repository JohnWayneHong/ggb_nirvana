package com.ggb.nirvanaclub.modules.article

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.IndexArticleInfoPagingAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.IndexArticleInfoBean
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import kotlinx.android.synthetic.main.fragment_article_info.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ArticleInfoFragment :BaseFragment(),GGBContract.View{

    private var present:GGBPresent?=null

    private var tagId:String = ""
    private var mAdapter :IndexArticleInfoPagingAdapter?=null

    private var pager = 1

    override fun getLayoutResource() = R.layout.fragment_article_info

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    @SuppressLint("ResourceType")
    override fun initView() {
        tagId = arguments?.getString("tagId","").toString()
        Log.e("TAG", "当前标签页的tagId为-------》$tagId: " )
        mAdapter = IndexArticleInfoPagingAdapter()
        rcy_article_info_rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rcy_article_info_rv.adapter = mAdapter

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        initEvent()

        mh_article_header.setColorSchemeResources(R.color.main_color)
        present?.pageArticleByTag(tagId,pager,7)
    }

    private fun initEvent() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            Log.e("TAG", "点击了: "+mAdapter?.getItem(position)?.articleId )
            activity?.startActivity<ArticleActivity>(Pair("articleId",mAdapter?.getItem(position)?.articleId))
        }
        mAdapter?.setOnLoadMoreListener {
            getNewsList(false,false)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true,false)
        }
    }

    private fun getNewsList(isRefreshList: Boolean,isShow:Boolean){
        if(isRefreshList){
            pager = 1
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present?.pageArticleByTag(tagId,pager,7)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.ARTICLEINFO -> {
                    data?.let {
                        data as IndexArticleInfoBean
                        if (pager<=1){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter?.setEnableLoadMore(true)
                            mAdapter?.setNewData(data.list)
                        }else{
                            if (data.list.isNullOrEmpty()){
                                mAdapter?.loadMoreEnd()
                            }else{
                                mAdapter?.addData(data.list)
                                mAdapter?.loadMoreComplete()
                            }
                        }
                    }
                }
                else -> {

                }

            }
        }
    }

    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)
        if(isRefreshList){
            if(pager<=1){
                swipe_refresh_layout.finishRefresh()
                mAdapter?.setEnableLoadMore(true)
            }else{
                mAdapter?.loadMoreFail()
            }
        }
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            if(pager<=1){
                mAdapter?.data?.clear()
                mAdapter?.notifyDataSetChanged()
                swipe_refresh_layout.finishRefresh()
                mAdapter?.setEnableLoadMore(true)
            }else{
                mAdapter?.loadMoreFail()
            }
        }else{
            if(!boolean){
//                activity?.toast("请检查网络连接")?.setGravity(Gravity.CENTER, 0, 0)
                mAdapter?.loadMoreFail()
            }
        }
    }

    companion object {
        fun newInstance(tagId:String): ArticleInfoFragment {
            val fragment = ArticleInfoFragment()
            val bundle = Bundle()
            bundle.putString("tagId",tagId)
            fragment.arguments = bundle
            return fragment
        }
    }
}