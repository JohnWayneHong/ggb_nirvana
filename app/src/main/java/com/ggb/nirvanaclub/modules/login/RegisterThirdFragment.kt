package com.ggb.nirvanaclub.modules.login

import android.annotation.SuppressLint
import android.os.Bundle
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.event.RegisterEvent
import com.ggb.nirvanaclub.modules.keyboard.utils.EmoticonsKeyboardUtils
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.utils.TimeCountUtil
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_register_third.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class RegisterThirdFragment :BaseFragment(),GGBContract.View, TimeCountUtil.OnCountDownListener{

    private var isFinish = false

    private var isGetCode = false

    private var time: TimeCountUtil?=null

    private var present:GGBPresent?=null

    override fun getLayoutResource() = R.layout.fragment_register_third

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        time = TimeCountUtil(60000, 1000)
        time?.setOnCountDownListener(this)

        EmoticonsKeyboardUtils.closeSoftKeyboard(context)
        initEvent()
    }

    private fun initEvent() {
        ppi_register_verify_password.setOnPasswordListener {
            present?.verifyCode(SharedPreferencesUtil.getRegisterString(context,"account"),ppi_register_verify_password.passwordString)
        }
        tv_register_get_code.setOnClickListener {
            if (!isGetCode){
                present?.sendCode(
                    SharedPreferencesUtil.getRegisterString(context,"account"),
                    SharedPreferencesUtil.getRegisterString(context,"type"),
                    SharedPreferencesUtil.getRegisterString(context,"nickName"),
                    SharedPreferencesUtil.getRegisterString(context,"password")
                )
            }
        }
        btn_register_third_pre.setOnClickListener {
            EventBus.getDefault().post(RegisterEvent(isFinish,1))
        }
    }

    override fun onTickChange(millisUntilFinished: Long) {
        tv_register_get_code.text = (millisUntilFinished / 1000).toString() + "s"
    }

    override fun OnFinishChanger() {
        tv_register_get_code.text = "重新发送"
        isGetCode = false
        btn_register_third_pre.setBackgroundColor(requireContext().resources.getColor((R.color.primary)))
        btn_register_third_pre.isEnabled = true
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.SENDCODE -> {
                    time?.start()
                    isGetCode = true
                    btn_register_third_pre.setBackgroundColor(requireContext().resources.getColor((R.color.main_color_1)))
                    btn_register_third_pre.isEnabled = false
                }
                GGBContract.VERIFYCODE -> {
                    EmoticonsKeyboardUtils.closeSoftKeyboard(context)
                    RxToast.success("账号创建成功！快去登录吧~",1)
                    EventBus.getDefault().post(RegisterEvent(isFinish,3))
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

    override fun refreshData() {
        super.refreshData()
        if (!isGetCode){
            present?.sendCode(
                SharedPreferencesUtil.getRegisterString(context,"account"),
                SharedPreferencesUtil.getRegisterString(context,"type"),
                SharedPreferencesUtil.getRegisterString(context,"nickName"),
                SharedPreferencesUtil.getRegisterString(context,"password")
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        time?.cancel()
    }

    companion object {
        fun newInstance(): RegisterThirdFragment {
            val fragment = RegisterThirdFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}