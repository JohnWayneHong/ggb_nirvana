package com.ggb.nirvanaclub.modules.login

import android.preference.Preference
import android.preference.PreferenceActivity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.MainActivity
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.MeOptionAdapter
import com.ggb.nirvanaclub.adapter.SettingAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.SettingBean
import com.ggb.nirvanaclub.bean.SettingListBean
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.modules.user.NirvanaEarnActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.utils.CacheDataUtil
import com.ggb.nirvanaclub.utils.NetUtils
import com.ggb.nirvanaclub.utils.showToast
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_login_setting.*
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity
import org.litepal.LitePal

class SettingActivity : BaseActivity(), GGBContract.View{

    private var mAdapter : SettingAdapter?=null

    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL,"牛蛙呐设置")

    override fun getLayoutResource(): Int = R.layout.activity_login_setting

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(me_toolbar).init()

        mAdapter = SettingAdapter()
        me_setting_options_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        me_setting_options_rv.adapter = mAdapter

        initSettingData()
    }

    override fun initEvent() {
        sl_me_settings_logout.setOnClickListener {
            if (!NetUtils.isNetConnected(this)){
                RxToast.error(this,"网络出差了。。。", Toast.LENGTH_SHORT, true)?.show()
                return@setOnClickListener
            }
            EventBus.getDefault().post(UserStateChangeEvent(2))
            finish()
        }
//        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
//            if (mAdapter?.data?.get(position)?.title=="其他"){
//                startActivity<NirvanaEarnActivity>()
//            }
//        }

        mAdapter?.setOnItemSettingChangeListener(object :SettingAdapter.OnItemSettingChangeListener{
            override fun onSelect(data:SettingListBean) {
                if (data.childTitle=="分享牛蛙呐"){
                    startActivity<NirvanaEarnActivity>()
                }
            }
        })
    }

    private fun initSettingData(){
        val options = mutableListOf(
            SettingBean("消息推送", mutableListOf(
                SettingListBean("文章更新推送"),
                SettingListBean("新消息推送"),
            )),
            SettingBean("账号设置", mutableListOf(
                SettingListBean("编辑个人资料"),
                SettingListBean("账号与安全"),
            )),
            SettingBean("通用设置", mutableListOf(
                SettingListBean("默认编辑器"),
                SettingListBean("添加写文章到桌面"),
                SettingListBean("赞赏设置"),
                SettingListBean("字号设置"),
                SettingListBean("隐私设置"),
                SettingListBean("黑名单设置"),
                SettingListBean("移动网络下加载图片"),
            )),
            SettingBean("其他", mutableListOf(
                SettingListBean("回收站"),
                SettingListBean("清除缓存"),
                SettingListBean("版本更新"),
                SettingListBean("分享牛蛙呐", "推荐"),
                SettingListBean("关于我们"),
            )),
        )
        mAdapter?.setNewData(options)
        if (C.USER_ID.isEmpty()){
            sl_me_settings_logout.visibility = View.GONE
        }else{
            sl_me_settings_logout.visibility = View.VISIBLE
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
    }
}