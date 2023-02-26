package com.ggb.nirvanaclub.modules.message

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import cc.shinichi.library.ImagePreview
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.content.*
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.enums.ConversationType
import cn.jpush.im.android.api.enums.MessageDirect
import cn.jpush.im.android.api.enums.MessageStatus
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.event.MessageRetractEvent
import cn.jpush.im.android.api.event.OfflineMessageEvent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.GroupInfo
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ChatAdapter
import com.ggb.nirvanaclub.bean.ChatBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.modules.message.audio2.AudioPlayManager
import com.ggb.nirvanaclub.modules.message.audio2.IAudioPlayListener
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.view.linktextview.QMUILinkTextView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast
import java.io.File


class MessageChatActivity : MessageChatInputActivity(), GGBContract.View{

    private var type = C.SINGLE//0为单聊，1为群聊

    private var userName = ""
    private var nickname = ""

    private var groupId:Long = 0
    private var groupName = ""

    private var chatList : MutableList<ChatBean> = mutableListOf()

    var mAdapter: ChatAdapter = ChatAdapter(chatList)

    private var conversation: Conversation?=null

    private var audioPlayManager: AudioPlayManager? = null

    private var hasPermissions = false

    private lateinit var present: GGBPresent

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_chat

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    private var handler: Handler = @SuppressLint("HandlerLeak") object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when(msg.what){
                C.SCROLL_BOTTOM->{
                    rcy_chat.scrollToPosition(chatList.size-1)
                }

                C.HIDEN_BOTTOM->{

                }

                C.SHOW_BOTTOM->{

                }
            }
        }
    }

    override fun initView() {
        type = intent.getIntExtra(C.TYPE,C.SINGLE)
        if(type==C.SINGLE){
            userName= intent.getStringExtra(C.DATA).toString()
            nickname=intent.getStringExtra(C.DATA_TWO).toString()
            tv_chat_username.text = nickname
        }
        if(type==C.GROUP){
            groupId=intent.getLongExtra(C.DATA,0)
            groupName=intent.getStringExtra(C.DATA_TWO).toString()
            tv_chat_username.text = groupName
        }

        audioPlayManager = AudioPlayManager()

        initTop()
        initChatList()
        initData()

        //语音设置
        checkTalkPermission()
        initInputView()
        mAdapter.setOnLinkClickListener(mOnLinkClickListener)
    }

    private fun initTop(){
        //聊天页面-去向清除聊天记录-群聊信息
        iv_message_chat.setOnClickListener {

        }
    }

    private fun initChatList(){
        rcy_chat.adapter = mAdapter
        rcy_chat.layoutManager = LinearLayoutManager(this)

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            //这里是拦截点击自己的头像
//            if (chatList[position].message.direct == MessageDirect.send){
//                return@setOnItemChildClickListener
//            }
            when(view.id){
                R.id.iv_head->{
                    //TODO 聊天记录点击用户头像 进入详情页面
                    if (type == C.SINGLE){
//                var myIntent = Intent(this,UserDetailActivity::class.java)
//                myIntent.putExtra(C.DATA,userName)
//                startActivity(myIntent)
                    }else if(type==C.GROUP){
//                var user = chatList[position].message.fromUser
//                var myIntent = Intent(this,UserDetailActivity::class.java)
//                myIntent.putExtra(C.DATA,user.userName)
//                startActivity(myIntent)
                    }
                }
                R.id.iv->{
                    //点击图片
                    if (chatList[position].itemType == ChatBean.IMG_SEND || chatList[position].itemType == ChatBean.IMG_RECEIVE){
                        val first=  (rcy_chat.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val last=  (rcy_chat.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                        if(position<first||position>last){
                            return@setOnItemChildClickListener
                        }
                        val imageContract = chatList[position].message.content as ImageContent

                        var path = ""
                        if(!TextUtils.isEmpty(imageContract.localThumbnailPath)){
                            path = imageContract.localThumbnailPath
                        }

                        imageContract.downloadOriginImage(chatList[position].message,object : DownloadCompletionCallback(){
                            override fun onComplete(p0: Int, p1: String?, p2: File) {
                                if (p0 == 0){
                                    ImagePreview.instance
                                        .setContext(this@MessageChatActivity)
                                        .setIndex(0)
                                        .setCloseIconResId(R.drawable.ic_action_close)
                                        .setZoomTransitionDuration(600)
                                        .setImage(p2.path)
                                        .setEnableDragClose(true)
                                        .setEnableClickClose(true)
                                        .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                                        .start()
                                }else{
                                    ImagePreview.instance
                                        .setContext(this@MessageChatActivity)
                                        .setIndex(0)
                                        .setZoomTransitionDuration(600)
                                        .setImage(path)
                                        .setEnableDragClose(true)
                                        .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                                        .start()
                                }
                            }
                        })
                    }
                }
            }

        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            //TODO 视频，文件，后续处理

            //播放录音
            if(chatList[position].itemType==ChatBean.VOICE_SEND|| chatList[position].itemType==ChatBean.VOICE_RECEIVE){
//                playVoiceUtil?.playVoice(chatList,position)

                //已经下载语音文件
                val ivAudio = view.findViewById<ImageView>(R.id.iv_voice)
                val message: cn.jpush.im.android.api.model.Message = chatList[position].message
                val vc = message.content as VoiceContent

                audioPlayManager?.startPlay(this,
                    Uri.parse(vc.localPath), object : IAudioPlayListener {
                        override fun onStart(var1: Uri?) {
                            if (ivAudio != null && ivAudio.background is AnimationDrawable) {
                                val animation = ivAudio.background as AnimationDrawable
                                animation.start()
                            }
                        }

                        override fun onStop(var1: Uri?) {
                            if (ivAudio != null && ivAudio.background is AnimationDrawable) {
                                val animation = ivAudio.background as AnimationDrawable
                                animation.stop()
                                animation.selectDrawable(0)
                            }
                        }

                        override fun onComplete(var1: Uri?) {
                            if (ivAudio != null && ivAudio.background is AnimationDrawable) {
                                val animation = ivAudio.background as AnimationDrawable
                                animation.stop()
                                animation.selectDrawable(0)
                            }
                        }
                    })

            }
        }
    }

    private fun initData(){
        //进入会话
        if(type==C.SINGLE){
            conversation= JMessageClient.getSingleConversation(userName)
            if(conversation==null){
                conversation= Conversation.createSingleConversation(userName)
            }
        }else if(type==C.GROUP){
            conversation= JMessageClient.getGroupConversation(groupId)
            if(conversation==null){
                conversation= Conversation.createGroupConversation(groupId)
            }
        }

        //获取通话记录
        if(conversation?.allMessage!=null){
            for( bean in  conversation?.allMessage!!.toMutableList()){
                addMessage(bean)
            }
        }
    }

    private fun sendTextMessage(text:String){
        if(!TextUtils.isEmpty(text) ){
            val textContent= TextContent(text)
            val m=conversation?.createSendMessage(textContent)
            val bean =ChatBean(m,ChatBean.TEXT_SEND)
            bean.upload=false
            chatList.add(bean)

            mAdapter.notifyItemInserted(chatList.size-1)
            // adapter_chat?.notifyDataSetChanged()

            val now=chatList.size

            handler.sendEmptyMessageDelayed(C.SCROLL_BOTTOM,100)

            m?.setOnSendCompleteCallback(object : BasicCallback(){
                override fun gotResult(p0: Int, p1: String?) {
                    if(p0!=0){
                        return
                    }
                    chatList.get(now-1).upload=true
                    mAdapter.notifyItemChanged(chatList.size-1)
                    // adapter_chat?.notifyDataSetChanged()
                }

            })
            JMessageClient.sendMessage(m)
        }
    }

    //消息加入和刷新界面
    fun addMessage(message: cn.jpush.im.android.api.model.Message){

        if(message.status == MessageStatus.send_fail){
            return
        }

        if(message.contentType == ContentType.eventNotification){
            return
        }

        if(message.targetType == ConversationType.single){
            val userInfo = message.targetInfo as UserInfo
            val targetId = userInfo.userName
            if(!targetId.equals(userName)){
                return
            }
        }

        if(message.targetType == ConversationType.group){
            val groupInfo = message.targetInfo as GroupInfo
            val targetId = groupInfo.groupID
            if(groupId!=targetId){
                return
            }
        }

        if(message.content is PromptContent){
            chatList.add(ChatBean(message ,ChatBean.RETRACT))
            mAdapter.notifyItemInserted(chatList.size-1)
            //  adapter_chat?.notifyDataSetChanged()
            handler.sendEmptyMessageDelayed(C.SCROLL_BOTTOM,100)

            return
        }

        when(message.contentType){

            ContentType.text->{
                if(message.direct== MessageDirect.send){
                    chatList.add(ChatBean(message ,ChatBean.TEXT_SEND))
                }else{
                    chatList.add(ChatBean(message,ChatBean.TEXT_RECEIVE))
                }
            }
            ContentType.image->{

                if(message.direct== MessageDirect.send){
                    chatList.add(ChatBean(message,ChatBean.IMG_SEND))
                }else{
                    chatList.add(ChatBean(message,ChatBean.IMG_RECEIVE))
                }
            }
            ContentType.video->{

                if(message.direct== MessageDirect.send){
                    chatList.add(ChatBean(message,ChatBean.VIDEO_SEND))
                }else{
                    chatList.add(ChatBean(message,ChatBean.VIDEO_RECEIVE))
                }
            }

            ContentType.voice->{
                if(message.direct== MessageDirect.send){
                    chatList.add(ChatBean(message,ChatBean.VOICE_SEND))
                }else{
                    chatList.add(ChatBean(message,ChatBean.VOICE_RECEIVE))
                }
            }
            ContentType.file->{
                if(message.direct== MessageDirect.send){
                    chatList.add(ChatBean(message,ChatBean.FILE_SEND))
                }else{
                    chatList.add(ChatBean(message,ChatBean.FILE_RECEIVE))
                }
            }

            ContentType.location->{
                if(message.direct== MessageDirect.send){
                    chatList.add(ChatBean(message,ChatBean.ADDRESS_SEND))
                }else{
                    chatList.add(ChatBean(message,ChatBean.ADDRESS_RECEIVE))
                }
            }
            ContentType.custom->{

                var type = (message.content as CustomContent).getStringValue(C.TYPE)
                if(TextUtils.isEmpty(type)){
                    return
                }

                if(type.equals(C.RED_PACKEGE)){
                    if(message.direct== MessageDirect.send){
                        chatList.add(ChatBean(message,ChatBean.REDP_SEND))
                    }else{
                        chatList.add(ChatBean(message,ChatBean.REDP_RECEIVE))
                    }
                }else if(type.equals(C.CARD)){
                    if(message.direct== MessageDirect.send){
                        chatList.add(ChatBean(message,ChatBean.CARD_SEND))
                    }else{
                        chatList.add(ChatBean(message,ChatBean.CARD_RECEIVE))
                    }
                }else if(type.equals(C.INVITATION)){
                    if(message.direct== MessageDirect.send){
                        chatList.add(ChatBean(message,ChatBean.GROUP_INVITA_SEND))
                    }else{
                        chatList.add(ChatBean(message,ChatBean.GROUP_INVITA_RECEIVE))
                    }
                }else if(type.equals(C.VIDEO_PHONE)){
                    if(message.direct== MessageDirect.send){
                        chatList.add(ChatBean(message,ChatBean.VIDEO_PHONE_SEND))
                    }else{
                        chatList.add(ChatBean(message,ChatBean.VIDEO_PHONE_RECEIVE))
                    }
                }


            }
        }

        mAdapter.notifyItemInserted(chatList.size-1)
        //  adapter_chat?.notifyDataSetChanged()
        handler.sendEmptyMessageDelayed(C.SCROLL_BOTTOM,100)
    }

    override fun scrollToBottom() {
        handler.sendEmptyMessageDelayed(C.SCROLL_BOTTOM,100)
//        rcy_chat.post(Runnable { rcy_chat.scrollToPosition(rcy_chat.bottom) })
    }

    override fun onSendImage(image: String?) {
        TODO("Not yet implemented")

    }

    override fun sendAudioMessage(filename: File, duration: Int) {
        //发送语音
        val content = VoiceContent(filename, duration)

        if (conversation == null) {
            return
        }else{
            val msg: cn.jpush.im.android.api.model.Message? = conversation!!.createSendMessage(content)
            JMessageClient.sendMessage(msg)
            if (msg==null){
                return
            }
            addMessage(msg)
            val now=chatList.size
            chatList[now-1].upload=false
            mAdapter.notifyItemChanged(now-1)

            msg.setOnSendCompleteCallback(object :BasicCallback(){
                override fun gotResult(p0: Int, p1: String?) {
                    if(p0!=0){
                        return
                    }
                    chatList[now-1].upload=true
                    mAdapter.notifyItemChanged(now-1)
                }
            })
        }

    }

    override fun sendCallMessage(callType: Int) {
        TODO("Not yet implemented")
    }

    override fun onSendBtnClick(content: String) {
        //发送文字或者表情包
        sendTextMessage(content)
    }

    private val mOnLinkClickListener: QMUILinkTextView.OnLinkClickListener = object : QMUILinkTextView.OnLinkClickListener {
        override fun onTelLinkClick(phoneNumber: String?) {
            val dialog = MessageTextDialog(this@MessageChatActivity)
            dialog.initPhoneView(this@MessageChatActivity, phoneNumber)
            dialog.show()
        }

        override fun onMailLinkClick(mailAddress: String?) {
            val dialog = MessageTextDialog(this@MessageChatActivity)
            dialog.initEmailView(this@MessageChatActivity, mailAddress)
            dialog.show()
        }

        override fun onWebUrlLinkClick(url: String?) {
            val dialog = MessageTextDialog(this@MessageChatActivity)
            dialog.initWebView(this@MessageChatActivity, url)
            dialog.show()
        }
    }


    override fun initEvent() {

    }

    /**
     * 打开麦克风权限，不然聊天没法使用
     */
    @SuppressLint("MissingPermission")
    private fun checkTalkPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.RECORD_AUDIO)
            .onGranted { permissions ->
                hasPermissions = true
            }
            .onDenied { permissions ->
                toast(R.string.talk_permission).setGravity(Gravity.CENTER, 0, 0)
            }
            .start()
    }


    override fun onResume() {
        super.onResume()
        JMessageClient.enterSingleConversation(userName)

        if(type == C.SINGLE){
            JMessageClient.enterSingleConversation(userName)
        }
    }

    override fun onPause() {
        super.onPause()
        JMessageClient.exitConversation()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            PictureConfig.CHOOSE_REQUEST->{

                val selectList = PictureSelector.obtainMultipleResult(data)
                Log.e("TAG", "选择图片的路径是==========》: "+selectList[0].compressPath )

                ImageContent.createImageContentAsync( File(selectList[0].compressPath), object : ImageContent.CreateImageContentCallback() {
                    override fun gotResult(responseCode: Int, responseMessage: String, imageContent: ImageContent) {
                        if (responseCode == 0) {
                            imageContent.setStringExtra(C.TYPE,"IMG")
                            val msg = conversation?.createSendMessage(imageContent)

                            sendMessage(msg!!,ChatBean.IMG_SEND)
                        }
                    }
                })
            }
        }
    }

    private  fun addMessageRefresh(chatbean: ChatBean) {
        chatList.add(chatbean)
        mAdapter.notifyItemInserted(chatList.size-1)
        handler.sendEmptyMessageDelayed(C.SCROLL_BOTTOM,100)
    }

    fun sendMessage(msg: cn.jpush.im.android.api.model.Message, type:Int){
        val bean = ChatBean(msg, type)
        bean.upload=false
        addMessageRefresh(bean)

        val now=chatList.size

        JMessageClient.sendMessage(msg)
        msg.setOnSendCompleteCallback(object : BasicCallback(){
            override fun gotResult(p0: Int, p1: String?) {
                if(p0!=0){
                    return
                }
                chatList.get(now-1).upload=true
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }

    //接受了在线信息
    public fun onEventMainThread(event: MessageEvent) {
        addMessage(event.message)
    }

    //离线消息
    public fun onEvent(event: OfflineMessageEvent) {
        for(bean in event.offlineMessageList){
            addMessage(bean)
        }
    }
    //消息被对方撤回通知事件
    public fun onEvent(event: MessageRetractEvent) {
        for(bean in chatList){
            if(event.retractedMessage.id == bean.message.id){
                bean.itemType = ChatBean.RETRACT
                mAdapter.notifyItemChanged(chatList.indexOf(bean))
            }
        }
    }

}