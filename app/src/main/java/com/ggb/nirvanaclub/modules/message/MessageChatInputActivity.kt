package com.ggb.nirvanaclub.modules.message

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.modules.keyboard.AppGridLayout
import com.ggb.nirvanaclub.modules.keyboard.ChatKeyBoard
import com.ggb.nirvanaclub.modules.keyboard.EmojiConstants
import com.ggb.nirvanaclub.modules.keyboard.SimpleCommonUtils
import com.ggb.nirvanaclub.modules.keyboard.data.EmoticonEntity
import com.ggb.nirvanaclub.modules.keyboard.interfaces.EmoticonClickListener
import com.ggb.nirvanaclub.modules.keyboard.utils.EmoticonsKeyboardUtils
import com.ggb.nirvanaclub.modules.keyboard.widget.FuncLayout
import com.ggb.nirvanaclub.modules.message.audio2.AudioRecordManager
import com.ggb.nirvanaclub.modules.message.audio2.IAudioRecordListener
import com.ggb.nirvanaclub.utils.GlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sj.emoji.EmojiBean
import com.tamsiree.rxkit.view.RxToast
import com.yanzhenjie.permission.AndPermission
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


abstract class MessageChatInputActivity : BaseActivity() , FuncLayout.OnFuncKeyBoardListener{

    private var ekBar: ChatKeyBoard? = null

    private var audioRecordManager: AudioRecordManager? = null

    private var hasPermissions = false

    protected fun initInputView() {
        ekBar = findViewById(R.id.ek_bar)
        initEmoticonsKeyBoardBar()
        initAudioRecordManager()
        //语音设置
        checkTalkPermission()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar!!.etChat)
        ekBar!!.setAdapter(SimpleCommonUtils.getCommonAdapter(this, emoticonClickListener))
        ekBar!!.addOnFuncKeyBoardListener(this)
        val appGridLayout = AppGridLayout(this)
        appGridLayout.initView(this, View.OnClickListener { view ->
            if (view.tag as Int == 1) {
                //点击了+👌进入相册选图
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .isWeChatStyle(true) //开启R.style.picture_WeChat_style样式
                    .imageEngine(GlideEngine.createGlideEngine())
                    .maxSelectNum(1) // 最大图片选择数量
                    .minSelectNum(1) // 最小选择数量
                    .imageSpanCount(3) // 每行显示个数
                    .selectionMode(PictureConfig.SINGLE) // 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                    .isPreviewImage(true) // 是否可9预览图片
                    .isPreviewVideo(false) // 是否可预览视频
                    .isCamera(true) // 是否显示拍照按钮
                    .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                    .isEnableCrop(false) // 是否裁剪
                    .isCompress(true) // 是否压缩
                    .hideBottomControls(false) // 是否显示uCrop工具栏，默认不显示
                    .isGif(true) // 是否显示gif图片
                    .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
                    .circleDimmedLayer(false) // 是否圆形裁剪
                    .showCropGrid(true) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(true) // 是否开启点击声音
                    .forResult(PictureConfig.CHOOSE_REQUEST) //结果回调onActivityResult code
            } else if (view.tag as Int == 2 || view.tag as Int == 3 || view.tag as Int == 4) {
                //发送音视频，无功能不做处理
                RxToast.info("牛蛙呐暂无开放此功能，敬请期待！")
//                sendCallMessage(if (view.tag as Int == 2) ChatBean.SUBTYPE_CALL_AUDIO else ChatBean.SUBTYPE_CALL_VIDEO)
            }
        })
        ekBar!!.addFuncView(appGridLayout)
        ekBar!!.etChat.setOnSizeChangedListener { w, h, oldw, oldh -> scrollToBottom() }
        ekBar!!.btnSend.setOnClickListener {
            if (!ekBar!!.etChat.text.toString().trim().equals("")) {
                onSendBtnClick(ekBar!!.etChat.text.toString())
                ekBar!!.etChat.setText("")
            }
        }
        ekBar!!.btnVoice.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val perms =
                        arrayOf(Manifest.permission.RECORD_AUDIO)
                    if (hasPermissions) {
                        audioRecordManager!!.startRecord()
                    } else {
                        RxToast.info(resources.getString(R.string.talk_permission))
                    }
                }
                MotionEvent.ACTION_MOVE -> if (isCancelled(v, event)) {
                    audioRecordManager!!.willCancelRecord()
                } else {
                    audioRecordManager!!.continueRecord()
                }
                MotionEvent.ACTION_UP -> {
                    audioRecordManager!!.stopRecord()
                    audioRecordManager!!.destroyRecord()
                }
            }
            false
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPathName String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPathName String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return `true` if and only if the file was copied;
     * `false` otherwise
     */
    fun copyFile(oldPathName: String, newPathName: File): Boolean {
        return try {
            val oldFile = File(oldPathName)
            if (!oldFile.exists()) {
                return false
            } else if (!oldFile.isFile) {
                return false
            } else if (!oldFile.canRead()) {
                return false
            }
            val fileInputStream = FileInputStream(oldPathName)
            val fileOutputStream = FileOutputStream(newPathName)
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (-1 != fileInputStream.read(buffer).also { byteRead = it }) {
                fileOutputStream.write(buffer, 0, byteRead)
            }
            fileInputStream.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (EmoticonsKeyboardUtils.isFullScreen(this)) {
            val isConsum = ekBar!!.dispatchKeyEventInFullScreen(event)
            return if (isConsum) isConsum else super.dispatchKeyEvent(event)
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onPause() {
        super.onPause()
        ekBar!!.reset()
    }

    protected fun initAudioRecordManager() {
        audioRecordManager = AudioRecordManager(this)
        audioRecordManager!!.maxVoiceDuration = AudioRecordManager.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND
        audioRecordManager!!.setAudioSavePath(null)
        audioRecordManager!!.audioRecordListener = object : IAudioRecordListener {
            private var mTimerTV: TextView? = null
            private var mStateTV: TextView? = null
            private var mStateIV: ImageView? = null
            private var mRecordWindow: PopupWindow? = null
            override fun initTipView() {
                Log.e("TAG","initTipView")
                val view = View.inflate(this@MessageChatInputActivity, R.layout.popup_audio_wi_vo, null)
                mStateIV = view.findViewById<View>(R.id.rc_audio_state_image) as ImageView
                mStateTV = view.findViewById<View>(R.id.rc_audio_state_text) as TextView
                mTimerTV = view.findViewById<View>(R.id.rc_audio_timer) as TextView
                mRecordWindow = PopupWindow(view, -1, -1)
                mRecordWindow!!.showAtLocation(findViewById(R.id.root_rl), 17, 0, 0)
                mRecordWindow!!.isFocusable = true
                mRecordWindow!!.isOutsideTouchable = false
                mRecordWindow!!.isTouchable = false
            }

            override fun setTimeoutTipView(counter: Int) {
                if (mRecordWindow != null) {
                    mStateIV!!.visibility = View.GONE
                    mStateTV!!.visibility = View.VISIBLE
                    mStateTV!!.text = "手指上滑，取消发送"
                    mStateTV!!.setBackgroundResource(R.drawable.bg_voice_popup)
                    mTimerTV!!.text = String.format("%s", *arrayOf<Any>(Integer.valueOf(counter)))
                    mTimerTV!!.visibility = View.VISIBLE
                }
            }

            override fun setRecordingTipView() {
                if (mRecordWindow != null) {
                    mStateIV!!.visibility = View.VISIBLE
                    mStateIV!!.setImageResource(R.drawable.ic_volume_1)
                    mStateTV!!.visibility = View.VISIBLE
                    mStateTV!!.text = "手指上滑，取消发送"
                    mStateTV!!.setBackgroundResource(R.drawable.bg_voice_popup)
                    mTimerTV!!.visibility = View.GONE
                }
            }

            override fun setAudioShortTipView() {
                if (mRecordWindow != null) {
                    mStateIV!!.setImageResource(R.drawable.ic_volume_wraning)
                    mStateTV!!.text = "录音时间太短"
                }
            }

            override fun setCancelTipView() {
                if (mRecordWindow != null) {
                    mTimerTV!!.visibility = View.GONE
                    mStateIV!!.visibility = View.VISIBLE
                    mStateIV!!.setImageResource(R.drawable.ic_volume_cancel)
                    mStateTV!!.visibility = View.VISIBLE
                    mStateTV!!.text = "松开手指，取消发送"
                    mStateTV!!.setBackgroundResource(R.drawable.bg_main_full_2)
                }
            }

            override fun destroyTipView() {
                if (mRecordWindow != null) {
                    mRecordWindow!!.dismiss()
                    mRecordWindow = null
                    mStateIV = null
                    mStateTV = null
                    mTimerTV = null
                }
            }

            override fun onStartRecord() {}
            override fun onFinish(audioPath: Uri, duration: Int) {
                //发送文件
                val file = File(audioPath.path)
                if (file.exists()) {
                    val extendmap = HashMap<String, String>()
                    extendmap["duration"] = duration.toString()
                    extendmap["hadplay"] = "1"

                    //TODO 发送语言消息
                    sendAudioMessage(file, duration)
//                    sendAudioMessage(file.name, JsonUtil.getJson(extendmap), audioPath.path)
                }
            }

            override fun onAudioDBChanged(db: Int) {
                when (db / 5) {
                    0 -> mStateIV!!.setImageResource(R.drawable.ic_volume_1)
                    1 -> mStateIV!!.setImageResource(R.drawable.ic_volume_2)
                    2 -> mStateIV!!.setImageResource(R.drawable.ic_volume_3)
                    3 -> mStateIV!!.setImageResource(R.drawable.ic_volume_4)
                    4 -> mStateIV!!.setImageResource(R.drawable.ic_volume_5)
                    5 -> mStateIV!!.setImageResource(R.drawable.ic_volume_6)
                    6 -> mStateIV!!.setImageResource(R.drawable.ic_volume_7)
                    else -> mStateIV!!.setImageResource(R.drawable.ic_volume_8)
                }
            }
        }
    }

    protected abstract fun scrollToBottom()

    protected abstract fun onSendImage(image: String?)

//    protected abstract fun sendAudioMessage(filename: String?, extend: String?, sandboxFilePath: String?)
    protected abstract fun sendAudioMessage(filename: File, duration: Int)

    protected abstract fun sendCallMessage(callType: Int)

    protected abstract fun onSendBtnClick(content: String)

    private fun isCancelled(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return event.rawX < location[0] || event.rawX > location[0] + view.width || event.rawY < location[1] - 40
    }

    private var emoticonClickListener: EmoticonClickListener<Any> = object : EmoticonClickListener<Any> {
        override fun onEmoticonClick(o: Any?, actionType: Int, isDelBtn: Boolean) {
            if (isDelBtn) {
                SimpleCommonUtils.delClick(ekBar!!.etChat)
            } else {
                if (o == null) {
                    return
                }
                if (actionType == EmojiConstants.EMOTICON_CLICK_BIGIMAGE) {
                    if (o is EmoticonEntity) {
//                        onSendImage(o.iconUri)
                    }
                } else {
                    var content: String? = null
                    if (o is EmojiBean) {
                        content = o.emoji
                    } else if (o is EmoticonEntity) {
                        content = o.content
                    }
                    if (TextUtils.isEmpty(content)) {
                        return
                    }
                    val index: Int = ekBar!!.getEtChat().getSelectionStart()
                    val editable: Editable? = ekBar!!.getEtChat().getText()
                    editable?.insert(index, content)
                }
            }
        }
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

    override fun OnFuncPop(height: Int) {
        scrollToBottom()
    }

    override fun OnFuncClose() {
        
    }

}