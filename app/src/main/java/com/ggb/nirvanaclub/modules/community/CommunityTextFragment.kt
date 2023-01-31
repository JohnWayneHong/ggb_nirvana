package com.ggb.nirvanaclub.modules.community

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunityTextAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.DevelopJokesListBean
import com.ggb.nirvanaclub.bean.IndexArticleInfoBean
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.GlideImageLoader
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_community_text.*
import kotlinx.android.synthetic.main.fragment_community_text.swipe_refresh_layout

class CommunityTextFragment : BaseFragment(), GGBContract.View{

    private var present:GGBPresent?=null

    private var isRefresh:Boolean = false

    private var mAdapter : CommunityTextAdapter?=null

    override fun getLayoutResource() = R.layout.fragment_community_text

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        mAdapter = CommunityTextAdapter()
        rcy_community_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rcy_community_list.adapter = mAdapter

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        mh_community_text_header.setColorSchemeResources(R.color.main_color)
        present?.communityText()
        initEvent()
    }

    private fun initEvent(){
        mAdapter?.setOnLoadMoreListener {
            swipe_refresh_layout.finishRefresh()
            present?.communityText()
        }
        swipe_refresh_layout.setOnRefreshListener {
            isRefresh = true
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
            present?.communityText()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        fun newInstance(): CommunityTextFragment {
            val fragment = CommunityTextFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETJOKESTEXT -> {
                    data?.let {
                        data as List<DevelopJokesListBean>
                        if (isRefresh){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter?.setEnableLoadMore(true)
                            mAdapter?.setNewData(data)
                            isRefresh = false
                        }else if (data.isEmpty()){
                            mAdapter?.loadMoreEnd()
                        }else{
                            mAdapter?.addData(data)
                            mAdapter?.loadMoreComplete()
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