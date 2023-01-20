package com.ggb.nirvanaclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.ggb.nirvanaclub.R
import kotlinx.android.synthetic.main.dialog_reminder_view.*

class ReminderDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_reminder_view)

        initEvent()
    }


    fun showReminder(index:Int){
        show()
        when(index){
            1 -> {
                tv_reminder_text.text = "是否退出登录？"
            }
            2 -> {
                tv_reminder_text.text = "您未设置交易密码，请点击确定设置交易密码后再进行兑换！"
            }
            3 -> {
                tv_reminder_text.text = "是否从购物车移除？"
            }
            4 -> {
                tv_reminder_text.text = "是否取消订单？"
            }
            5 -> {
                tv_reminder_text.text = "是否删除订单？"
            }
            6 -> {
                tv_reminder_text.text = "是否删除该地址？"
            }
            7 -> {
                tv_reminder_text.text = ""
            }
            8 -> {
                tv_reminder_text.text = "是否删除该收款账户？"
            }
        }
    }

    fun showReminder(reminderString:String){
        show()
        tv_reminder_text.text = reminderString
    }

    private fun initEvent(){
        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            dismiss()
            mOnNoticeConfirmListener?.onConfirm()
        }
    }

    interface OnNoticeConfirmListener{
        fun onConfirm()
    }

    private var mOnNoticeConfirmListener : OnNoticeConfirmListener? = null

    fun setOnNoticeConfirmListener(mOnNoticeConfirmListener : OnNoticeConfirmListener){
        this.mOnNoticeConfirmListener = mOnNoticeConfirmListener
    }


}