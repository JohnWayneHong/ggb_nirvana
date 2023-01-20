package com.ggb.nirvanaclub.modules.user

import android.os.Bundle
import android.util.Log
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment

class UserInfoThreeFragment :BaseFragment(){

    override fun getLayoutResource() = R.layout.fragment_message

    override fun initView() {
        Log.e("TAG", "initView:这是第三个fragment " )

    }

    companion object {
        fun newInstance(): UserInfoThreeFragment {
            val fragment = UserInfoThreeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}