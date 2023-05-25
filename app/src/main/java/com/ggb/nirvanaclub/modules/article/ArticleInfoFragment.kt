package com.ggb.nirvanaclub.modules.article

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.IndexArticleInfoPagingAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.ArticleContentBean
import com.ggb.nirvanaclub.bean.IndexArticleInfoBean
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.ScreenUtils
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.view.CircleImageView
import kotlinx.android.synthetic.main.fragment_article_info.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import per.goweii.anylayer.Layer
import per.goweii.anylayer.guide.GuideLayer
import per.goweii.anylayer.notification.NotificationLayer


class ArticleInfoFragment :BaseFragment(),GGBContract.View{

    private var present:GGBPresent?=null

    private var tagId:String = ""
    private var mAdapter :IndexArticleInfoPagingAdapter?=null

    private var pager = 0

    override fun getLayoutResource() = R.layout.fragment_article_info

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    @SuppressLint("ResourceType")
    override fun initView() {
        tagId = arguments?.getString("tagId","").toString()
        Log.e("TAG", "当前标签页的tagId为-------》$tagId " )
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
            activity?.startActivity<ArticleActivity>(Pair("articleId",mAdapter?.getItem(position)?.id))
        }
        mAdapter?.setOnLoadMoreListener {
            getNewsList(false)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true)
        }
        floating_action_btn.setOnClickListener {
            rcy_article_info_rv.run {
                if (LinearLayoutManager(activity).findFirstVisibleItemPosition() > 20) {
                    scrollToPosition(0)
                } else {
                    smoothScrollToPosition(0)
                }
            }
        }
        rcy_article_info_rv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offsetY = recyclerView.computeVerticalScrollOffset()

//                Log.e("当前坐标=============》", "dx: " + dx + ",  dy: " + dy+ ",  offsetY: " + offsetY)

                val al = offsetY / 600f * 0xff
                if (dy <= 0) {
                    if (floating_action_btn.visibility !== View.VISIBLE) {
                        floating_action_btn.visibility = View.VISIBLE
                    }
                } else {
                    if (floating_action_btn.visibility !== View.GONE) {
                        floating_action_btn.visibility = View.GONE
                    }
                }

            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    private fun getNewsList(isRefreshList: Boolean){
        if(isRefreshList){
            pager = 0
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
                        data as List<IndexArticleInfoBean>
                        if (pager<=0){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter?.setEnableLoadMore(true)
                            mAdapter?.setNewData(data)
                        }else{
                            if (data.isNullOrEmpty()){
                                mAdapter?.loadMoreEnd()
                            }else{
                                mAdapter?.addData(data)
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
            if(pager<=0){
                swipe_refresh_layout.finishRefresh()
                mAdapter?.setEnableLoadMore(true)
            }else{
                mAdapter?.loadMoreFail()
            }
        }
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            if(pager<=0){
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