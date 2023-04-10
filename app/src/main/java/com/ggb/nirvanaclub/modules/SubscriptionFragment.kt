package com.ggb.nirvanaclub.modules

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.HomeListAdapter
import com.ggb.nirvanaclub.adapter.LaboratoryListAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.LaboratoryListBean
import com.ggb.nirvanaclub.modules.laboratory.captcha.CaptchaActivity
import com.ggb.nirvanaclub.modules.laboratory.ugame.FullscreenActivity
import com.ggb.nirvanaclub.modules.laboratory.video.VideoActivity
import com.ggb.nirvanaclub.modules.user.NirvanaEarnActivity
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxkit.RxRecyclerViewDividerTool
import kotlinx.android.synthetic.main.fragment_subscription.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.startActivity

/**
 * 改造成实验室，未上架的功能
 */
class SubscriptionFragment :BaseFragment(){

    private var mAdapter: LaboratoryListAdapter?=null

    override fun getLayoutResource() = R.layout.fragment_subscription

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_laboratory).init()

        mAdapter = LaboratoryListAdapter()
        rcy_laboratory.layoutManager = GridLayoutManager(context,2)
        rcy_laboratory.adapter = mAdapter

        rcy_laboratory.addItemDecoration(RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)))


        val laboratoryList = arrayListOf<LaboratoryListBean>()
        laboratoryList.add(LaboratoryListBean("牛蛙呐游戏",R.mipmap.ic_bear_question))
        laboratoryList.add(LaboratoryListBean("牛蛙呐二楼",R.mipmap.ic_bear_question))
        laboratoryList.add(LaboratoryListBean("牛蛙呐WebView",R.mipmap.ic_bear_question))
        laboratoryList.add(LaboratoryListBean("牛蛙呐牛逼验证码",R.mipmap.ic_bear_question))
        laboratoryList.add(LaboratoryListBean("牛蛙呐健康管理",R.mipmap.ic_bear_question))
        laboratoryList.add(LaboratoryListBean("牛蛙呐视频",R.mipmap.ic_bear_question))
        laboratoryList.add(LaboratoryListBean("尚未开发",R.mipmap.ic_bear_question))
        mAdapter?.setNewData(laboratoryList)

        initEvent()
    }

    private fun initEvent(){
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (mAdapter?.data?.get(position)?.name=="牛蛙呐游戏"){
                activity?.startActivity<FullscreenActivity>()
            }
            if (mAdapter?.data?.get(position)?.name=="牛蛙呐牛逼验证码"){
                activity?.startActivity<CaptchaActivity>()
            }
            if (mAdapter?.data?.get(position)?.name=="牛蛙呐健康管理"){
                //TODO 健康管理弹窗
                activity?.startActivity<CaptchaActivity>()
            }
            if (mAdapter?.data?.get(position)?.name=="牛蛙呐视频"){
//                activity?.startActivity<VideoActivity>()
            }
        }

//        refreshLayout.setOnMultiListener(object : SimpleMultiListener() {
//            override fun onLoadMore(refreshLayout: RefreshLayout) {
//                refreshLayout.finishLoadMore(2000)
//            }
//
//            override fun onRefresh(refreshLayout: RefreshLayout) {
//                Toast.makeText(context, "下拉刷新", Toast.LENGTH_SHORT).show()
//                refreshLayout.finishRefresh(2000)
//            }
//
//            override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
//                if (oldState == RefreshState.TwoLevel) {
//                    second_floor_content.animate().alpha(0F).duration = 1000
//                }
//            }
//        })
//        header.setOnTwoLevelListener {
//            Toast.makeText(context, "打开二楼", Toast.LENGTH_SHORT).show()
//            second_floor_content.animate().alpha(1F).duration = 2000
//            true
//        }
//        iv_second_back.setOnClickListener {
//            header.finishTwoLevel()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


    companion object {
        fun newInstance(): SubscriptionFragment {
            val fragment = SubscriptionFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}