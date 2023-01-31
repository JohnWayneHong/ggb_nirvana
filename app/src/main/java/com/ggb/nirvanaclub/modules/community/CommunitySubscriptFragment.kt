package com.ggb.nirvanaclub.modules.community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunitySubscriptAdapter
import com.ggb.nirvanaclub.adapter.HomeListAdapter
import com.ggb.nirvanaclub.adapter.LaboratoryListAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.DevelopJokesListBean
import com.ggb.nirvanaclub.bean.DevelopJokesSubscriptListBean
import com.ggb.nirvanaclub.bean.LaboratoryListBean
import com.ggb.nirvanaclub.modules.laboratory.captcha.CaptchaActivity
import com.ggb.nirvanaclub.modules.laboratory.ugame.FullscreenActivity
import com.ggb.nirvanaclub.modules.user.NirvanaEarnActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxkit.RxRecyclerViewDividerTool
import kotlinx.android.synthetic.main.fragment_community_subscript.*
import kotlinx.android.synthetic.main.fragment_community_subscript.swipe_refresh_layout
import kotlinx.android.synthetic.main.fragment_community_text.*
import kotlinx.android.synthetic.main.fragment_subscription.*
import kotlinx.android.synthetic.main.item_community_text.view.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.startActivity

class CommunitySubscriptFragment :BaseFragment(), GGBContract.View{

    private var present: GGBPresent?=null

    private var isRefresh:Boolean = false

    private var mAdapter: CommunitySubscriptAdapter?=null
    private var bAdapter: CommunitySubscriptAdapter?=null

    override fun getLayoutResource() = R.layout.fragment_community_subscript

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        mAdapter = CommunitySubscriptAdapter()
        bAdapter = CommunitySubscriptAdapter()
        rcy_scr_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        rcy_scr_list.adapter = mAdapter

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        rcy_scr_content_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rcy_scr_content_list.adapter = bAdapter

        val emptySecond = View.inflate(context,R.layout.item_article_empty_view,null)
        bAdapter?.emptyView = emptySecond

        mh_community_scr_header.setColorSchemeResources(R.color.main_color)
        present?.communityRecommendSubscript()

        initEvent()
    }

    private fun initEvent(){
//        mAdapter?.setOnLoadMoreListener {
//            swipe_refresh_layout.finishRefresh()
//            present?.communityRecommendSubscript()
//        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.tv_community_subscript->{
                    mAdapter?.selectItem(position)
                }
            }
        }
        swipe_refresh_layout.setOnRefreshListener {
            isRefresh = true
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
            rcy_scr_list.smoothScrollToPosition(0)
            present?.communityRecommendSubscript()
        }
    }

    companion object {
        fun newInstance(): CommunitySubscriptFragment {
            val fragment = CommunitySubscriptFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETJOKESRECOMMENDSUBSCRIPT -> {
                    data?.let {
                        data as List<DevelopJokesSubscriptListBean>
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