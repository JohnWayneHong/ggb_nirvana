package com.ggb.nirvanaclub.modules.user

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.HomeListAdapter
import com.ggb.nirvanaclub.adapter.UserInfoOneListAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_info_one.*

class UserInfoOneFragment :BaseFragment(){

    private lateinit var mAdapter: UserInfoOneListAdapter

    override fun getLayoutResource() = R.layout.fragment_user_info_one

    override fun initView() {
        mAdapter = UserInfoOneListAdapter()
        rcy_user_info_one.layoutManager = LinearLayoutManager(activity)
        rcy_user_info_one.adapter = mAdapter

        val list = arrayListOf<String>()

        list.addAll(arrayListOf("111","222","333"))
        mAdapter.setNewData(list)
    }

    companion object {
        fun newInstance(): UserInfoOneFragment {
            val fragment = UserInfoOneFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}