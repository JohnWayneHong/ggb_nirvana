package com.ggb.nirvanaclub.modules.laboratory.video

import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : BaseActivity() , GGBContract.View{

    private lateinit var present: GGBPresent

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_video

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_laboratory_video).init()

        fl_video.viewTreeObserver.addOnPreDrawListener(object :ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                fl_video.viewTreeObserver.removeOnPreDrawListener(this)
                val jzvdStd = JzvdStd(this@VideoActivity).apply {
                    setUp("http://baobab.kaiyanapp.com/api/v1/playUrl?vid=315960&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=","胶片旅行日记，将生活过成一部电影")
                    startVideo()
                }
                fl_video.addView(
                    jzvdStd,FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                return true
            }

        })
    }

    override fun initEvent() {

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
            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }
}