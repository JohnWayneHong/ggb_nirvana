package com.ggb.nirvanaclub.modules

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.MainActivity
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.MeBannerAdapter
import com.ggb.nirvanaclub.adapter.MeOptionAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.MeOptionBean
import com.ggb.nirvanaclub.bean.QQAvatarBean
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.modules.login.DevelopSettingActivity
import com.ggb.nirvanaclub.modules.login.LoginActivity
import com.ggb.nirvanaclub.modules.login.SettingActivity
import com.ggb.nirvanaclub.modules.scanner.ActivityScannerCode
import com.ggb.nirvanaclub.modules.user.HealthyActivity
import com.ggb.nirvanaclub.modules.user.ShopCartActivity
import com.ggb.nirvanaclub.modules.user.UserInfoActivity
import com.ggb.nirvanaclub.utils.*
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import com.tencent.mmkv.MMKV
import com.tencent.open.log.SLog
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import org.litepal.LitePal
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class MeFragment :BaseFragment(){

    private var aAdapter : MeOptionAdapter?=null
    private var bAdapter : MeBannerAdapter?=null

    private var optionList = arrayListOf(
        MeOptionBean("?????????", "??????????????????"),
        MeOptionBean("????????????", "????????????"),
        MeOptionBean("???????????????", "????????????"),
        MeOptionBean("????????????" ),
        MeOptionBean("????????????" ),
        MeOptionBean("????????????" ),
        MeOptionBean("??????" ),
        MeOptionBean("????????????" ),
        MeOptionBean("???????????????" ),
        MeOptionBean("????????????" ),
        MeOptionBean("???????????????" )

    )

    private val kv = MMKV.defaultMMKV()
    private var loginType = 0

    private var bannerHeight: Int = 0
    // ??? 999999 ???????????????????????? 3 ????????????????????????????????????????????????????????????
    private var curCarouselPosition = 999999
    private var carouselTask: TimerTask? = null
    private val carouselTimer = Timer()
    private val carouselHandler = Handler(Looper.myLooper()!!) {
        vp_me_carousel.setCurrentItem(++curCarouselPosition, true)
        true
    }

    override fun getLayoutResource() = R.layout.fragment_me

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(me_toolbar).init()
        EventBus.getDefault().register(this)

        aAdapter = MeOptionAdapter()
        me_user_options_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        me_user_options_rv.adapter = aAdapter

        bAdapter = MeBannerAdapter()
        vp_me_carousel.adapter = bAdapter

        aAdapter?.setNewData(optionList)

        // ???????????????????????????????????????????????????????????????????????????TitleBar??????????????????1
        bannerHeight = me_banner.layoutParams.height - me_toolbar.height - ImmersionBar.getStatusBarHeight(this)
        initEvent()
        setCarousel()
        initUserBean()
    }

    private fun initEvent(){
        me_scroll_view.apply {
            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                // scrollY ??????????????????
                val alpha = scrollY.toFloat() / bannerHeight
                me_toolbar.alpha = if (alpha > 1) 1f else alpha
            })

        }
        me_login_clickable.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<UserInfoActivity>()
//                "???????????????".showToast()
            }
        }
        aAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.ll_me_option ->{
                    if (aAdapter?.data?.get(position)?.title == "??????"){
                        activity?.startActivity<SettingActivity>()
                    }
                    if (aAdapter?.data?.get(position)?.title == "?????????"){
                        activity?.startActivity<HealthyActivity>()
                    }
                    if (aAdapter?.data?.get(position)?.title == "????????????"){
                        activity?.startActivity<ShopCartActivity>()
                    }
                    if (aAdapter?.data?.get(position)?.title == "????????????"){
                        checkPermission()
                    }
                    if (aAdapter?.data?.get(position)?.title == "???????????????"){
                        activity?.startActivity<DevelopSettingActivity>()
                    }
                    if (position==7){
                        val cacheSize = CacheDataUtil.getTotalCacheSize(requireContext())
                        val rxDialog = RxDialogSureCancel(requireContext())
                        rxDialog.setContent("????????????????????????????????? $cacheSize")
                        rxDialog.sureView.setOnClickListener {
                            CacheDataUtil.clearAllCache(requireContext())
                            RxToast.success(requireContext(),"?????????????????????", Toast.LENGTH_SHORT, true)?.show()
                            rxDialog.cancel() }
                        rxDialog.cancelView.setOnClickListener { rxDialog.cancel() }
                        rxDialog.show()
                    }
                }
            }
        }
        me_scan_code.setOnClickListener {
            checkCameraPermission()
        }

        ll_me.setOnClickListener {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getLogin(event: UserStateChangeEvent){
        when(event.state){
            1->{
                if (!kv.decodeString("qq_info").isNullOrEmpty()){
                    val avatar = kv.decodeString("qq_info")!!
                    Log.e("TAG", "????????????????????????: $avatar")
                    val avatarRunnable = Runnable {
                        val result = JSONObject(avatar)
                        if (result.has("figureurl")) {
                            var bitmap: Bitmap? = null
                            try {
                                bitmap = TencentUtil.getbitmap(result.getString("figureurl_qq_2"))
                                Log.e("TAG", "?????????????????????????????????: $bitmap")
                            } catch (e: JSONException) {
                                SLog.e("TAG", "Util.getBitmap() jsonException : " + e.message)
                            }

                            val msg = Message()
                            msg.what = 1
                            val info = QQAvatarBean(bitmap,result.getString("nickname"))
                            msg.obj = info
                            mHandler.sendMessage(msg)
                        }
                    }
                    val avatarThread = Thread(avatarRunnable)
                    avatarThread.start()
                    loginType = 1
                    C.IS_LOGIN = true
                }
            }
            2->{
                if (loginType==1){
                    App.mTencent.logout(activity)
                    kv.remove("qq_login")
                }
                "??????????????????".showToast()

                C.USER_ID = ""
                C.IS_LOGIN = false
                LitePal.deleteAll(UserBean::class.java)
                iv_me_user_avatar.setImageDrawable(resources.getDrawable(R.drawable.ic_login_img))
                tv_me_user_nickname.text = resources.getString(R.string.me_login_click_string)

                //JMessage????????????
                JMessageClient.logout()
                SharedPreferencesUtil.clearUserData(context)
                App.context.getSharedPreferences("cookies",Context.MODE_PRIVATE).all.forEach {
                    App.context.getSharedPreferences("cookies", Context.MODE_PRIVATE).edit().apply {
                        remove(it.key)
                        apply()
                    }
                }

            }
            3 -> {
                val user = LitePal.findLast(UserBean::class.java)
                tv_me_user_nickname.text = user.userName
//                iv_me_user_avatar.setImageBitmap(Base64ToBitmapUtil.base64ToBitmap(user.userImg))
                ImageLoaderUtil().displayHeadImage(context, user.userImg, iv_me_user_avatar)

                C.USER_ID = user.userId
                C.IS_LOGIN = user.isLogin
                loginType = 3

                //JMessage?????????????????????????????????
                val avatarRunnable = Runnable {
                    val bitmap: Bitmap?
                    try {
                        if (user.userImg.startsWith("http")){
                            bitmap = TencentUtil.getbitmap(user.userImg)
                            Log.e("TAG", "JMessage====????????????????????????===Http?????????: $bitmap")
                        }else{
                            bitmap = Base64ToBitmapUtil.base64ToBitmap(user.userImg)
                            Log.e("TAG", "JMessage====????????????????????????===base64?????????: $bitmap")
                        }
                        BitmapToFile.saveFile(bitmap, user.userId)
                    } catch (e: JSONException) {
                        SLog.e("TAG", "JMessage====????????????????????????Exception : " + e.message)
                    }
                }
                val avatarThread = Thread(avatarRunnable)
                avatarThread.start()

                //??????????????????
                val PHOTO_PATH = "/sdcard/Android/data/com.ggb.nirvanaclub" + "/picture/" + user.userId

                Handler().postDelayed({
                    JMessageClient.updateUserAvatar(File(PHOTO_PATH), object : BasicCallback() {
                        override fun gotResult(p0: Int, p1: String?) {
                            if (p0 == 0) {
                                Log.e("TAG", "JMessage===??????????????????=====???: ")
                            } else {
                                Log.e("TAG", "JMessage===??????????????????=====???: "+p1)
                            }
                        }
                    })
                },5000)
            }
        }

    }

    //?????????
    @SuppressLint("ClickableViewAccessibility")
    private fun setCarousel(){
        vp_me_carousel.apply {
            val imgIds = mutableListOf(R.mipmap.carousel1, R.mipmap.carousel2, R.mipmap.carousel3)
            bAdapter?.setNewData(imgIds)
            // ??? 999999 ???????????????????????? 3 ??????????????????????????????????????????????????????????????????
            setCurrentItem(999999, false)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                // ?????????????????????????????????????????????????????????????????????????????? CarouselDots ??? setSelectedDotX ??????
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    me_carousel_dots.setSelectedDotX(position % imgIds.size, positionOffset)
                }

                // ???????????????????????????????????????????????? curCarouselPosition ??????
                // Handler ?????? curCarouselPosition ????????????????????????
                override fun onPageSelected(position: Int) {
                    curCarouselPosition = position
                }
            })
            /*
             * ??????????????????????????????????????????????????????????????????????????????
             * ?????????????????? true????????? true ??????????????????????????? vp ?????????????????????
             */
            getChildAt(0).setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        stopCarousel()
                        false
                    }
                    MotionEvent.ACTION_UP -> {
                        startCarousel()
                        false
                    }
                    else -> false
                }
            }
            // ?????????????????????????????????
            me_carousel_dots.dotCount = 3
        }
    }

    /**
     * ???????????????????????????
     */
    private fun initUserBean(){
        val user = LitePal.findLast(UserBean::class.java)
        if (user!=null){
            val avatar = user.headshot?.let {
                BitmapFactory.decodeByteArray(user.headshot,0,
                    it.size)
            }
            when (user.userLoginType){
                1->{
                    iv_me_user_avatar.setImageBitmap(avatar)
                }
                3->{
//                    iv_me_user_avatar.setImageBitmap(Base64ToBitmapUtil.base64ToBitmap(user.userImg))
                    ImageLoaderUtil().displayHeadImage(context,user.userImg,iv_me_user_avatar)
                }
            }

            tv_me_user_nickname.text = user.userName
            C.USER_ID = user.userId
            C.IS_LOGIN = user.isLogin
        }
    }

    /**
     * ????????????????????? Handler?????? Handler ?????? vp
     */
    private fun newCarouselTask() = object : TimerTask() {
        override fun run() {
            carouselHandler.sendEmptyMessage(0)
        }
    }

    /**
     * ???????????????????????? 3s ?????????????????????
     */
    private fun startCarousel() {
        carouselTask = newCarouselTask()
        carouselTimer.schedule(carouselTask, 3000L, 3000L)
    }

    private fun stopCarousel() {
        carouselTask?.cancel()
        carouselTimer.purge()
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if(msg.what == 1){
                context?.toast("????????????????????????")
                val info = msg.obj as QQAvatarBean
                iv_me_user_avatar.setImageBitmap(info.avatar)
                tv_me_user_nickname.text = info.nickname
                val userBean = UserBean()
                userBean.userName = info.nickname
                userBean.headshot = convertImg(info.avatar!!)
                userBean.userId = info.nickname
                userBean.userLoginType = 1
                userBean.isLogin = true
                userBean.save()
                C.USER_ID = userBean.userId
            }else{
                context?.toast("????????????????????????")
            }
        }
    }

    /**
     * ??????????????? ?????????????????????
     */
    private fun convertImg(bitmap: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.CALL_PHONE)
            .onGranted { permissions ->
                val intent = Intent(Intent.ACTION_CALL)
                val data = Uri.parse("tel:" + "18695635119")
                intent.data = data
                startActivity(intent)
            }
            .onDenied { permissions ->

            }
            .start()
    }

    @SuppressLint("MissingPermission")
    private fun checkCameraPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.CAMERA)
            .onGranted { permissions ->
                activity?.startActivity<ActivityScannerCode>()
            }
            .onDenied { permissions ->

            }
            .start()
    }

    override fun onResume() {
        super.onResume()
        // ???????????????
        startCarousel()
    }

    override fun onPause() {
        super.onPause()
        // ???????????????
        stopCarousel()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        fun newInstance(): MeFragment {
            val fragment = MeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}