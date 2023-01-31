package com.ggb.nirvanaclub.modules.login

import android.util.Log
import android.view.View
import android.widget.Toast
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.GlideImageLoader
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import kotlinx.android.synthetic.main.activity_develop_setting.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.litepal.LitePal

class DevelopSettingActivity : BaseActivity() , GGBContract.View,DevelopRandomGirlUtil.OnGetRandomGirlCompleteListener,DevelopJokesUtil.OnGetRandomJokesCompleteListener{

    private lateinit var present: GGBPresent


    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL,"牛蛙呐开发者社区")

    override fun getLayoutResource(): Int = R.layout.activity_develop_setting

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(public_title).init()

        if (!isLoginCheck()){
            val rxDialog = RxDialogSureCancel(this)
            rxDialog.setContent("你还没有登录，无法进入开发者社区")
            rxDialog.sureView.setOnClickListener {
                RxToast.info(this,"快去登录叭！", Toast.LENGTH_SHORT, true)?.show()
                rxDialog.cancel()
                startActivity<LoginActivity>()
                finish()
            }
            rxDialog.cancelView.setOnClickListener {
                rxDialog.cancel()
                finish()
            }
            rxDialog.show()
            return
        }else{

        }
    }

    override fun initEvent() {
        ppi_develop_password.setOnPasswordListener {
            if (ppi_develop_password.passwordString=="123456"){
                RxToast.success(this, "验证通过！", Toast.LENGTH_SHORT)?.show()
                ll_develop.visibility = View.VISIBLE
                ll_develop_check.visibility = View.GONE
//                present.developGetRandomGirl()
                DevelopRandomGirlUtil.getRandomGirl(this,this)
                DevelopJokesUtil.getRandomJokes(this,this)
            }else{
                RxToast.error(this, "验证失败！", Toast.LENGTH_SHORT)?.show()
                ll_develop.visibility = View.GONE
                ll_develop_check.visibility = View.VISIBLE
            }
        }
        tv_develop_change.setOnClickListener {
            DevelopRandomGirlUtil.getRandomGirl(this,this)
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                GGBContract.GETRANDOMGIRL -> {
                    data as List<DevelopRandomGirlListBean>

                    val bannerList = arrayListOf<String>()
                    data.forEach {
                        bannerList.add(it.imageUrl)
                    }
                    ban_develop_list.setImageLoader(GlideImageLoader())
                    ban_develop_list.setImages(bannerList)
                    ban_develop_list.start()

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

    private fun isLoginCheck():Boolean{
        val user = LitePal.findLast(UserBean::class.java)

        return if (user!=null){
            //第三方登录 账号虚假 后期可改
            user.userLoginType != 1
        }else{
            false
        }
    }

    override fun onComplete(result: DevelopRandomGirlBean) {
        ban_develop_list.setImageLoader(GlideImageLoader())
        val bannerList = arrayListOf<String>()
        result.data.forEach {
            bannerList.add(it.imageUrl)
        }
        ban_develop_list.setImages(bannerList)
        ban_develop_list.start()
    }

    override fun onJokesComplete(result: DevelopJokesBean) {

    }

}