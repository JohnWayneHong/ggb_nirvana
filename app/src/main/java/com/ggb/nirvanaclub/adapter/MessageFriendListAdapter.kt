package com.ggb.nirvanaclub.adapter

import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.model.UserInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R

class MessageFriendListAdapter: BaseQuickAdapter<UserInfo, BaseViewHolder>(R.layout.item_message_friend_list){
    override fun convert(helper: BaseViewHolder, item: UserInfo) {

        item.getAvatarBitmap(object : GetAvatarBitmapCallback() {
            override fun gotResult(i: Int, s: String, bitmap: Bitmap) {
                if (i == 0) {
                    (helper.getView<View>(R.id.cir_message_list_avatar) as ImageView).setImageBitmap(bitmap)
                }
            }
        })

        if (TextUtils.isEmpty(item.nickname)) {
            helper.setText(R.id.tv_message_list_name, item.userName)
        } else {
            helper.setText(R.id.tv_message_list_name, item.nickname)
        }

        helper.addOnClickListener(R.id.tv_message_list_talk)
    }
}