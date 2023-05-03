package com.ggb.nirvanaclub.modules.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.constans.C
import kotlinx.android.synthetic.main.activity_like_collection.*

class UserLikeAndCollectionActivity:BaseActivity() {

    private var pagerTitle = arrayListOf("赞过的文章","收藏文章")
    private var fragments = arrayListOf<BaseFragment>()

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"赞和收藏")

    override fun getLayoutResource(): Int = R.layout.activity_like_collection

    override fun initView() {
        initFragment()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(UserLikeInfoFragment.newInstance())
        fragments.add(UserCollectionInfoFragment.newInstance())

        vp_like_and_collection.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_like_collection_type.setupWithViewPager(vp_like_and_collection)

        vp_like_and_collection.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                if (p0==0){
                    fragments[p0+1].refreshData()
                }else{
                    fragments[p0-1].refreshData()
                }
            }
        })
    }

    override fun initEvent() {
        
    }
}