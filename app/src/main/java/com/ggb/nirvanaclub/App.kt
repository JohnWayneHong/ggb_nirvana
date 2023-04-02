package com.ggb.nirvanaclub

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Process
import android.util.Log
import cn.jpush.android.api.JPushInterface
import cn.jpush.im.android.api.JMessageClient
import com.didichuxing.doraemonkit.DoraemonKit
import com.ggb.nirvanaclub.app.BaseApplication
import com.ggb.nirvanaclub.config.LogConfigData
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.manager.ServiceTimeManager
import com.ggb.nirvanaclub.receiver.NotificationClickEventReceiver
import com.ggb.nirvanaclub.utils.CrashHandler
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.utils.rxutils.RxTool
import com.tencent.mmkv.MMKV
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.tauth.Tencent
import org.litepal.LitePal
import kotlin.properties.Delegates


class App: BaseApplication(){

    private val mActivityList = arrayListOf<Activity>()

    override fun onCreate() {
        super.onCreate()
        init()
        context = applicationContext
        MMKV.initialize(context)
        mTencent = Tencent.createInstance(C.QQ_LOGIN_APPLY_ID, context, "com.tencent.login.fileprovider")

        CrashHandler.getInstance().init(context)
        RxTool.init(this)
        com.tamsiree.rxkit.RxTool.init(this)

        DoraemonKit.install(context.applicationContext as Application, "")

        //腾讯X5接入WebRtc暂时闪退无解
//        initTBS()
    }

    companion object {
        var instance : App by Delegates.notNull()
        /**
         * 用于全局获取上下文
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var  mTencent:Tencent
    }

    private fun init(){
        instance = this
        LitePal.initialize(this)
        LitePal.getDatabase()
        ServiceTimeManager.getInstance().init()

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        SharedPreferencesUtil.putCommonString(this,"RegistrationID", JPushInterface.getRegistrationID(this))

        //JMessage
        JMessageClient.setDebugMode(true)
        //开启消息记录漫游
        JMessageClient.init(this,true)


        //注册Notification点击的接收器
        val notificationClickEventReceiver = NotificationClickEventReceiver(this)


        LogConfigData.ISDEBUG = isApkInDebug()


    }

    private fun initTBS(){
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        // 初始化 腾讯X5n  WebView
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                isX5 = arg0
                Log.e("腾讯X5", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.e("腾讯X5", " onCoreInitFinished")
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb)
    }


    fun addActivity(activity: Activity) {
        mActivityList.add(activity)
    }

    /**
     * 关闭程序所有的Activity
     */
    fun clearActivityList() {
        for (i in mActivityList.indices) {
            val activity = mActivityList[i]
            activity.finish()
        }
        mActivityList.clear()
    }

    /**
     * 退出程序
     */
    fun exitApp() {
        clearActivityList()
        killProcess()
    }

    private fun killProcess() {
        Process.killProcess(Process.myPid())
    }

    private fun isApkInDebug(): Boolean {
        return try {
            val info = this.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            false
        }
    }

}