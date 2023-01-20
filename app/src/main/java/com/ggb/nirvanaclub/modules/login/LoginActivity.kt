package com.ggb.nirvanaclub.modules.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.MainActivity
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.SimpleUserInfo
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.listener.BaseUiListener
import com.ggb.nirvanaclub.modules.user.PrivacyProtocolActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.RegularUtil
import com.ggb.nirvanaclub.utils.TimeCountUtil
import com.ggb.nirvanaclub.utils.showToast
import com.gyf.immersionbar.ImmersionBar
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

import org.jetbrains.anko.toast

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
     */
    private var loginType = LoginType.CODE
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
            loginType = if (loginType == LoginType.CODE) LoginType.PWD else LoginType.CODE
            when(loginType){
                LoginType.CODE->{
                    rl_login_account.visibility = View.GONE
                    rl_user_login_pwd.visibility = View.GONE
                    rl_login_mobile.visibility = View.VISIBLE
                    rl_login_mobile_code.visibility = View.VISIBLE
                    tv_user_login_change_type.text = resources.getString(R.string.me_user_login_title_pwd_string)
                }
                LoginType.PWD->{
                    rl_login_account.visibility = View.VISIBLE
                    rl_user_login_pwd.visibility = View.VISIBLE
                    rl_login_mobile.visibility = View.GONE
                    rl_login_mobile_code.visibility = View.GONE
                    tv_user_login_change_type.text = resources.getString(R.string.me_user_login_title_code_string)
                }
            }
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
                        present.login(et_user_login_mobile.text.toString(),user_login_code.text.toString(),-1)
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
                        present.login(et_user_login_account.text.toString(),user_login_pwd.text.toString(),-2)
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
                    present.info(-1)
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
                    userBean.userImg = data.photo
                    userBean.userName = data.nickName
                    userBean.userId = data.nickName
                    userBean.userLoginType = 3
                    userBean.isLogin = true
                    userBean.save()
                    EventBus.getDefault().post(UserStateChangeEvent(3))
                    finish()
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
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