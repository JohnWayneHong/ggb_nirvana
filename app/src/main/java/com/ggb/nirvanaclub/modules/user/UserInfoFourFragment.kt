package com.ggb.nirvanaclub.modules.user

import android.os.Bundle
import android.util.Log
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment

class UserInfoFourFragment :BaseFragment(){

    override fun getLayoutResource() = R.layout.fragment_message

    override fun initView() {
        Log.e("TAG", "initView:这是第四个fragment " )

    }

    companion object {
        fun newInstance(): UserInfoFourFragment {
            val fragment = UserInfoFourFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}