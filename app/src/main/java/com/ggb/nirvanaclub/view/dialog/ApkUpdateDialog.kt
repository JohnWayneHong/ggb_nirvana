package com.ggb.nirvanaclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.AppUpdateBean
import com.ggb.nirvanaclub.utils.AppUtils
import com.ggb.nirvanaclub.utils.ScreenUtils
import com.google.gson.JsonParser
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.dialog_new_apk_update.*

class ApkUpdateDialog : Dialog {

    private lateinit var info : AppUpdateBean

    private var uiStyle = 300

    constructor(context: Context) : super(context, R.style.CustomDialog)

    constructor(context: Context,style:Int) : super(context, R.style.CustomDialog) {
        this.uiStyle = style
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(uiStyle){
            300 ->{
                setContentView(R.layout.dialog_new_apk_update)
            }
            301 ->{
                setContentView(R.layout.dialog_new_apk_update_1)
            }
            306 ->{
                setContentView(R.layout.dialog_new_apk_update_6)
            }
            307 ->{
                setContentView(R.layout.dialog_new_apk_update_7)
            }
            308 ->{
                setContentView(R.layout.dialog_new_apk_update_8)
            }
            309 ->{
                setContentView(R.layout.dialog_new_apk_update_9)
            }
            310 ->{
                setContentView(R.layout.dialog_new_apk_update_10)
            }
        }

        initWindow()
        initEvent()
    }

    private fun initWindow(){
        val lp = window?.attributes
        lp?.width = (ScreenUtils.getScreenWidth(context)*0.9).toInt()
        lp?.height = (ScreenUtils.getScreenWidth(context)*1.3).toInt()
        window?.attributes = lp
    }

    fun showUpdate(info : AppUpdateBean){
        show()
        this.info = info

        tv_update_new_version.text = AppUtils.getVersionName(context)+".${info.data.versionCode}"
        tv_update_new_content.text = JsonParser.parseString(info.data.message).asJsonObject.get("message").asString
        tv_update_new_content.movementMethod = ScrollingMovementMethod.getInstance()
        iv_update_new_close.visibility = if(info.data.isForce.toInt() != 1) View.VISIBLE else View.GONE
        setCanceledOnTouchOutside(info.data.isForce.toInt() != 1)
    }

    private fun initEvent(){
        tv_update_new_confirm.setOnClickListener {
            mOnApkDownloadConfirmListener?.onConfirmDownload(info)
        }
        tv_update_new_cancel.setOnClickListener {
            RxToast.warning(context.resources.getString(R.string.update_forbiten))
        }
        iv_update_new_close.setOnClickListener {
            RxToast.success(context.resources.getString(R.string.update_forbiten_1))
            dismiss()
        }
    }

    interface OnApkDownloadConfirmListener{
        fun onConfirmDownload(info :AppUpdateBean)
    }

    private var mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener? = null

    fun setOnApkDownloadConfirmListener(mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener){
        this.mOnApkDownloadConfirmListener = mOnApkDownloadConfirmListener
    }

}