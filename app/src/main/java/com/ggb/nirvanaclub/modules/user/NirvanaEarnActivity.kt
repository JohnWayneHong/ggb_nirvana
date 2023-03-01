package com.ggb.nirvanaclub.modules.user

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.view.View
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.CodeUtils
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import com.ggb.nirvanaclub.utils.ScreenShotUtil
import com.gyf.immersionbar.ImmersionBar
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_smart_earn.*
import org.jetbrains.anko.toast
import org.litepal.LitePal

class NirvanaEarnActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM,"牛蛙呐社区")

    override fun getLayoutResource() = R.layout.activity_smart_earn

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(tb_earn_title).init()

        val user = LitePal.findLast(UserBean::class.java)

        ImageLoaderUtil().displayImage(this,user.userImg,iv_user_head)
        tv_user_name.text = user.userName
        tv_user_star.text = user.userLevelName
        if(C.USER_DOWNLOAD_URL.isNotEmpty()){
            iv_user_code.setImageBitmap(CodeUtils.createQRCodeBitmap(C.USER_DOWNLOAD_URL, Color.rgb(18,148,248)))
        }

        ll_public_back.setOnClickListener {
            finish()
        }

        bt_save.setOnClickListener {
            bt_save.visibility = View.INVISIBLE
            checkDownloadPermission()
        }

    }

    override fun initEvent() {

    }

    @SuppressLint("MissingPermission")
    private fun checkDownloadPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->
                val saveRunnable = Runnable {
                    val isSuccess = ScreenShotUtil.saveScreenshotFromView(ll_earn_view,this)

                    val msg = Message()
                    msg.what = if(isSuccess)1 else 0
                    mHandler.sendMessage(msg)
                }

                val saveThread = Thread(saveRunnable)
                saveThread.start()
            }
            .onDenied { permissions ->

            }
            .start()
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if(msg.what == 1){
                toast("保存成功")
                bt_save.visibility = View.VISIBLE

            }else{
                toast("保存失败")
                bt_save.visibility = View.VISIBLE

            }
        }
    }

}