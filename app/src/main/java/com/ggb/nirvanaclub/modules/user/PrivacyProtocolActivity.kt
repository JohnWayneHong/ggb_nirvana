package com.ggb.nirvanaclub.modules.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.constans.C
import kotlinx.android.synthetic.main.activity_privacy_protocol.*

class PrivacyProtocolActivity:BaseActivity() {

    private var pagerTitle = arrayListOf("用户协议","隐私政策")
    private var fragments = arrayListOf<BaseFragment>()

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"牛蛙呐协议与政策")

    override fun getLayoutResource(): Int = R.layout.activity_privacy_protocol

    override fun initView() {
        initFragment()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(UserProtocolFragment.newInstance())
        fragments.add(PrivacyPolicyFragment.newInstance())

        vp_privacy_page.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_privacy_type.setupWithViewPager(vp_privacy_page)

        vp_privacy_page.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {

            }
        })
    }

    override fun initEvent() {
        
    }
}