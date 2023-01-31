package com.ggb.nirvanaclub

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.AppUpdateBean
import com.ggb.nirvanaclub.bean.AppUpdateListBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.StepRefreshEvent
import com.ggb.nirvanaclub.listener.BaseUiListener
import com.ggb.nirvanaclub.modules.*
import com.ggb.nirvanaclub.service.StepCalculationService
import com.ggb.nirvanaclub.utils.*
import com.ggb.nirvanaclub.view.RxToast
import com.ggb.nirvanaclub.view.dialog.ApkUpdateDialog
import com.ggb.nirvanaclub.view.dialog.OpenAuthorityDialog
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_subscription.*
import kotlinx.android.synthetic.main.home_bottom_tab_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import java.io.File

class MainActivity: BaseActivity() ,ConfigDownloadUtils.OnConfigDownloadCompleteListener, APKRefreshDownload.OnDownLoadCompleteListener{

    private lateinit var  iu: BaseUiListener

    private var lastBackTimeMillis = 0L

    private var isMustUpdate = false
    private var isUpdateComplete = false
    private var isUpdateChecked = false

    private var fragments = arrayListOf<BaseFragment>()

    private var updateDialog : ApkUpdateDialog? = null
    private var authorityDialog : OpenAuthorityDialog? = null

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_main

    override fun beForSetContentView() {

    }

    override fun initView() {
        checkUpdate()
//        initWriteStorage()
//        initFragment()
        setCrashStorage()
        EventBus.getDefault().register(this)

        iu = BaseUiListener(App.mTencent)
        //初始化配置
        Tencent.setIsPermissionGranted(true, Build.MODEL)
        Tencent.resetTargetAppInfoCache()
        Tencent.resetQQAppInfoCache()
        Tencent.resetTimAppInfoCache()

        //开启后台定位
//        val intent = Intent(this, StepCalculationService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent)
//        } else {
//            startService(intent)
//        }

        updateDialog = ApkUpdateDialog(this)
        authorityDialog = OpenAuthorityDialog(this)
    }

    private fun initBlack(){
        val decorView = window.decorView
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0F)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        decorView.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
        Log.e("Update", "缅怀设置完毕: " )
    }

    override fun initEvent() {
        updateDialog?.setOnApkDownloadConfirmListener(object :ApkUpdateDialog.OnApkDownloadConfirmListener{
            override fun onConfirmDownload(info: AppUpdateBean) {
                checkUpdatePermission(info.data)
            }
        })
        updateDialog?.setOnDismissListener {
            if(isMustUpdate&&!isUpdateComplete){
                finish()
            }else if (!isMustUpdate){
                initFragment()
            }
        }
        authorityDialog?.setOnApkDownloadConfirmListener(object :OpenAuthorityDialog.OnOpenAuthorityConfirmListener{
            override fun onConfirmDownload() {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, 1)
            }
        })
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(IndexFragment.newInstance())
        fragments.add(CommunityFragment.newInstance())
        fragments.add(SubscriptionFragment.newInstance())
        fragments.add(MessageFragment.newInstance())
        fragments.add(MeFragment.newInstance())

        vp_home.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }

        vp_home.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
//                if(position==0&& C.USER_ID.isNotEmpty()){
//                    present.getSignResult(C.USER_ID,false)
//                }
//                if(position==1&&C.USER_ID.isNotEmpty()&&!isLoadMarket){
//                    isLoadMarket = true
//                    present.getTransactionProcess(C.USER_ID)
//                }
                if(!fragments[position].isLoadComplete){
                    fragments[position].refreshData()
                }
            }
        })

        vp_home.offscreenPageLimit = 4
        ll_home.setOnClickListener {
            select(0)
        }
        ll_market.setOnClickListener {
            select(1)
        }
        ll_circle.setOnClickListener {
            select(2)
        }
        ll_shop.setOnClickListener {
            select(3)
        }
        ll_mine.setOnClickListener {
            select(4)
        }
        select(0)

    }

    private fun select(position:Int) {
        when (position) {
            0 -> {
                img_home.isSelected = true
                img_market.isSelected = false
                img_circle.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = true
                tv_market.isSelected = false
                tv_circle.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(0, false)
            }
            1 -> {
                img_home.isSelected = false
                img_market.isSelected = true
                img_circle.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_market.isSelected = true
                tv_circle.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(1, false)
            }
            2 -> {
                img_home.isSelected = false
                img_market.isSelected = false
                img_circle.isSelected = true
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_market.isSelected = false
                tv_circle.isSelected = true
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(2, false)
            }
            3 -> {
                img_home.isSelected = false
                img_market.isSelected = false
                img_circle.isSelected = false
                img_shop.isSelected = true
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_market.isSelected = false
                tv_circle.isSelected = false
                tv_shop.isSelected = true
                tv_mine.isSelected = false
                vp_home.setCurrentItem(3, false)
            }
            4 -> {
                img_home.isSelected = false
                img_market.isSelected = false
                img_circle.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = true
                tv_home.isSelected = false
                tv_market.isSelected = false
                tv_circle.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = true
                vp_home.setCurrentItem(4, false)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun shakeStepRefresh(event: StepRefreshEvent){
//        (fragments[1] as CommunityFragment).initStep(event.step)
    }

    /**
     * 检查更新
     */
    private fun checkUpdate(){
        if (!isUpdateChecked){
            ConfigDownloadUtils.configDownload(C.ANDROID_UPDATE_CONFIG_ADDRESS,this,this)
        }
    }

    private fun initWriteStorage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }
    }

    /**
     * 打开存储权限 避免应用crash无法生成日志
     */
    @SuppressLint("MissingPermission")
    private fun setCrashStorage(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->

            }
            .onDenied { permissions ->
                toast(R.string.crash_file_permission).setGravity(Gravity.CENTER, 0, 0)
                finish()
            }
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //腾讯QQ回调
//        Tencent.onActivityResultData(requestCode, resultCode, data,iu)
        if (requestCode == Constants.REQUEST_API) {
            Tencent.onActivityResultData(requestCode, resultCode, data,iu)
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, iu)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkUpdatePermission(apkBean: AppUpdateListBean){
        val updateUrl = apkBean.downloadUrl
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val haveInstallPermission = packageManager.canRequestPackageInstalls()
                    if(haveInstallPermission){
                        APKRefreshDownload(this).startDownload(updateUrl,this)
                    }else{
                        authorityDialog?.showOpenAuthority()
                    }
                }else{
                    APKRefreshDownload(this).startDownload(updateUrl,this)
                }
            }
            .onDenied { permissions ->
                toast(R.string.update_error_1)
            }
            .start()
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.READ_PHONE_STATE)
            .onGranted { permissions ->
//                login(0)
            }
            .onDenied { permissions ->

            }
            .start()
    }

    private fun openAPK() {
        val file = File(APKRefreshDownload.getSaveFilePath())
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this, "com.tencent.login.fileprovider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            data = Uri.fromFile(file)
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive")
        startActivity(intent)
        finish()
    }

    override fun onComplete(isComplete: Boolean) {
        isUpdateComplete = isComplete
        if(isComplete){
            openAPK()
        }else{
            toast(R.string.download_fail)
        }
    }

    override fun onConfigComplete(result: AppUpdateBean?) {
        if(result!=null&&result.data.versionCode.toInt()>0){
            val vn = AppUtils.getVersionName(this)
            val vc = AppUtils.getAppVersionCode(this)
            C.USER_DOWNLOAD_URL = result.data.downloadUrl
            C.IS_MUST_UPDATE = result.data.isForce
            if (C.IS_MUST_UPDATE){
                initBlack()
            }
            Log.e("Update", "当前版本是否强制更新（黑白模式暂时和这个参数一致）-----------》: "+result.data.isForce)
            Log.e("Update", "当前版本-----------》: "+vc)
            Log.e("Update", "当前服务器最新版本-----------》: "+result.data.versionCode)
            if(vc!=0L&&result.data.versionCode.toInt()>vc){
                isMustUpdate = result.data.isForce == true
                isUpdateComplete = false
                updateDialog?.showUpdate(result)
                return
            }else{
                initFragment()
            }
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                checkPermission()
            }
            isUpdateChecked = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateDialog?.dismiss()
        authorityDialog?.dismiss()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 回退事件处理，根据各自 Fragment 的需求处理回退事件
     */
    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastBackTimeMillis > 2500L) {
            lastBackTimeMillis = currentTimeMillis
            RxToast.info(this,"再按一次退出程序", Toast.LENGTH_SHORT, true)?.show()
//            toast("再按一次退出程序")
        } else {
            super.onBackPressed()
        }
    }

}