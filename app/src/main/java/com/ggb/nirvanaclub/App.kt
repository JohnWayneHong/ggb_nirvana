package com.ggb.nirvanaclub

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import cn.jpush.android.api.JPushInterface
import com.didichuxing.doraemonkit.DoraemonKit
import com.ggb.nirvanaclub.app.BaseApplication
import com.ggb.nirvanaclub.config.LogConfigData
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.manager.ServiceTimeManager
import com.ggb.nirvanaclub.utils.CrashHandler
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.utils.rxutils.RxTool
import com.tencent.mmkv.MMKV
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

        LogConfigData.ISDEBUG = isApkInDebug()

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

    private fun isApkInDebug(): Boolean {
        return try {
            val info = this.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            false
        }
    }

}