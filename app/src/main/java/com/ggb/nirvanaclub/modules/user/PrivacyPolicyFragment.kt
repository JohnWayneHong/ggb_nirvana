package com.ggb.nirvanaclub.modules.user

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_privacy_policy.*

class PrivacyPolicyFragment : BaseFragment() {

    override fun getLayoutResource(): Int = R.layout.fragment_privacy_policy

    override fun initView() {
        privacy_policy_content.loadUrl("file:///android_asset/privacy_policy.html")
    }

    companion object {
        fun newInstance(): PrivacyPolicyFragment {
            val fragment = PrivacyPolicyFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}