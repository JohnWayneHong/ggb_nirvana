package com.ggb.nirvanaclub.base

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.ContactNotifyEvent
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.GuideActivity
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.AppStatus
import com.ggb.nirvanaclub.utils.AppStatusManager
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.title_public_view.*

abstract class BaseActivity: MyBaseActivity() {

    protected var activityStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()

        activityStatus = C.ACTIVITY_RECYCLE_CREATE
        App.instance.addActivity(this)
        if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYCLE) {
            //被回收，跳转到启动页面
            if(this is GuideActivity){
                AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL)
            }else{
                App.instance.clearActivityList()
                val intent = Intent(this, GuideActivity::class.java)
                startActivity(intent)
                return
            }
        }
        initView()
        initEvent()
        initTitle()
//        initBlack()
        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this)
    }

    private fun initTitle(){
        val d = getTitleType()
        if(d.type == C.TITLE_CUSTOM){

            return
        }
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(public_title).init()
        when(d.type){
            C.TITLE_NORMAL -> {
                tv_public_title.text = d.title
                tv_public_right.visibility = View.GONE
                tv_public_right_background.visibility = View.GONE
                iv_public_right.visibility = View.GONE
            }
            C.TITLE_RIGHT_TEXT -> {
                tv_public_title.text = d.title
                tv_public_right.text = d.right
                tv_public_right.setTextColor(resources.getColor(d.rightTextColor))
                tv_public_right.visibility = View.VISIBLE
                tv_public_right_background.visibility = View.GONE
                iv_public_right.visibility = View.GONE
            }
            C.TITLE_RIGHT_TEXT_BACKGROUND -> {
                tv_public_title.text = d.title
                tv_public_right_background.text = d.right
                tv_public_right.visibility = View.GONE
                tv_public_right_background.visibility = View.VISIBLE
                iv_public_right.visibility = View.GONE
            }
        }
        ll_public_back.setOnClickListener {
            finish()
        }
    }

    private fun initBlack(){
        val decorView = window.decorView
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0F)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        decorView.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
    }

    abstract fun getTitleType():PublicTitleData

    class PublicTitleData{
        var type:Int
        var title:String
        var right:String
        var rightImg:Int
        var rightTextColor:Int

        constructor(type:Int):this(type,"")

        constructor(type:Int,title:String):this(type,title,"")

        constructor(type:Int,title:String,right:String):this(type,title,right,0)

        constructor(type:Int,title:String,right:String,rightImg:Int):this(type,title,right,rightImg, R.color.main_color)

        constructor(type:Int,title:String,right:String,rightImg:Int,rightTextColor:Int){
            this.type = type
            this.title = title
            this.right = right
            this.rightImg = rightImg
            this.rightTextColor = rightTextColor
        }

    }

    override fun onStart() {
        super.onStart()
        activityStatus = C.ACTIVITY_RECYCLE_START
    }

    override fun onResume() {
        super.onResume()
        activityStatus = C.ACTIVITY_RECYCLE_RESUME
    }

    override fun onPause() {
        super.onPause()
        activityStatus = C.ACTIVITY_RECYCLE_PAUSE
    }

    override fun onStop() {
        super.onStop()
        activityStatus = C.ACTIVITY_RECYCLE_STOP
    }

    override fun onDestroy() {
        super.onDestroy()
        activityStatus = C.ACTIVITY_RECYCLE_DESTROY
        loadingDialog?.dismiss()
        //        //注销消息接收
        JMessageClient.unRegisterEventReceiver(this)
    }

    /**
     * 用于WebContent的返回按钮的事件，系统的返回键
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun onEvent(event: ContactNotifyEvent) {
        if (event.type == ContactNotifyEvent.Type.invite_received) {
            var has = false
            for (i in C.friendApply.indices) {
                if (C.friendApply[i].fromUsername == event.fromUsername) {
                    has = true
                }
            }
            if (!has) {
                C.friendApply.add(event)
                RxToast.info(this, "收到了好友邀请！", Toast.LENGTH_SHORT)?.show()
            }
        }
    }

}