package com.ggb.nirvanaclub.modules.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunityDailyChangeAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.modules.laboratory.video.VideoActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_community_daily.*

class CommunityDailyFragment :BaseFragment(),GGBContract.View{

    private var present: GGBPresent?=null

    private var nextPageUrl = ""

    private var bAdapter: CommunityDailyChangeAdapter?=null

    override fun getLayoutResource() = R.layout.fragment_community_daily

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        bAdapter = CommunityDailyChangeAdapter()
        rcy_community_daily.adapter = bAdapter
        rcy_community_daily.layoutManager = LinearLayoutManager(context)

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        bAdapter?.emptyView = empty

        mh_community_android_header.setColorSchemeResources(R.color.main_color)
        initEvent()

        present?.communityDailyBanner()
    }

    private fun initEvent(){

        bAdapter?.setOnVideoClickListener(object :CommunityDailyChangeAdapter.OnVideoClickListener{

            override fun onVideoClick(data: CommunityDailyItemBean) {
                val myIntent = Intent(activity, VideoActivity::class.java)
                myIntent.putExtra("videoData", data.data)
                startActivity(myIntent)
            }

            override fun onVideoListClick(data: CommunityDailyDataBean) {
                val myIntent = Intent(activity, VideoActivity::class.java)
//                myIntent.putExtra("playUrl", data.playUrl)
//                myIntent.putExtra("playTitle", data.title)
                myIntent.putExtra("videoData", data)
                startActivity(myIntent)
            }
        })

        bAdapter?.setOnLoadMoreListener {
            present?.communityDailyVideo(nextPageUrl)

        }

        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true)
        }
    }

    private fun getNewsList(isRefreshList: Boolean){
        if (isRefreshList){
            bAdapter?.loadMoreComplete()
            bAdapter?.setEnableLoadMore(false)
            nextPageUrl = ""
        }else{
            swipe_refresh_layout.finishRefresh()
        }
        present?.communityDailyBanner()
    }
    
    companion object {
        fun newInstance(): CommunityDailyFragment {
            val fragment = CommunityDailyFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.COMMUNITYDAILYBANNER -> {
                    data?.let {
                        data as CommunityDailyBean
                        present?.communityDailyVideo(data.nextPageUrl)

                        swipe_refresh_layout.finishRefresh()
                        bAdapter?.setNewData(arrayListOf(CommunityBasicBean(data,1,null)))

                    }
                }
                GGBContract.COMMUNITYDAILYVIDEO ->{
                    data?.let {
                        data as CommunityDailyBean

                        bAdapter?.setEnableLoadMore(true)
                        bAdapter?.loadMoreComplete()
                        data.issueList[0].itemList.forEach {
                            bAdapter?.addData(arrayListOf(CommunityBasicBean(data,2,it.data)))
                        }
                        bAdapter?.notifyDataSetChanged()

                        if (!data.nextPageUrl.isNullOrEmpty()){
                            nextPageUrl = data.nextPageUrl
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