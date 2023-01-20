package com.ggb.nirvanaclub.modules

import android.os.Bundle
import android.util.Log
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment

class MessageFragment :BaseFragment(){

    override fun getLayoutResource() = R.layout.fragment_message

    override fun initView() {

    }

    companion object {
        fun newInstance(): MessageFragment {
            val fragment = MessageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}