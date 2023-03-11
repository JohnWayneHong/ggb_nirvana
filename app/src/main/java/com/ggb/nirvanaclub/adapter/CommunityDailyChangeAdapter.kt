package com.ggb.nirvanaclub.adapter

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.utils.*
import com.google.android.material.button.MaterialButton
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

class CommunityDailyChangeAdapter: BaseQuickAdapter<CommunityBasicBean, BaseViewHolder>(R.layout.item_community_daily_video) {

    interface OnVideoClickListener{
        fun onVideoClick(data: CommunityDailyItemBean)
        fun onVideoListClick(data: CommunityDailyDataBean)
    }

    private var mOnVideoClickListener:OnVideoClickListener? = null

    fun setOnVideoClickListener(mOnVideoClickListener:OnVideoClickListener){
        this.mOnVideoClickListener = mOnVideoClickListener
    }


    override fun convert(helper: BaseViewHolder, item: CommunityBasicBean) {
        when (item.itemType) {
            CommunityBasicBean.BANNER -> {
                //轮播图
                helper.getView<Banner>(R.id.ban_community_daily).visibility = View.VISIBLE
                helper.getView<ConstraintLayout>(R.id.cl_community_daily_header).visibility = View.GONE
                helper.getView<TextView>(R.id.tv_community_daily_header).visibility = View.GONE

                val bannerPicList = arrayListOf<String>()
                val bannerTitleList = arrayListOf<String>()

                item.communityDailyBean.issueList[0].itemList.forEach {
                    if (it.type != "banner2"){
                        bannerPicList.add(it.data.cover.detail)
                        bannerTitleList.add(it.data.title)
                    }
                }

                helper.getView<Banner>(R.id.ban_community_daily).setImageLoader(GlideImageLoader())
                helper.getView<Banner>(R.id.ban_community_daily).setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                helper.getView<Banner>(R.id.ban_community_daily).setImages(bannerPicList)
                helper.getView<Banner>(R.id.ban_community_daily).setBannerTitles(bannerTitleList)
                helper.getView<Banner>(R.id.ban_community_daily).start()

                helper.getView<Banner>(R.id.ban_community_daily).setOnBannerListener {

                    mOnVideoClickListener?.onVideoClick(item.communityDailyBean.issueList[0].itemList[it+1])
                    Log.e("TAG", "当前Banner位置是===》$it: ", )
                }

            }
            CommunityBasicBean.VIDEO -> {
                //视频
                helper.getView<Banner>(R.id.ban_community_daily).visibility = View.GONE
                helper.getView<ConstraintLayout>(R.id.cl_community_daily_header).visibility = View.VISIBLE
                helper.getView<TextView>(R.id.tv_community_daily_header).visibility = View.GONE

                if (item.videoBean.dataType!="TextHeader"){
                    helper.getView<Banner>(R.id.ban_community_daily).visibility = View.GONE
                    helper.getView<ConstraintLayout>(R.id.cl_community_daily_header).visibility = View.VISIBLE
                    helper.getView<TextView>(R.id.tv_community_daily_header).visibility = View.GONE

                    ImageLoaderUtil().displayImage(mContext,item.videoBean.cover.detail , helper.getView(R.id.iv_cover))
                    helper.getView<MaterialButton>(R.id.mb_community_daily_type).text = item.videoBean.category
                    helper.getView<MaterialButton>(R.id.mb_community_daily_time).text = TimeUtil.getStringByFormat(item.videoBean.duration*1000.toLong(),
                        TimeUtil.dateFormatMS)
                    ImageLoaderUtil().displayImage(mContext,item.videoBean.author.icon , helper.getView(R.id.iv_community_daily_author))
                    helper.getView<AppCompatTextView>(R.id.tv_community_daily_title).text = item.videoBean.title
                    helper.getView<AppCompatTextView>(R.id.tv_community_daily_content).text = item.videoBean.author.name
                }else{
                    helper.getView<Banner>(R.id.ban_community_daily).visibility = View.GONE
                    helper.getView<ConstraintLayout>(R.id.cl_community_daily_header).visibility = View.GONE
                    helper.getView<TextView>(R.id.tv_community_daily_header).visibility = View.VISIBLE

                    helper.getView<TextView>(R.id.tv_community_daily_header).text = item.videoBean.text
                }

                helper.getView<ConstraintLayout>(R.id.cl_community_daily_header).setOnClickListener {
                    mOnVideoClickListener?.onVideoListClick(item.videoBean)
                }
            }

        }
    }

}