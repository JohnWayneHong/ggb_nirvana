package com.ggb.nirvanaclub.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
import com.google.android.material.imageview.ShapeableImageView
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

class CommunityDailyVideoAdapter: BaseQuickAdapter<CommunityBasicBean, BaseViewHolder>(R.layout.item_community_daily_video_list) {

    interface OnVideoClickListener{
        fun onVideoClick(data: CommunityDailyDataBean)
    }

    private var mOnVideoClickListener:OnVideoClickListener? = null

    fun setOnVideoClickListener(mOnVideoClickListener:OnVideoClickListener){
        this.mOnVideoClickListener = mOnVideoClickListener
    }


    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: CommunityBasicBean) {
        when (item.itemType) {
            CommunityBasicBean.VIDEO -> {
                //视频
                helper.getView<ConstraintLayout>(R.id.cl_video_content).visibility = View.VISIBLE
                helper.getView<TextView>(R.id.tv_video_top_title).visibility = View.GONE

                ImageLoaderUtil().displayImage(mContext,item.videoBean.cover.feed , helper.getView(R.id.iv_video_cover))
                helper.getView<MaterialButton>(R.id.btn_video_content_length).text = TimeUtil.getStringByFormat(item.videoBean.duration*1000.toLong(),
                    TimeUtil.dateFormatMS)

                helper.getView<TextView>(R.id.tv_video_title).text = item.videoBean.title
                helper.getView<TextView>(R.id.tv_video_category).text = item.videoBean.category + "/" + if (item.videoBean.author==null) "" else item.videoBean.author.name

                helper.getView<ConstraintLayout>(R.id.cl_video_content).setOnClickListener {
                    mOnVideoClickListener?.onVideoClick(item.videoBean)
                }

            }
            CommunityBasicBean.TEXTHEADER -> {
                //类型
                helper.getView<ConstraintLayout>(R.id.cl_video_content).visibility = View.GONE
                helper.getView<TextView>(R.id.tv_video_top_title).visibility = View.VISIBLE

                helper.getView<TextView>(R.id.tv_video_top_title).text = item.videoBean.text

            }

        }
    }

}