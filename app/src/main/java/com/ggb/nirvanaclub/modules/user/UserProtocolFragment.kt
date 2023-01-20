package com.ggb.nirvanaclub.modules.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.modules.IndexFragment
import kotlinx.android.synthetic.main.fragment_user_protocol.*

class UserProtocolFragment : BaseFragment() {

    override fun getLayoutResource(): Int = R.layout.fragment_user_protocol

    override fun initView() {
        user_protocol_content.loadUrl("file:///android_asset/user_protocol.html")
    }

    companion object {
        fun newInstance(): UserProtocolFragment {
            val fragment = UserProtocolFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}