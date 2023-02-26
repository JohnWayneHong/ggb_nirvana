package com.ggb.nirvanaclub.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.DevelopJokesListBean
import com.ggb.nirvanaclub.modules.community.*
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_community.*
import java.util.ArrayList

class CommunityFragment : BaseFragment(), GGBContract.View{

    private var present:GGBPresent?=null

    private val mTitles = arrayOf(
        "Android","广场", "体系","导航",
         "段子乐文", "段子乐图","推荐的人"
    )

    private val mFragments = ArrayList<Fragment>()
    private var mAdapter: MyPagerAdapter? = null

    override fun getLayoutResource() = R.layout.fragment_community

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(requireActivity()).statusBarDarkFont(true).titleBar(rxTool_community_title).init()

        initFragment()
        initEvent()
    }

    private fun initFragment(){

        mFragments.clear()
        mFragments.add(CommunityAndroidFragment.newInstance())
        mFragments.add(CommunitySquareFragment.newInstance())
        mFragments.add(CommunityKnowledgeFragment.newInstance())
        mFragments.add(CommunityNavigationFragment.newInstance())
        mFragments.add(CommunityTextFragment.newInstance())
        mFragments.add(CommunityPictureFragment.newInstance())
        mFragments.add(CommunitySubscriptFragment.newInstance())

        mAdapter = MyPagerAdapter(childFragmentManager)
        vp_community.adapter = mAdapter

        rxTool_community_title.setViewPager(vp_community,mTitles, context as FragmentActivity,mFragments)

    }

    private fun initEvent(){

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

    private inner class MyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
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