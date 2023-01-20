package com.ggb.nirvanaclub.modules.laboratory.captcha

import android.graphics.drawable.Drawable
import android.widget.SeekBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxkit.TLog
import kotlinx.android.synthetic.main.activity_captcha.*

class CaptchaActivity : BaseActivity() , GGBContract.View{

    private lateinit var present: GGBPresent

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_captcha

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_laboratory_captcha).init()

        scv_swipe_captcha.onCaptchaMatchCallback = object : SwipeCaptchaView.OnCaptchaMatchCallback {
            override fun matchSuccess(rxSwipeCaptcha: SwipeCaptchaView) {
                RxToast.success(this@CaptchaActivity, "验证通过！", Toast.LENGTH_SHORT)?.show()
                //swipeCaptcha.createCaptcha();
                sb_captcha_dragBar.isEnabled = false
            }

            override fun matchFailed(rxSwipeCaptcha: SwipeCaptchaView) {
                TLog.d("zxt", "matchFailed() called with: rxSwipeCaptcha = [$rxSwipeCaptcha]")
                RxToast.error(this@CaptchaActivity, "验证失败:拖动滑块将悬浮头像正确拼合", Toast.LENGTH_SHORT)?.show()
                rxSwipeCaptcha.resetCaptcha()
                sb_captcha_dragBar.progress = 0
            }
        }
        sb_captcha_dragBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                scv_swipe_captcha.setCurrentSwipeValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //随便放这里是因为控件
                sb_captcha_dragBar.max = scv_swipe_captcha.maxSwipeValue
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                TLog.d("zxt", "onStopTrackingTouch() called with: seekBar = [$seekBar]")
                scv_swipe_captcha.matchCaptcha()
            }
        })
        val simpleTarget: SimpleTarget<Drawable?> = object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                scv_swipe_captcha.setImageDrawable(resource)
                scv_swipe_captcha.createCaptcha()
            }
        }

        //测试从网络加载图片是否ok
        Glide.with(this)
            .load(R.mipmap.welcome)
            .into(simpleTarget)
    }

    override fun initEvent() {
        btn_captcha_change.setOnClickListener {
            scv_swipe_captcha.createCaptcha()
            sb_captcha_dragBar.isEnabled = true
            sb_captcha_dragBar.progress = 0
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }
}