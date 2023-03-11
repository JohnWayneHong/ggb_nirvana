package com.ggb.nirvanaclub.adapter

import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.content.CustomContent
import cn.jpush.im.android.api.content.PromptContent
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.GroupInfo
import cn.jpush.im.android.api.model.UserInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.MessageChatUtils

class MessageFriendAdapter(data: List<Conversation>): BaseQuickAdapter<Conversation, BaseViewHolder>(R.layout.item_message_main,data){


    override fun convert(helper: BaseViewHolder?, item: Conversation) {
        if (item.targetInfo is UserInfo) {
            val userInfo = item.targetInfo as UserInfo
            userInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                override fun gotResult(i: Int, s: String, bitmap: Bitmap) {
                    if (i == 0) {
                        (helper!!.getView<View>(R.id.civ_message_main_head) as ImageView).setImageBitmap(bitmap)
                    } else {
                        helper!!.setImageResource(R.id.civ_message_main_head, R.mipmap.ic_bear_question)
                    }
                }
            })
            helper!!.setText(R.id.tv_message_main_name, MessageChatUtils.getName(userInfo))
        } else {
            val groupInfo = item.targetInfo as GroupInfo
            groupInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                override fun gotResult(i: Int, s: String, bitmap: Bitmap) {
                    if (i == 0) {
                        (helper!!.getView<View>(R.id.civ_message_main_head) as ImageView).setImageBitmap(bitmap)
                    } else {
                        helper!!.setImageResource(R.id.civ_message_main_head, R.mipmap.ic_bear_question)
                    }
                }
            })
            helper!!.setText(R.id.tv_message_main_name, groupInfo.groupName)
        }


        val lastMsg = item.latestMessage
        if (lastMsg != null) {
            val contentStr: String
            when (lastMsg.contentType) {
                ContentType.image -> contentStr = "[图片]"
                ContentType.voice -> contentStr = "[语音]"
                ContentType.location -> contentStr = "[位置]"
                ContentType.file -> contentStr = "[文件]"
                ContentType.video -> contentStr = "[视频]"
                ContentType.eventNotification -> contentStr = "[群组消息]"
                ContentType.custom -> {
                    val addressContent = lastMsg.content as CustomContent
                    val type = addressContent.getStringValue(C.TYPE)
                    if (TextUtils.isEmpty(type)) {
                        contentStr = ""
                        helper.setText(R.id.tv_message_main_content, contentStr)
                        return
                    }
                    contentStr = if (type == C.RED_PACKEGE) {
                        "[红包]"
                    } else if (type == C.ADDRESS) {
                        "[定位]"
                    } else if (type == C.CARD) {
                        "[个人名片]"
                    } else if (type == C.INVITATION) {
                        "[群邀请]"
                    } else {
                        "[语音通话]"
                    }
                }
                ContentType.prompt -> contentStr = (lastMsg.content as PromptContent).promptText
                else -> contentStr = (lastMsg.content as TextContent).text
            }
            helper.setText(R.id.tv_message_main_content, contentStr)
        }


        if (item.extra == C.NEW_MESSAGE) {
//            helper.getView<View>(R.id.v_message_main_new).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.v_message_main_new).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.v_message_main_new).text = item.unReadMsgCnt.toString()
        } else {
            helper.getView<TextView>(R.id.v_message_main_new).visibility = View.GONE
//            helper.getView<View>(R.id.v_message_main_new).visibility = View.GONE
        }
    }

}