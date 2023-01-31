package com.ggb.nirvanaclub


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Handler
import android.view.View
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.AppUtils
import com.ggb.nirvanaclub.utils.TimeCountUtil
import kotlinx.android.synthetic.main.activity_guide.*
import org.jetbrains.anko.startActivity


class GuideActivity : BaseActivity(),TimeCountUtil.OnCountDownListener {

    private var time: TimeCountUtil?=null


    private var isJump = false

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_guide

    @SuppressLint("SetTextI18n")
    override fun initView() {
        time = TimeCountUtil(3000, 1000)
        time?.setOnCountDownListener(this)
        time?.start()
        Handler().postDelayed({
//            AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL)
            if (!isJump){
                startActivity<MainActivity>()
                finish()
            }
        },3000)

        tv_guide_version.text = "${AppUtils.getVersionName(this)}.${AppUtils.getAppVersionCode(this)}"

//        initBlack()
    }

    private fun initBlack(){
        val decorView = window.decorView
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0F)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        decorView.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
    }

    override fun initEvent() {
        tv_jump_guide.setOnClickListener {
            isJump = true
            startActivity<MainActivity>()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        time?.cancel()
//        changeIcon("com.ggb.nirvanaclub.MainAliasActivity")
    }

    private fun changeIcon(activityPath: String?) {
        val pm: PackageManager = packageManager
        pm.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        activityPath?.let { ComponentName(this, it) }?.let {
            pm.setComponentEnabledSetting(
                it,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )
        }
        //重启桌面 加速显示
        restartSystemLauncher(pm);
    }

    fun restartSystemLauncher(pm: PackageManager) {
        val am: ActivityManager = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val resolves: List<ResolveInfo> = pm.queryIntentActivities(i, 0)
        for (res in resolves) {
            if (res.activityInfo != null) {
                am.killBackgroundProcesses(res.activityInfo.packageName)
            }
        }
    }

    override fun onTickChange(millisUntilFinished: Long) {
        tv_jump_guide.text = (millisUntilFinished / 1000).toString() + "s"+" | "+"跳过"
//        tv_jump_guide.text = "跳过"+"("+(millisUntilFinished / 1000).toString() + "s"+")"
    }

    override fun OnFinishChanger() {

    }

}
