package com.ggb.nirvanaclub.modules.user

import android.os.Bundle
import android.util.Log
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment

class UserInfoTwoFragment :BaseFragment(){

    override fun getLayoutResource() = R.layout.fragment_message

    override fun initView() {
        Log.e("TAG", "initView:这是第二个fragment " )

    }

    companion object {
        fun newInstance(): UserInfoTwoFragment {
            val fragment = UserInfoTwoFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}