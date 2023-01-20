package com.ggb.nirvanaclub.modules.user

import android.graphics.Color
import android.util.Log
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.HealthyBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.manager.ServiceTimeManager
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.utils.CodeUtils
import com.ggb.nirvanaclub.utils.HealthyTimeUtils
import com.ggb.nirvanaclub.utils.TimeUtil
import com.ggb.nirvanaclub.view.dialog.HealthyCodeDialog
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_healthy.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.attr

class HealthyActivity : BaseActivity() , GGBContract.View, HealthyTimeUtils.OnHealthyTimeCompleteListener,ServiceTimeManager.OnTimeSecondUpdateListener{

    private var healthyDialog:HealthyCodeDialog?=null

    private var isChose = false

    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL,"乖乖帮能康码")

    override fun getLayoutResource(): Int = R.layout.activity_healthy

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(public_title).init()
        ServiceTimeManager.getInstance().registerTimeUpdateListener(this)
        healthyDialog = HealthyCodeDialog(this)

        healthyDialog?.showHealthyCode()
        HealthyTimeUtils.HealthyTime(C.ANDROID_UPDATE_TIMES_ADDRESS,this,this)

    }

    override fun initEvent() {
        healthyDialog?.setOnHealthyCodeListener(object :HealthyCodeDialog.OnHealthyCodeListener{
            override fun onConfirm() {
                isChose = true
                ll_healthy_color.setBackgroundColor(Color.rgb(81,173,111))
                iv_healthy_bar_code.setImageBitmap(CodeUtils.createQRCodeBitmap("乖乖熊是绿码", Color.rgb(81,173,111)))
                tv_healthy_code_warning.text = "凭此码可在乖乖帮范围内通行，请主动出示"
                tv_healthy_name.text = "乖乖熊"
                tv_healthy_hesuan.text = "<1"
                tv_healthy_result.text = "天\n阴性"
                tv_healthy_result_2.text = TimeUtil.getStringByFormat(ServiceTimeManager.getInstance().currentTimeMillis(),TimeUtil.dateFormatYMDHM)
            }

            override fun onCancel() {
                isChose = true
                ll_healthy_color.setBackgroundColor(Color.rgb(236,9,9))
                iv_healthy_bar_code.setImageBitmap(CodeUtils.createQRCodeBitmap("垃圾小兔子是红码！！！马上抓捕！", Color.rgb(236,9,9)))
                tv_healthy_code_warning.text = "您的能康码是红码！请立刻前往乖乖帮方舱医院治疗！"
                tv_healthy_name.text = "乖乖兔"
                tv_healthy_hesuan.text = "<1"
                tv_healthy_result.text = "天\n能性"
                tv_healthy_result_2.text = TimeUtil.getStringByFormat(ServiceTimeManager.getInstance().currentTimeMillis(),TimeUtil.dateFormatYMDHM)

            }

        })

        healthyDialog?.setOnDismissListener {
            if (!isChose){
                finish()
            }
        }

    }

    override fun onSuccess(flag: String?, data: Any?) {
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
    }

    override fun onConfigComplete(result: HealthyBean?) {
        ServiceTimeManager.getInstance().setCurServiceTime(result!!.currentTime)
        Log.e("TAG", "onConfigComplete: ------->${result.currentTime}", )
    }

    override fun onSecondUpdate(mills: Long) {
        tv_healthy_date.text = TimeUtil.getStringByFormat(mills,TimeUtil.dateFormatMDofChinese)
        tv_healthy_hours.text = TimeUtil.getStringByFormat(mills,TimeUtil.dateFormatHMS)
    }

    override fun onUpdateTime() {
        HealthyTimeUtils.HealthyTime(C.ANDROID_UPDATE_TIMES_ADDRESS,this,this)
    }

    override fun onDestroy() {
        super.onDestroy()
        healthyDialog?.dismiss()
        ServiceTimeManager.getInstance().unregisterTimeUpdateListener();
    }
}