package com.ggb.nirvanaclub.modules.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ArticleCollectionInfoAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.ArticleCollectionInfoBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.modules.article.ArticleActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.CopyUtils
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_article_info.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class UserCollectionInfoFragment :BaseFragment(),GGBContract.View{

    private var present:GGBPresent?=null

    private var mAdapter : ArticleCollectionInfoAdapter?=null

    private var pager = 0

    override fun getLayoutResource() = R.layout.fragment_article_info

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    @SuppressLint("ResourceType")
    override fun initView() {
        mAdapter = ArticleCollectionInfoAdapter()
        rcy_article_info_rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rcy_article_info_rv.adapter = mAdapter

        val empty = View.inflate(context,R.layout.item_empty_comment_view,null)
        mAdapter?.emptyView = empty

        initEvent()

        mh_article_header.setColorSchemeResources(R.color.main_color)
        present?.pageArticleByCollection(pager,7)
    }

    private fun initEvent() {
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            mAdapter?.closeAll(null)
            when(view.id){
                R.id.tv_collection_copy->{
                    CopyUtils.copyText(C.NIRVANA_WEB_ADDRESS+"blog/"+mAdapter?.getItem(position)?.blog?.id)
                    RxToast.success("复制成功！")
                }
                R.id.tv_collection_open->{
                    if (mAdapter?.getItem(position)?.blog?.id.isNullOrEmpty()){
                        RxToast.error("链接为空")
                        return@setOnItemChildClickListener
                    }
                    if (context !=null){
                        CopyUtils.openBrowser(context, C.NIRVANA_WEB_ADDRESS+"blog/"+mAdapter?.getItem(position)?.blog?.id)
                    }

                }
                R.id.tv_collection_delete->{
                    present?.deleteArticleToCollection(mAdapter?.getItem(position)?.blog?.id.toString())
                }
                R.id.ll_collection_content->{
                    activity?.startActivity<ArticleActivity>(Pair("articleId",mAdapter?.getItem(position)?.blog?.id))
                }
            }
        }
        mAdapter?.setOnLoadMoreListener {
            getNewsList(false)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true)
        }
        //暂时关闭悬浮按钮
        floating_action_btn.visibility = View.GONE
//        floating_action_btn.setOnClickListener {
//            rcy_article_info_rv.run {
//                if (LinearLayoutManager(activity).findFirstVisibleItemPosition() > 20) {
//                    scrollToPosition(0)
//                } else {
//                    smoothScrollToPosition(0)
//                }
//            }
//        }
//        rcy_article_info_rv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val offsetY = recyclerView.computeVerticalScrollOffset()
//                val al = offsetY / 600f * 0xff
//                if (dy <= 0) {
//                    if (floating_action_btn.visibility !== View.VISIBLE) {
//                        floating_action_btn.visibility = View.VISIBLE
//                    }
//                } else {
//                    if (floating_action_btn.visibility !== View.GONE) {
//                        floating_action_btn.visibility = View.GONE
//                    }
//                }
//
//            }
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//        })

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
        present?.pageArticleByCollection(pager,7)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.ARTICLECOLLECTIONINFO -> {
                    data?.let {
                        data as List<ArticleCollectionInfoBean>
                        val filterData = data.filter {
                            it.blog !=null
                        }
                        if (pager<=0){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter?.setEnableLoadMore(true)
                            mAdapter?.setNewData(filterData)
                        }else{
                            if (data.isNullOrEmpty()){
                                mAdapter?.loadMoreEnd()
                            }else{
                                mAdapter?.addData(filterData)
                                mAdapter?.loadMoreComplete()
                            }
                        }
                    }
                }
                GGBContract.DELETEARTICLETOCOLLECTION -> {
                    RxToast.success("取消收藏!")
                    getNewsList(true)
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

    override fun refreshData() {
        super.refreshData()
        mAdapter?.closeAll(null)
    }

    companion object {
        fun newInstance(): UserCollectionInfoFragment {
            val fragment = UserCollectionInfoFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}