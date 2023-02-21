package com.ggb.nirvanaclub.modules.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.DevelopJokesBean
import com.ggb.nirvanaclub.bean.OnCodeIsNot200
import com.ggb.nirvanaclub.bean.SimpleUserInfo
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.listener.BaseUiListener
import com.ggb.nirvanaclub.modules.user.PrivacyProtocolActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.*
import com.ggb.nirvanaclub.view.RxToast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.litepal.LitePal

class LoginActivity : BaseActivity() ,GGBContract.View,TimeCountUtil.OnCountDownListener{

    private lateinit var present: GGBPresent
    private lateinit var  iu: BaseUiListener
    private var time: TimeCountUtil?=null

    private var isShowPassword = false
    private var isSelectPrivacy = false
    private var isGetCode = false

    private var lastClickTime = 0L

    /**
     * 登录方式，默认是手机验证码登录
     * 新版本尚未实现，暂时默认账户登录
     */
    private var loginType = LoginType.PWD
    private lateinit var codeSpannableString: SpannableString
    private lateinit var pwdSpannableString: SpannableString

    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL,"牛蛙呐登录")

    override fun getLayoutResource() = R.layout.activity_login

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(public_title).init()
        EventBus.getDefault().register(this)

        initMustReadSpannableString()
        setMustReadText()
        initQQLogin()
        user_login_verify_bar.apply {
            draggable = user_login_verify_draggable
            actualDraggable = user_login_verify_actual_draggable
        }
        time = TimeCountUtil(60000, 1000)
        time?.setOnCountDownListener(this)
    }

    override fun initEvent(){
        tv_user_login_change_type.setOnClickListener {
            RxToast.info("牛蛙呐系统升级中，暂时无法使用手机登录，请谅解~")

//            loginType = if (loginType == LoginType.CODE) LoginType.PWD else LoginType.CODE
//            when(loginType){
//                LoginType.CODE->{
//                    rl_login_account.visibility = View.GONE
//                    rl_user_login_pwd.visibility = View.GONE
//                    rl_login_mobile.visibility = View.VISIBLE
//                    rl_login_mobile_code.visibility = View.VISIBLE
//                    tv_user_login_change_type.text = resources.getString(R.string.me_user_login_title_pwd_string)
//                }
//                LoginType.PWD->{
//                    rl_login_account.visibility = View.VISIBLE
//                    rl_user_login_pwd.visibility = View.VISIBLE
//                    rl_login_mobile.visibility = View.GONE
//                    rl_login_mobile_code.visibility = View.GONE
//                    tv_user_login_change_type.text = resources.getString(R.string.me_user_login_title_code_string)
//                }
//            }
            setMustReadText()
        }
        iv_user_login_qq.setOnClickListener {
            if (!isSelectPrivacy){
                resources.getString(R.string.user_login_mustread_popup_string).showToast()
                return@setOnClickListener
            }
            if (!App.mTencent.isSessionValid) {
                when (App.mTencent.login(this, "all",iu)) {
                    0 -> "正常登录".showToast()
                    1 -> "正在跳转QQ登录".showToast()
                    -1 -> {
                        "异常".showToast()
                        App.mTencent.logout(App.context)
                    }
                    2 -> "使用H5登陆或显示下载页面".showToast()
                    else -> "出错".showToast()
                }
            }
        }
        tv_user_login_must_read.setOnClickListener {
            startActivity<PrivacyProtocolActivity>()
        }
        ll_user_login_must_read.setOnClickListener {
            user_login_must_read_checkbox.isChecked = !user_login_must_read_checkbox.isChecked
            isSelectPrivacy = !isSelectPrivacy
        }
        user_login_get_code.setOnClickListener {
            if (!checkVerify()){
                resources.getString(R.string.verify_error).showToast()
                return@setOnClickListener
            }
            if (!isGetCode){
                if (!isSelectPrivacy){
                    resources.getString(R.string.user_login_mustread_popup_string).showToast()
                    return@setOnClickListener
                }
                if (et_user_login_mobile.text.isEmpty()){
                    resources.getString(R.string.me_user_login_account_code_hint_string).showToast()
                }
                if(!RegularUtil.isChinaPhoneLegal(et_user_login_mobile.text.toString())){
                    resources.getString(R.string.mobile_error).showToast()
                    return@setOnClickListener
                }
                present.sendCode(et_user_login_mobile.text.toString())
            }
        }
        user_login_btn.setOnClickListener {
            if (checkVerify()&&isSelectPrivacy){
                when(loginType){
                    LoginType.CODE -> {
                        if (et_user_login_mobile.text.isEmpty()){
                            resources.getString(R.string.me_user_login_account_code_hint_string).showToast()
                            return@setOnClickListener
                        }
                        if (user_login_code.text.isEmpty()){
                            resources.getString(R.string.me_user_login_code_hint_string).showToast()
                            return@setOnClickListener
                        }
                        if (!RegularUtil.isChinaPhoneLegal(et_user_login_mobile.text.toString())){
                            resources.getString(R.string.mobile_error).showToast()
                            return@setOnClickListener
                        }
                        //后续加接口
//                        present.login(et_user_login_mobile.text.toString(),user_login_code.text.toString())
                    }
                    LoginType.PWD -> {
                        if (et_user_login_account.text.isEmpty()){
                            resources.getString(R.string.me_user_login_account_pwd_hint_string).showToast()
                            return@setOnClickListener
                        }
                        if (user_login_pwd.text.isEmpty()){
                            resources.getString(R.string.me_user_login_pwd_hint_string).showToast()
                            return@setOnClickListener
                        }
                        if (!RegularUtil.isEmailLegal(et_user_login_account.text.toString())&&!RegularUtil.isChinaPhoneLegal(et_user_login_account.text.toString())){
                            resources.getString(R.string.account_error).showToast()
                            return@setOnClickListener
                        }
                        present.login(et_user_login_account.text.toString(),user_login_pwd.text.toString())
                    }
                }
            }else{
                resources.getString(R.string.verify_error_2).showToast()
                return@setOnClickListener
            }

        }

    }

    /**
     * 用户须知打勾的文字设置
     */
    private fun setMustReadText(){
        tv_user_login_must_read.apply {
            text = when(loginType){
                LoginType.CODE -> codeSpannableString
                LoginType.PWD -> pwdSpannableString
            }
        }
    }

    /**
     * 初始化用户须知打勾的 SpannableString
     */
    private fun initMustReadSpannableString() {
        pwdSpannableString = SpannableString("我已阅读并同意《用户协议》和《隐私政策》").apply {
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.gray_text_color)), 0, 7, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.primary)), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.gray_text_color)), 13, 14, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.primary)), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        codeSpannableString = SpannableString("我已阅读并同意《用户协议》和《隐私政策》，未注册手机号登录时将自动注册").apply {
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.gray_text_color)), 0, 7, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.primary)), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.gray_text_color)), 13, 14, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.primary)), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.gray_text_color)), 20, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    /**
     * 初始化QQ登录
     */
    private fun initQQLogin(){
        iu = BaseUiListener(App.mTencent)
        //初始化配置
        Tencent.setIsPermissionGranted(true, Build.MODEL)
        Tencent.resetTargetAppInfoCache()
        Tencent.resetQQAppInfoCache()
        Tencent.resetTimAppInfoCache()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getQQLoginSuccess(event: UserStateChangeEvent){
        finish()
    }

    /**
     * 判断行为验证是否通过
     */
    private fun checkVerify():Boolean{
        user_login_verify_bar.apply {
            return isVerifyPass()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //腾讯QQ回调
        Tencent.onActivityResultData(requestCode, resultCode, data,iu)
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, iu)
            }
        }
    }

    override fun onTickChange(millisUntilFinished: Long) {
        user_login_get_code.text = (millisUntilFinished / 1000).toString() + "s"
    }

    override fun OnFinishChanger() {
        user_login_get_code.text = "重新发送"
        isGetCode = false
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                GGBContract.LOGIN -> {
                    present.info()
                }
                GGBContract.SENDCODE -> {
                    time?.start()
                    isGetCode = true
                }
                GGBContract.INFO -> {
                    data as SimpleUserInfo
                    Log.e("TAG", "用户信息 名称是--->: "+data.nickName )
                    Log.e("TAG", "用户头像--->: "+data.photo )
                    val userBean = UserBean()
                    userBean.userId = data.id
                    userBean.userName = data.nickName
                    userBean.usercreateTime = data.createTime
                    userBean.account = data.account
                    userBean.userImg = data.photo
                    userBean.userSign = if (!data.sign.isNullOrEmpty()) data.sign else ""
                    userBean.userBirth = if (!data.birth.isNullOrEmpty()) data.birth else ""
                    userBean.userStatus = if (!data.status.isNullOrEmpty()) data.status else ""

                    userBean.userLoginType = 3
                    userBean.isLogin = true
                    userBean.save()
                    EventBus.getDefault().post(UserStateChangeEvent(3))
                    SharedPreferencesUtil.putUserString(this,"UserIDToJMessage", data.id)
                    SharedPreferencesUtil.putUserString(this,"UserPasswordToJMessage", data.id)

                    val user = LitePal.findLast(UserBean::class.java)

                    Log.e("TAG", "onSuccess: "+user.userId )

                    JMessageClient.register(data.id,data.id,object : BasicCallback(){
                        //统一JMessage注册用户，无论是否注册过，只要登录的用户就注册JMessage
                        override fun gotResult(p0: Int, p1: String?) {
                            if(p0==0){
                                Log.e("TAG", "JMessage===注册成功=====》: " )
                            }else{
                                if (p1 != null) {
                                    Log.e("TAG", "JMessage===注册失败=====》: "+p1 )
                                }
                            }
                            //登录JMessage
                            JMessageClient.login(data.id,data.id,object :BasicCallback(){
                                override fun gotResult(p0: Int, p1: String?) {
                                    if (p0==0){
                                        RxToast.success(this@LoginActivity, "登录成功！", Toast.LENGTH_SHORT)?.show()
                                        Log.e("TAG", "JMessage===登录成功=====》: " )

                                        //登录成功更新用户信息
                                        val userInfo = JMessageClient.getMyInfo()
                                        userInfo.nickname = userBean.userName

                                        JMessageClient.updateMyInfo(UserInfo.Field.nickname,userInfo,object :BasicCallback(){
                                            override fun gotResult(p0: Int, p1: String?) {
                                                if(p0 == 0){
                                                    Log.e("TAG", "JMessage===更新昵称成功=====》: " )
                                                }
                                            }
                                        })
                                    }else{
                                        if (p1 != null) {
                                            RxToast.error(this@LoginActivity, p1, Toast.LENGTH_SHORT)?.show()
                                            Log.e("TAG", "JMessage===登录失败=====》: "+p1 )
                                        }
                                    }
                                }

                            })

                        }
                    })

                    finish()
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        val errorBean = Gson().fromJson<OnCodeIsNot200>(string, object : TypeToken<OnCodeIsNot200>() {}.type)
        RxToast.error(errorBean.message)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            RxToast.warning("请检查网络连接")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        time?.cancel()
        EventBus.getDefault().unregister(this)
    }

    enum class LoginType {
        CODE, PWD
    }


}