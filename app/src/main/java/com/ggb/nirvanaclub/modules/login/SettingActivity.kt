package com.ggb.nirvanaclub.modules.login

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.SettingAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.SettingBean
import com.ggb.nirvanaclub.bean.SettingListBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.modules.user.NirvanaEarnActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.NetUtils
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_login_setting.*
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import per.goweii.anylayer.Layer
import per.goweii.anylayer.guide.GuideLayer

class SettingActivity : BaseActivity(), GGBContract.View{

    private lateinit var present: GGBPresent

    private var mAdapter : SettingAdapter?=null

    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL,"牛蛙呐设置")

    override fun getLayoutResource(): Int = R.layout.activity_login_setting

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(me_toolbar).init()

        mAdapter = SettingAdapter()
        me_setting_options_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        me_setting_options_rv.adapter = mAdapter

        initSettingData()
        window?.decorView?.doOnLayout {
            showGuideDialogIfNeeded()
        }
    }

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initEvent() {
        floating_login_out_btn.setOnClickListener {
            if (!NetUtils.isNetConnected(this)){
                RxToast.error(this,"网络出差了。。。", Toast.LENGTH_SHORT, true)?.show()
                return@setOnClickListener
            }
            present.loginOut()
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
            floating_login_out_btn.visibility = View.GONE
        }else{
            floating_login_out_btn.visibility = View.VISIBLE
        }
    }

    private fun showGuideDialogIfNeeded(){
        SharedPreferencesUtil.putUserBoolean(this,"KEY_SETTING_GUIDE",true)

        if (!SharedPreferencesUtil.getUserBoolean(this,"KEY_SETTING_GUIDE")){
            return
        }
        window?.decorView?.post {
            showGuideBackBtnDialog {
                SharedPreferencesUtil.putUserBoolean(this,"KEY_SETTING_GUIDE",false)
//                showGuideDoubleTapDialog {
//                    showGuidePreviewImageDialog {
//                        GuideSPUtils.getInstance().setArticleGuideShown()
//                    }
//                }
            }
        }
    }

    private fun showGuideBackBtnDialog(onDismiss: () -> Unit) {
        GuideLayer(this@SettingActivity)
            .backgroundColorInt(resources.getColor(R.color.colorDialogBg))
            .mapping(GuideLayer.Mapping().apply {
                targetView(floating_login_out_btn)
                cornerRadius(9999F)
                guideView(LayoutInflater.from(this@SettingActivity)
                    .inflate(R.layout.dialog_guide_tip, null, false).apply {
                        findViewById<TextView>(R.id.dialog_guide_tv_tip).apply {
                            text = "退出账号请点击这里哦~"
                        }
                    })
                marginLeft(16)
                horizontalAlign(GuideLayer.Align.Horizontal.TO_LEFT)
                verticalAlign(GuideLayer.Align.Vertical.CENTER)
            })
            .mapping(GuideLayer.Mapping().apply {
                val cx = window?.decorView?.width ?: 0 / 2
                val cy = window?.decorView?.height ?: 0 / 2
                targetRect(Rect(cx, cy, cx, cy))
                guideView(LayoutInflater.from(this@SettingActivity)
                    .inflate(R.layout.dialog_guide_btn, null, false).apply {
                        findViewById<TextView>(R.id.dialog_guide_tv_btn).apply {
                            text = "我知道了"
                        }
                    })
                marginBottom(48)
                horizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                verticalAlign(GuideLayer.Align.Vertical.CENTER)
                onClick(Layer.OnClickListener { layer, _ ->
                    layer.dismiss()
                }, R.id.dialog_guide_tv_btn)
            })
            .onVisibleChangeListener(object : Layer.OnVisibleChangeListener {
                override fun onShow(layer: Layer) {
                }

                override fun onDismiss(layer: Layer) {
                    onDismiss.invoke()
                }
            })
            .show()

    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                GGBContract.LOGINOUT -> {
                    EventBus.getDefault().post(UserStateChangeEvent(2))
                    finish()
                }
            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
    }
}