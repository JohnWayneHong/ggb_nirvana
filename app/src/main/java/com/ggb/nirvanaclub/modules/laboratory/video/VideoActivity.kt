package com.ggb.nirvanaclub.modules.laboratory.video

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunityDailyVideoAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.CommunityBasicBean
import com.ggb.nirvanaclub.bean.CommunityDailyDataBean
import com.ggb.nirvanaclub.bean.CommunityDailyIssueBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import com.ggb.nirvanaclub.utils.TimeUtil
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : BaseActivity() , GGBContract.View{

    private lateinit var present: GGBPresent

    private var mAdapter: CommunityDailyVideoAdapter?=null

    private var playUrl = "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=315960&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid="
    private var playTitle = "胶片旅行日记，将生活过成一部电影"

    private lateinit var videoData : CommunityDailyDataBean

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_video

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        videoData = intent.getSerializableExtra("videoData") as CommunityDailyDataBean

        playUrl = videoData.playUrl
        playTitle = videoData.title

        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(mSurfaceContainer).init()

        mAdapter = CommunityDailyVideoAdapter()
        rcv_community_video_recommend.adapter = mAdapter
        rcv_community_video_recommend.layoutManager = LinearLayoutManager(this)

        val empty = View.inflate(this,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        mh_community_video_header.setColorSchemeResources(R.color.main_color)

        addNormalVideoView()
        initVideoDesc(videoData)
        present.communityDailyVideoContent(videoData.id)

    }

    private fun addNormalVideoView() {

        val jzvdStd = JzvdStd(this@VideoActivity).apply {
            setUp(playUrl,playTitle)
            startVideo()
        }
        mSurfaceContainer.addView(
            jzvdStd, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

//        mSurfaceContainer.viewTreeObserver
//            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    mSurfaceContainer.viewTreeObserver.removeOnPreDrawListener(this)
//                    val jzvdStd = JzvdStd(this@VideoActivity).apply {
//                        setUp(playUrl,playTitle)
//                        startVideo()
//                    }
//                    mSurfaceContainer.addView(
//                        jzvdStd, FrameLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT
//                        )
//                    )
//                    return true
//                }
//            })
    }

    @SuppressLint("SetTextI18n")
    private fun initVideoDesc(videoData: CommunityDailyDataBean) {
        ImageLoaderUtil().displayImage(this,videoData.cover.blurred,mVideoBackground)
        swipe_refresh_layout.isVisible = true
        tv_community_video_title.text = videoData.title
        tv_community_video_type.text = videoData.category + "/" + TimeUtil.getStringByFormat(videoData.author.latestReleaseTime.toLong(),
            TimeUtil.dateFormatYMDHM)
        tv_community_video_description.text = videoData.description
        tv_community_video_collectionCount.text = videoData.consumption.collectionCount.toString()
        tv_community_video_shareCount.text = videoData.consumption.shareCount.toString()
        tv_community_video_replyCount.text = videoData.consumption.replyCount.toString()
        ImageLoaderUtil().displayImage(this,videoData.author.icon,iv_community_video_author)
        iv_community_video_author_title.text = videoData.author.name
        iv_community_video_author_description.text = videoData.author.description

    }

    override fun initEvent() {
        mAdapter?.setOnVideoClickListener(object :CommunityDailyVideoAdapter.OnVideoClickListener{
            override fun onVideoClick(data: CommunityDailyDataBean) {
                videoData = data

                addNormalVideoView()
                initVideoDesc(videoData)
                playUrl = videoData.playUrl
                playTitle = videoData.title
                //更新推荐视频
                present.communityDailyVideoContent(data.id)
//                nsv_community_video_title.smoothScrollTo(0,0,1000)
                nsv_community_video_title.fullScroll(View.FOCUS_UP)

            }

        })
        swipe_refresh_layout.setOnRefreshListener {
            present.communityDailyVideoContent(videoData.id)

        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                GGBContract.COMMUNITYDAILYVIDEOCONTENT -> {
                    data?.let {
                        data as CommunityDailyIssueBean

                        val videoList = arrayListOf<CommunityBasicBean>()

                        swipe_refresh_layout.finishRefresh()

                        data.itemList.forEach {
                            if (it.type=="textCard"){
                                videoList.add(CommunityBasicBean(null,3,it.data))
                            }else{
                                videoList.add(CommunityBasicBean(null,2,it.data))
                            }
                        }

                        mAdapter?.setNewData(videoList)

                    }
                }else->{

                }

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }
}