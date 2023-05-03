package com.ggb.nirvanaclub.modules.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.findFragment
import androidx.viewpager.widget.ViewPager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.RegisterEvent
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.tamsiree.rxui.view.indicator.TStepperIndicator
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

class RegisterActivity:BaseActivity() {

    private var fragments = arrayListOf<BaseFragment>()

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"加入牛蛙呐")

    override fun getLayoutResource(): Int = R.layout.activity_register

    override fun initView() {
        EventBus.getDefault().register(this)
        vp_register.setNoScroll(true)
        initFragment()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(RegisterFirstFragment.newInstance())
        fragments.add(RegisterSecondFragment.newInstance())
        fragments.add(RegisterThirdFragment.newInstance())
        fragments.add(RegisterFourthFragment.newInstance())

        vp_register.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }

        stepper_indicator.setViewPager(vp_register,true)

        stepper_indicator.addOnStepClickListener(object :TStepperIndicator.OnStepClickListener{
            override fun onStepClicked(step: Int) {

                vp_register.setCurrentItem(step,true)
            }

        })
//        vp_register.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
//            override fun onPageScrollStateChanged(p0: Int) {}
//            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
//            override fun onPageSelected(p0: Int) {
//                if (p0==0){
//                    fragments[p0+1].refreshData()
//                }else{
//                    fragments[p0-1].refreshData()
//                }
//            }
//        })


    }

    override fun initEvent() {
        
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getFragmentIsSuccess(event: RegisterEvent){
        when (event.item) {
            4 -> {
                startActivity<LoginActivity>()
                finish()
            }
            2 -> {
                fragments[2].refreshData()
                vp_register.setCurrentItem(event.item,true)
            }
            else -> {
                vp_register.setCurrentItem(event.item,true)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}