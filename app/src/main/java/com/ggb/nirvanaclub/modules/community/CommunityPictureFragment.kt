package com.ggb.nirvanaclub.modules.community

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
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
import com.ggb.nirvanaclub.utils.ScreenShotUtil
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_smart_earn.*
import kotlinx.android.synthetic.main.fragment_community_text.*
import kotlinx.android.synthetic.main.fragment_community_text.swipe_refresh_layout
import org.jetbrains.anko.toast

class CommunityPictureFragment : BaseFragment(), GGBContract.View{

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
        present?.communityPicture()
        initEvent()
        checkDownloadPermission()
    }

    private fun initEvent(){
        mAdapter?.setOnLoadMoreListener {
            swipe_refresh_layout.finishRefresh()
            present?.communityPicture()
        }
        swipe_refresh_layout.setOnRefreshListener {
            isRefresh = true
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
            present?.communityPicture()
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.ll_community_content->{
                    Log.e("TAG", "所选中的新闻图片大小是==========》: "+mAdapter?.data?.get(position)?.joke?.imageSize )
                    Log.e("TAG", "所选中的新闻图片大小是==========》: "+mAdapter?.data?.get(position)?.joke?.imageUrl )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkDownloadPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->

            }
            .onDenied { permissions ->
                context?.toast(R.string.crash_file_permission)?.setGravity(Gravity.CENTER, 0, 0)
            }
            .start()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        fun newInstance(): CommunityPictureFragment {
            val fragment = CommunityPictureFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETJOKESPICTURE -> {
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