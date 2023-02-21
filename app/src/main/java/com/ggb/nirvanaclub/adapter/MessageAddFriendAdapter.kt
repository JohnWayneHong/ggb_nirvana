package com.ggb.nirvanaclub.adapter

import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.event.ContactNotifyEvent
import cn.jpush.im.android.api.model.UserInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R

class MessageAddFriendAdapter: BaseQuickAdapter<ContactNotifyEvent, BaseViewHolder>(R.layout.item_message_add_friend){
    override fun convert(helper: BaseViewHolder?, item: ContactNotifyEvent) {
        helper?.getView<TextView>(R.id.tv_message_add_name)?.text = item.fromUsername
        helper?.getView<TextView>(R.id.tv_message_add_comment)?.text = item.reason

        JMessageClient.getUserInfo(item.fromUsername, object : GetUserInfoCallback() {
            override fun gotResult(i: Int, s: String, userInfo: UserInfo) {
                if (i == 0) {
                    userInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                        override fun gotResult(i: Int, s: String, bitmap: Bitmap) {
                            if (i == 0) {
                                (helper!!.getView<View>(R.id.cir_message_add_avatar) as ImageView).setImageBitmap(bitmap)
                            }
                        }
                    })
                    if (TextUtils.isEmpty(userInfo.nickname)) {
                        helper!!.setText(R.id.tv_message_add_name, userInfo.userName)
                    } else {
                        helper!!.setText(R.id.tv_message_add_name, userInfo.nickname)
                    }
                }
            }
        })

        helper?.addOnClickListener(R.id.tv_message_add_confirm)
    }
}