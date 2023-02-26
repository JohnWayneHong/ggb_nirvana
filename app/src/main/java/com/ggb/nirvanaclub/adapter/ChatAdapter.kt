package com.ggb.nirvanaclub.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.content.VideoContent
import cn.jpush.im.android.api.content.VoiceContent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.ChatBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import com.ggb.nirvanaclub.utils.TimeFormat
import com.ggb.nirvanaclub.utils.ViewUtil
import com.ggb.nirvanaclub.view.emojitextview.BubbleImageView
import com.ggb.nirvanaclub.view.emojitextview.EmojiconTextView
import com.ggb.nirvanaclub.view.linktextview.QMUILinkTextView
import com.tencent.open.log.SLog
import java.io.File
import java.text.NumberFormat

class ChatAdapter(data: List<ChatBean>) : BaseMultiItemQuickAdapter<ChatBean, BaseViewHolder>(data) {

    private var mOnLinkClickListener: QMUILinkTextView.OnLinkClickListener? = null

    init {

        addItemType(ChatBean.TEXT_SEND, R.layout.item_chat_text_send)
        addItemType(ChatBean.TEXT_RECEIVE, R.layout.item_chat_text_receive)
        addItemType(ChatBean.IMG_SEND, R.layout.item_chat_img_send)
        addItemType(ChatBean.IMG_RECEIVE, R.layout.item_chat_img_receive)
        addItemType(ChatBean.VOICE_SEND, R.layout.item_chat_voice_send)
        addItemType(ChatBean.VOICE_RECEIVE, R.layout.item_chat_voice_receive)

//        //文件
//        addItemType(ChatBean.FILE_SEND, R.layout.item_chat_file_send)
//        addItemType(ChatBean.FILE_RECEIVE, R.layout.item_chat_file_receive)
//        //红包
//        addItemType(ChatBean.REDP_SEND, R.layout.item_chat_redp_send)
//        addItemType(ChatBean.REDP_RECEIVE, R.layout.item_chat_redp_receive)
//        //位置
//        addItemType(ChatBean.ADDRESS_SEND, R.layout.item_chat_address_send)
//        addItemType(ChatBean.ADDRESS_RECEIVE, R.layout.item_chat_address_receive)
//        //名片
//        addItemType(ChatBean.CARD_SEND, R.layout.item_chat_card_send)
//        addItemType(ChatBean.CARD_RECEIVE, R.layout.item_chat_card_receive)
//
//        addItemType(ChatBean.VIDEO_SEND, R.layout.item_chat_img_send)
//        addItemType(ChatBean.VIDEO_RECEIVE, R.layout.item_chat_img_receive)
//        //群名片
//        addItemType(ChatBean.GROUP_INVITA_SEND, R.layout.item_chat_card_send)
//        addItemType(ChatBean.GROUP_INVITA_RECEIVE, R.layout.item_chat_card_receive)
//
//        addItemType(ChatBean.VIDEO_PHONE_SEND, R.layout.item_chat_text_send)
//        addItemType(ChatBean.VIDEO_PHONE_RECEIVE, R.layout.item_chat_text_receive)
//        //消息撤回
//        addItemType(ChatBean.RETRACT, R.layout.item_chat_retract)
    }

    fun setOnLinkClickListener(mOnLinkClickListener: QMUILinkTextView.OnLinkClickListener) {
        this.mOnLinkClickListener = mOnLinkClickListener
    }

    @SuppressLint("ResourceType", "CutPasteId")
    override fun convert(helper: BaseViewHolder, item: ChatBean) {
        if (item.itemType == ChatBean.RETRACT) {
            return
        }
        if (item.message == null) {
            return
        }
        if (helper.adapterPosition == 0) {
            val timeFormat = TimeFormat(mContext, item.message.createTime)
            helper.setText(R.id.tv_time, timeFormat.detailTime)
            helper.getView<View>(R.id.tv_time).visibility = View.VISIBLE
        } else {
            val oldBean = data[helper.adapterPosition - 1]
            if (oldBean != null && item != null) {
                if (oldBean.message != null && item.message != null) {
                    val oldTime = oldBean.message.createTime
                    val nowTime = item.message.createTime

                    // 如果两条消息之间的间隔超过五分钟则显示时间
                    if (nowTime - oldTime > 300000) {
                        val timeFormat = TimeFormat(mContext, item.message.createTime)
                        helper.setText(R.id.tv_time, timeFormat.detailTime)
                        helper.getView<View>(R.id.tv_time).visibility = View.VISIBLE
                    } else {
                        helper.getView<View>(R.id.tv_time).visibility = View.GONE
                    }
                } else {
                    helper.getView<View>(R.id.tv_time).visibility = View.GONE
                }
            } else {
                helper.getView<View>(R.id.tv_time).visibility = View.GONE
            }
        }
        when (helper.itemViewType) {
            ChatBean.TEXT_SEND, ChatBean.TEXT_RECEIVE -> {
                helper.setText(R.id.etv_chat_tv, (item.message.content as TextContent).text)
                helper.itemView.findViewById<EmojiconTextView>(R.id.etv_chat_tv).setOnLinkClickListener(mOnLinkClickListener)
                helper.itemView.findViewById<EmojiconTextView>(R.id.etv_chat_tv).setNeedForceEventToParent(true)
            }
            ChatBean.IMG_SEND, ChatBean.IMG_RECEIVE -> {
                //设置是否显示文字进度
                helper.getView<BubbleImageView>(R.id.iv).setProgressVisible(false)
                helper.getView<BubbleImageView>(R.id.iv).showShadow(false)

                helper.addOnClickListener(R.id.iv)

                try {
                    //压缩图片工具
                    val convertImage = ImageLoaderUtil.calculaThumhSize(mContext,(item.message.content as ImageContent).width,(item.message.content as ImageContent).height)
                    helper.itemView.findViewById<BubbleImageView>(R.id.iv).layoutParams.width = convertImage.imageWidt.toInt()
                    helper.itemView.findViewById<BubbleImageView>(R.id.iv).layoutParams.height = convertImage.imageHeigh.toInt()
                }catch (e: NullPointerException) {
                    helper.itemView.findViewById<BubbleImageView>(R.id.iv).layoutParams.width =
                        ViewUtil.Dp2px(mContext, mContext.resources.getDimension(R.dimen.dimen_150))
                    helper.itemView.findViewById<BubbleImageView>(R.id.iv).layoutParams.height =
                        ViewUtil.Dp2px(mContext, mContext.resources.getDimension(R.dimen.dimen_150))
                    SLog.e("TAG", "聊天记录的图片转换Exception : " + e.message)
                }

                ImageLoaderUtil().loadImage(mContext as Activity?,((item.message.content as ImageContent).localThumbnailPath),R.mipmap.logo,helper.getView<BubbleImageView>(R.id.iv))
            }
            ChatBean.VIDEO_SEND, ChatBean.VIDEO_RECEIVE -> {
                val videoContent = item.message.content as VideoContent
                val options2 = RequestOptions()
                options2.centerInside()
                    .placeholder(R.color.white)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                videoContent.downloadThumbImage(
                    item.message,
                    object : DownloadCompletionCallback() {
                        override fun onComplete(i: Int, s: String, file: File) {
                            Glide.with(mContext).load(file.path).apply(options2)
                                .into((helper.getView<View>(R.id.iv) as ImageView))
                        }
                    })
            }
            ChatBean.VOICE_RECEIVE, ChatBean.VOICE_SEND -> {
                helper.setText(
                    R.id.tv,
                    (item.message.content as VoiceContent).duration.toString() + " "+" ''"
                )
                val screenWidthPx = mContext.applicationContext.resources.displayMetrics.widthPixels
                val increment = (screenWidthPx / 2 / 30 * (item.message.content as VoiceContent).duration)

                val params: ViewGroup.LayoutParams = helper.getView<LinearLayout>(R.id.ll_content).layoutParams
                params.width = ViewUtil.Dp2px(mContext, 65F) + increment

            }
//            ChatBean.FILE_SEND, ChatBean.FILE_RECEIVE -> {
//                val fileSize = (item.message.content as FileContent).getNumberExtra("fileSize")
//                val size = ChatAdapter.getFileSize(fileSize)
//                helper.setText(R.id.tv_name, (item.message.content as FileContent).fileName)
//                    .setText(R.id.tv_size, size)
//            }
//            ChatBean.REDP_SEND, ChatBean.REDP_RECEIVE -> {}
//            ChatBean.ADDRESS_RECEIVE, ChatBean.ADDRESS_SEND -> {
//                val addressContent = item.message.content as LocationContent
//                val detail = addressContent.address
//                val address = addressContent.getStringExtra("path")
//                if (!TextUtils.isEmpty(address)) helper.setText(R.id.tv, address)
////                if (!TextUtils.isEmpty(detail)) helper.setText(R.id.tv_detail, detail)
//            }
//            ChatBean.CARD_RECEIVE, ChatBean.CARD_SEND -> {}
//            ChatBean.GROUP_INVITA_RECEIVE, ChatBean.GROUP_INVITA_SEND -> {}
//            ChatBean.VIDEO_PHONE_SEND, ChatBean.VIDEO_PHONE_RECEIVE -> {
//                val videoPhoneContent = item.message.content as CustomContent
//                val time = videoPhoneContent.getStringValue(C.DATA)
//                val t = time.toInt()
//                helper.setText(R.id.tv, "语音通话 " + t / 60 + "分" + t % 60 + "秒")
//            }
        }
        helper.addOnClickListener(R.id.iv_head)

        item.message.fromUser.getAvatarBitmap(object : GetAvatarBitmapCallback() {
            override fun gotResult(i: Int, s: String, bitmap: Bitmap) {
                if (i == 0) (helper.getView<View>(R.id.iv_head) as ImageView).setImageBitmap(bitmap)
            }
        })
        if (item.upload) {
            helper.getView<View>(R.id.pb).visibility = View.INVISIBLE
        } else {
            helper.getView<View>(R.id.pb).visibility = View.VISIBLE
        }
    }


    companion object {

        fun getFileSize(fileSize: Number): String {
            val ddf1 = NumberFormat.getNumberInstance()
            //保留小数点后两位
            ddf1.maximumFractionDigits = 2
            val size = fileSize.toDouble()
            val sizeDisplay: String = if (size > 1048576.0) {
                val result = size / 1048576.0
                ddf1.format(result) + " MB"
            } else if (size > 1024) {
                val result = size / 1024
                ddf1.format(result) + " KB"
            } else {
                ddf1.format(size) + " B"
            }
            return sizeDisplay
        }
    }

}