package com.ggb.nirvanaclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.AppUpdateBean
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.utils.RegularUtil
import com.ggb.nirvanaclub.utils.ScreenUtils
import com.ggb.nirvanaclub.view.RxToast
import com.tamsiree.rxui.view.dialog.RxDialogEditSureCancel
import kotlinx.android.synthetic.main.dialog_healthy_code.*
import kotlinx.android.synthetic.main.dialog_new_apk_update.*
import kotlinx.android.synthetic.main.dialog_scanner_code.*
import org.litepal.LitePal

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

        //设置扫一扫加好友
        if (realContent.startsWith("Nirvana:")){

            val user = LitePal.findLast(UserBean::class.java)
            JMessageClient.getUserInfo(RegularUtil.truncateHeadString(realContent,8), object : GetUserInfoCallback() {
                override fun gotResult(
                    responseCode: Int,
                    responseMessage: String,
                    info: UserInfo
                ) {
                    if (responseCode == 0) {
                        if (info.isFriend) {
                            RxToast.info(context, "已经是好友！", Toast.LENGTH_SHORT)?.show()
                            tv_title.text = "你们已经是好友啦！！"
                            tv_content.text = "快去通讯录里好好找找！"
                        }else{
                            ContactManager.sendInvitationRequest(info.userName,
                                "b0935a121eb9326d4cafaad3",
                                "我是${user.userName}", object : BasicCallback() {
                                    override fun gotResult(p0: Int, p1: String?) {
                                        if (p0 == 0) {
                                            RxToast.success(context, "申请发送成功！", Toast.LENGTH_SHORT)?.show()
                                            tv_title.text = "好友请求已发送！"
                                            tv_content.text = "请等待对方同意！"
                                        }
                                        Log.v("TAG", p0.toString() + p1)
                                    }
                                })
                        }
                    }
                }
            })
        }else{
            tv_title.text = dialogTitle
            tv_content.text = realContent

        }
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