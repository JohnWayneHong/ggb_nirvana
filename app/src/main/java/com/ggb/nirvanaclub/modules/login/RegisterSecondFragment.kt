package com.ggb.nirvanaclub.modules.login

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ArticleLikeInfoAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.ArticleLikeInfoBean
import com.ggb.nirvanaclub.bean.DataConfigBean
import com.ggb.nirvanaclub.bean.RegisterCheckBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.RegisterEvent
import com.ggb.nirvanaclub.modules.article.ArticleActivity
import com.ggb.nirvanaclub.modules.keyboard.utils.EmoticonsKeyboardUtils
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.CopyUtils
import com.ggb.nirvanaclub.utils.PasswordUtils
import com.ggb.nirvanaclub.utils.RegularUtil
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.view.popup.ItemSelectWindow
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_register_second.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class RegisterSecondFragment :BaseFragment(),GGBContract.View{

    private var registerTypeWindow: ItemSelectWindow? = null

    private var registerType = 0

    private var isFinish = false

    private var present:GGBPresent?=null

    override fun getLayoutResource() = R.layout.fragment_register_second

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    @SuppressLint("ResourceType")
    override fun initView() {

        registerTypeWindow = ItemSelectWindow(requireActivity())
        EmoticonsKeyboardUtils.closeSoftKeyboard(context)
        initEvent()
    }

    private fun initEvent() {

        ll_register_type.setOnClickListener {
            val tl = arrayListOf<DataConfigBean>()
            tl.add(DataConfigBean(1,"邮箱注册"))
            tl.add(DataConfigBean(2,"手机注册"))
            registerTypeWindow?.showItemList(ll_register_type, tl)
            showArrow(true,iv_register_type)
        }
        registerTypeWindow?.setOnItemSelectListener(object :ItemSelectWindow.OnItemSelectListener{
            override fun onSelect(config: DataConfigBean) {
                tv_register_type.text = config.value
                registerType = config.key
                if (registerType == 2){
                    tv_user_register_address.text = "手机账号"
                }else{
                    tv_user_register_address.text = "邮箱地址"
                }
            }
        })
        registerTypeWindow?.setOnDismissListener {
            showArrow(false,iv_register_type)
        }

        btn_register_second_next.setOnClickListener {
            val message = checkTableContent()
            if (message != ""){
                RxToast.warning(message)
            }else{
                if (registerType == 1){
                    present?.checkAccountIsRegister(user_register_address.text.toString())
                }
            }
        }
        btn_register_second_pre.setOnClickListener {

            EventBus.getDefault().post(RegisterEvent(isFinish,0))
        }
    }

    private fun showArrow(isUp:Boolean,image: ImageView){
        if(isUp){
            image.setImageResource(R.mipmap.ic_up_arrow_black)
        }else{
            image.setImageResource(R.mipmap.ic_down_arrow_black)
        }
    }

    private fun checkTableContent():String{
        if (registerType == 1){
            return if (RegularUtil.isEmailLegal(user_register_address.text.toString())) "" else "请输入有效的邮箱地址"
        }
        if (registerType == 2){
            return if (RegularUtil.isChinaPhoneLegal(user_register_address.text.toString())) "" else "请输入有效的手机号码"
        }
        if (registerType == 0){
            return "请选择注册方式"
        }
        return "当前有Bug，请联系管理员反馈"
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.CHECKACCOUNTISREGISTER -> {
                    data as RegisterCheckBean
                    isFinish = data.ok
                    if (!isFinish){
                        RxToast.warning("该账号已经注册")
                    }else{
                        SharedPreferencesUtil.putRegisterString(context,"account",user_register_address.text.toString())
                        SharedPreferencesUtil.putRegisterString(context,"type",
                        if (registerType == 1) "email" else "phone")
                        EventBus.getDefault().post(RegisterEvent(!isFinish,2))
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        registerTypeWindow?.dismiss()
    }

    companion object {
        fun newInstance(): RegisterSecondFragment {
            val fragment = RegisterSecondFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}