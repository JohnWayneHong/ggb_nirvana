package com.ggb.nirvanaclub.modules

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.DevelopJokesListBean
import com.ggb.nirvanaclub.modules.community.CommunityPictureFragment
import com.ggb.nirvanaclub.modules.community.CommunitySubscriptFragment
import com.ggb.nirvanaclub.modules.community.CommunityTextFragment
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : BaseFragment(), GGBContract.View{

    private var present:GGBPresent?=null

    private var fragments = arrayListOf<BaseFragment>()

    override fun getLayoutResource() = R.layout.fragment_community

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(requireActivity()).statusBarDarkFont(true).titleBar(ll_community_title).init()


        initFragment()
        initEvent()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(CommunitySubscriptFragment.newInstance())
        fragments.add(CommunityPictureFragment.newInstance())
        fragments.add(CommunityTextFragment.newInstance())
        fragments.add(CommunityPictureFragment.newInstance())

        vp_community.adapter = object : FragmentPagerAdapter(childFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }
        vp_community.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(!fragments[position].isLoadComplete){
                    fragments[position].refreshData()
                }
            }
        })

        vp_community.offscreenPageLimit = 4

        tv_community_scribe.setOnClickListener {
            select(0)
        }
        tv_community_recommend.setOnClickListener {
            select(1)
        }
        tv_community_text.setOnClickListener {
            select(2)
        }
        tv_community_picture.setOnClickListener {
            select(3)
        }
        select(1)
    }

    private fun initEvent(){

    }

    private fun select(position:Int) {
        when (position) {
            0 -> {
                tv_community_scribe.setTextColor(resources.getColor(R.color.main_color))

                tv_community_recommend.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_text.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_picture.setTextColor(resources.getColor(R.color.text_main_color))
                vp_community.setCurrentItem(0, false)
            }
            1 -> {
                tv_community_recommend.setTextColor(resources.getColor(R.color.main_color))

                tv_community_scribe.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_text.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_picture.setTextColor(resources.getColor(R.color.text_main_color))
                vp_community.setCurrentItem(1, false)
            }
            2 -> {
                tv_community_text.setTextColor(resources.getColor(R.color.main_color))

                tv_community_scribe.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_recommend.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_picture.setTextColor(resources.getColor(R.color.text_main_color))
                vp_community.setCurrentItem(2, false)
            }
            3 -> {
                tv_community_picture.setTextColor(resources.getColor(R.color.main_color))

                tv_community_scribe.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_text.setTextColor(resources.getColor(R.color.text_main_color))

                tv_community_recommend.setTextColor(resources.getColor(R.color.text_main_color))
                vp_community.setCurrentItem(3, false)
            }

        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        fun newInstance(): CommunityFragment {
            val fragment = CommunityFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETJOKESTEXT -> {
                    data?.let {
                        data as List<DevelopJokesListBean>

                    }
                }
                else -> {

                }

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }
}