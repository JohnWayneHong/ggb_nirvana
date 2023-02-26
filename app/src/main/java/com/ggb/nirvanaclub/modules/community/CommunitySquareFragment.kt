package com.ggb.nirvanaclub.modules.community


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunityAndroidAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.CommunityAndroidBean
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import kotlinx.android.synthetic.main.fragment_community_android.*

class CommunitySquareFragment :BaseFragment(), GGBContract.View{

    private var present: GGBPresent?=null

    private var pager = 0

    private var mAdapter: CommunityAndroidAdapter?=null

    override fun getLayoutResource() = R.layout.fragment_community_android

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        mAdapter = CommunityAndroidAdapter()
        rcy_community_android.layoutManager = LinearLayoutManager(context)
        rcy_community_android.adapter = mAdapter

        present?.communitySquare(pager)

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        mh_community_android_header.setColorSchemeResources(R.color.main_color)

        initEvent()
    }

    private fun initEvent(){

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            mAdapter?.getItem(position)?.id?.let {
                mAdapter?.getItem(position)?.title?.let { it1 ->
                    mAdapter?.getItem(position)?.link?.let { it2 ->
                        CommunityWebContentActivity.start(activity,
                            it, it1, it2
                        )
                    }
                }
            }
        }

        mAdapter?.setOnLoadMoreListener {
            getNewsList(false)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true)
        }
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
        present?.communitySquare(pager)
    }

    companion object {
        fun newInstance(): CommunitySquareFragment {
            val fragment = CommunitySquareFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.COMMUNITYSQUARE -> {
                    data?.let {
                        data as CommunityAndroidBean
                        if (pager<=0){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter?.setEnableLoadMore(true)
                            mAdapter?.setNewData(data.datas)
                        }else{
                            if (data.datas.isEmpty()){
                                mAdapter?.loadMoreEnd()
                            }else{
                                mAdapter?.addData(data.datas)
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

    override fun onFailed(string: String?, isRefreshList: Boolean) {
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
    }
}