package com.ggb.nirvanaclub.modules.login

import android.annotation.SuppressLint
import android.os.Bundle
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.event.RegisterEvent
import com.ggb.nirvanaclub.modules.keyboard.utils.EmoticonsKeyboardUtils
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import kotlinx.android.synthetic.main.fragment_register_fourth.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class RegisterFourthFragment :BaseFragment(),GGBContract.View{

    private var present:GGBPresent?=null

    override fun getLayoutResource() = R.layout.fragment_register_fourth

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        initEvent()
    }

    private fun initEvent() {
        btn_register_fourth_login.setOnClickListener {
            EventBus.getDefault().post(RegisterEvent(true,4))
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {

            }
        }
    }

    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
    }

    companion object {
        fun newInstance(): RegisterFourthFragment {
            val fragment = RegisterFourthFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}