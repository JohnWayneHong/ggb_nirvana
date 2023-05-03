package com.ggb.nirvanaclub.modules.login

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ArticleLikeInfoAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.ArticleLikeInfoBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.RegisterEvent
import com.ggb.nirvanaclub.modules.article.ArticleActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.CopyUtils
import com.ggb.nirvanaclub.utils.PasswordUtils
import com.ggb.nirvanaclub.utils.RsaEncryptionUtils
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_register_first.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class RegisterFirstFragment :BaseFragment(),GGBContract.View{

    private var isShowOldPassword = false
    private var isShowCheckPassword = false

    private var isFinish = false

    private var present:GGBPresent?=null

    override fun getLayoutResource() = R.layout.fragment_register_first

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    @SuppressLint("ResourceType")
    override fun initView() {

        initEvent()
    }

    private fun initEvent() {
        ll_old_password_status.setOnClickListener {
            isShowOldPassword = !isShowOldPassword
            if (isShowOldPassword) {
                iv_old_password_status.setImageResource(R.mipmap.ic_eye_open)
                user_register_pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                iv_old_password_status.setImageResource(R.mipmap.ic_eye_close)
                user_register_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            user_register_pwd.setSelection(user_register_pwd.text.length)
        }

        ll_check_password_status.setOnClickListener {
            isShowCheckPassword = !isShowCheckPassword
            if (isShowCheckPassword) {
                iv_check_password_status.setImageResource(R.mipmap.ic_eye_open)
                user_register_pwd_check.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                iv_check_password_status.setImageResource(R.mipmap.ic_eye_close)
                user_register_pwd_check.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            user_register_pwd_check.setSelection(user_register_pwd_check.text.length)
        }

        btn_user_register_next.setOnClickListener {
            if (!checkPasswordSame()){
                RxToast.warning("两次密码输入不一致！")
                return@setOnClickListener
            }else{
                val message = checkTableContent()
                if (message != ""){
                    RxToast.warning(message)
                }else{
                    isFinish = true
                    SharedPreferencesUtil.putRegisterString(context,"nickName",et_user_register_account.text.toString())
                    SharedPreferencesUtil.putRegisterString(context,"password", RsaEncryptionUtils().rsaEncode(user_register_pwd.text.toString()))
                    EventBus.getDefault().post(RegisterEvent(!isFinish,1))
                }
            }
        }
    }

    private fun checkPasswordSame():Boolean{
        return user_register_pwd.text.toString()==user_register_pwd_check.text.toString()
    }

    private fun checkTableContent():String{
        if (et_user_register_account.text.isNullOrEmpty()){
            return "请输入昵称"
        }
        if (user_register_pwd.text.isNullOrEmpty()){
            return "请输入密码"
        }
        if (user_register_pwd_check.text.isNullOrEmpty()){
            return "请再次输入密码"
        }
        return if (PasswordUtils.isLetterDigit(user_register_pwd_check.text.toString())&&user_register_pwd_check.text.length>=6&&user_register_pwd_check.text.length<=20){
            ""
        }else{
            "密码长度在 6-20 之间, 且包含字母和数字"
        }

    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.ARTICLELIKEINFO -> {

                }
                GGBContract.DISLIKEARTICLE -> {

                }
                else -> {

                }

            }
        }
    }

    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)

    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
    }

    companion object {
        fun newInstance(): RegisterFirstFragment {
            val fragment = RegisterFirstFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}