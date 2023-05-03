package com.ggb.nirvanaclub.modules.message

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.*
import com.ggb.nirvanaclub.utils.rxutils.RxQrBarTool
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxfeature.tool.RxQRCode
import com.tencent.open.log.SLog
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_smart_earn.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.litepal.LitePal
import java.io.File

class MessageUserInfoActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM,"扫一扫,添加我为好友")

    override fun getLayoutResource() = R.layout.activity_smart_earn

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(tb_earn_title).init()

        tv_public_title.text = "扫一扫,添加我为好友"
        initAvatar()
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

    private fun initAvatar(){
        val user = LitePal.findLast(UserBean::class.java)
        var bitmap: Bitmap? =null
        ImageLoaderUtil().displayImage(this,user.userImg,iv_user_head)
        tv_user_name.text = user.userName
        tv_user_star.text = user.userLevelName
        val avatarRunnable = Runnable {
            try {
                if (user.userImg.startsWith("http")){
                    bitmap = TencentUtil.getbitmap(user.userImg)
                    Log.e("TAG", "个人名片===Http的头像: $bitmap")
                }else{
                    bitmap = Base64ToBitmapUtil.base64ToBitmap(user.userImg)
                    Log.e("TAG", "个人名片===base64的头像: $bitmap")
                }
//            iv_user_code.setImageBitmap(CodeUtils.createQRCodeBitmap(user.id.toString(), Color.rgb(18,148,248)))

            } catch (e: JSONException) {
                SLog.e("TAG", "个人名片====线程里了获取头像Exception : " + e.message)
            }
        }
        val avatarThread = Thread(avatarRunnable)
        avatarThread.start()

        Handler().postDelayed({
            RxQRCode.builder("Nirvana:"+user.userId)
                .backColor(-0x1)
                .codeColor(-0x1000000)
                .codeSide(600)
                .codeLogo(bitmap)
                .codeBorder(1)
                .into(iv_user_code)
//            iv_user_code.setImageBitmap(CodeUtils.addLogo(CodeUtils.createQRCodeBitmap("Nirvana:"+user.userId, Color.rgb(18,148,248)),bitmap,0.2f))
        },1000)
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