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

class HealthyCodeDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_healthy_code)

        initWindow()
        initEvent()
    }

    private fun initWindow(){
        val lp = window?.attributes
        lp?.height = (ScreenUtils.getScreenWidth(context)*1.3).toInt()
        window?.attributes = lp
    }

    fun showHealthyCode(){
        show()

        setCanceledOnTouchOutside(false)
    }

    private fun initEvent(){
        tv_yes.setOnClickListener {
            mOnHealthyCodeListener?.onConfirm()
            dismiss()
        }
        tv_no.setOnClickListener {
            mOnHealthyCodeListener?.onCancel()
            dismiss()
        }
    }

    interface OnHealthyCodeListener{
        fun onConfirm()
        fun onCancel()
    }

    private var mOnHealthyCodeListener:OnHealthyCodeListener? = null

    fun setOnHealthyCodeListener(mOnHealthyCodeListener:OnHealthyCodeListener){
        this.mOnHealthyCodeListener = mOnHealthyCodeListener
    }

}