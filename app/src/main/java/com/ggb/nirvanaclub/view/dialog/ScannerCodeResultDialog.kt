package com.ggb.nirvanaclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.AppUpdateBean
import com.ggb.nirvanaclub.utils.ScreenUtils
import kotlinx.android.synthetic.main.dialog_healthy_code.*
import kotlinx.android.synthetic.main.dialog_new_apk_update.*
import kotlinx.android.synthetic.main.dialog_scanner_code.*

class ScannerCodeResultDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_scanner_code)

        initWindow()
        initEvent()
    }

    private fun initWindow(){
        val lp = window?.attributes
        lp?.height = (ScreenUtils.getScreenWidth(context)*1.3).toInt()
        window?.attributes = lp
    }

    fun showScannerCode(realContent: String, dialogTitle: String) {
        show()

        tv_title.text = dialogTitle
        tv_content.text = realContent
        setCanceledOnTouchOutside(false)
    }

    private fun initEvent(){
        tv_sure.setOnClickListener {
            mOnHealthyCodeListener?.onConfirm()
            cancel()
//            dismiss()
        }
    }

    fun setSureListener(listener: View.OnClickListener?) {
        tv_sure.setOnClickListener(listener)
    }

    interface OnHealthyCodeListener{
        fun onConfirm()
    }

    private var mOnHealthyCodeListener:OnHealthyCodeListener? = null

    fun setOnHealthyCodeListener(mOnHealthyCodeListener:OnHealthyCodeListener){
        this.mOnHealthyCodeListener = mOnHealthyCodeListener
    }

}