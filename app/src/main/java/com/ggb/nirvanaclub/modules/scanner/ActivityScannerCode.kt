package com.ggb.nirvanaclub.modules.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.rxutils.RxBeepTool.playBeep
import com.ggb.nirvanaclub.utils.rxutils.RxPhotoTool.openLocalImage
import com.ggb.nirvanaclub.utils.rxutils.RxQrBarTool.decodeFromPhoto
import com.ggb.nirvanaclub.utils.rxutils.RxTool.ScaleUpDowm
import com.ggb.nirvanaclub.view.RxToast
import com.ggb.nirvanaclub.view.RxToast.success
import com.ggb.nirvanaclub.view.dialog.ScannerCodeResultDialog
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import kotlinx.android.synthetic.main.activity_scaner_code.*
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author hongweijie
 */
class ActivityScannerCode : BaseActivity() {

    private var inactivityTimer: InactivityTimer? = null

    /**
     * 扫描处理
     */
    private var handler: CaptureActivityHandler? = null

    /**
     * 扫描边界的宽度
     */
    private var mCropWidth = 0

    /**
     * 扫描边界的高度
     */
    private var mCropHeight = 0

    /**
     * 是否有预览
     */
    private var hasSurface = false

    /**
     * 扫描成功后是否震动
     */
    private val vibrate = true

    /**
     * 闪光灯开启状态
     */
    private var mFlashing = true


    /**
     * 扫描结果显示框
     */
    private var scannerDialog: ScannerCodeResultDialog? = null

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun initView() {

//        setTransparentStatusBar(this)
//        setPortrait(this)
        //界面控件初始化
        initDecode()
        //        initView();
        //权限初始化
        initPermission()
        //扫描动画初始化
        initScanerAnimation()
        //初始化 CameraManager
        CameraManager.init(this)
        hasSurface = false
        inactivityTimer = InactivityTimer(this)
    }

    override fun initEvent() {

    }


    private fun initDecode() {
        multiFormatReader = MultiFormatReader()

        // 解码的参数
        val hints = Hashtable<DecodeHintType, Any?>(2)
        // 可以解析的编码类型
        var decodeFormats = Vector<BarcodeFormat?>()
        if (decodeFormats.isEmpty()) {
            decodeFormats = Vector()
            val PRODUCT_FORMATS = Vector<BarcodeFormat?>(5)
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_A)
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_E)
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_13)
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_8)
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            val ONE_D_FORMATS = Vector<BarcodeFormat?>(PRODUCT_FORMATS.size + 4)
            ONE_D_FORMATS.addAll(PRODUCT_FORMATS)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_39)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_93)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_128)
            ONE_D_FORMATS.add(BarcodeFormat.ITF)
            val QR_CODE_FORMATS = Vector<BarcodeFormat?>(1)
            QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE)
            val DATA_MATRIX_FORMATS = Vector<BarcodeFormat?>(1)
            DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX)

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(ONE_D_FORMATS)
            decodeFormats.addAll(QR_CODE_FORMATS)
            decodeFormats.addAll(DATA_MATRIX_FORMATS)
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        multiFormatReader?.setHints(hints)
    }

    override fun onResume() {
        super.onResume()
        val surfaceView = findViewById<SurfaceView>(R.id.capture_preview)
        val surfaceHolder = surfaceView.holder
        if (hasSurface) {
            //Camera初始化
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (!hasSurface) {
                        hasSurface = true
                        initCamera(holder)
                    }
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    hasSurface = false
                }
            })
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
    }

    override fun onPause() {
        super.onPause()
        if (handler != null) {
            handler?.quitSynchronously()
            handler?.removeCallbacksAndMessages(null)
            handler = null
        }
        CameraManager.get().closeDriver()
    }

    override fun onDestroy() {
        inactivityTimer?.shutdown()
        mScanerListener = null
        super.onDestroy()
    }

    private fun initPermission() {
        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    private fun initScanerAnimation() {
        val mQrLineView = findViewById<ImageView>(R.id.capture_scan_line)
        ScaleUpDowm(mQrLineView)
    }

    var cropWidth: Int
        get() = mCropWidth
        set(cropWidth) {
            mCropWidth = cropWidth
            CameraManager.FRAME_WIDTH = mCropWidth
        }

    var cropHeight: Int
        get() = mCropHeight
        set(cropHeight) {
            mCropHeight = cropHeight
            CameraManager.FRAME_HEIGHT = mCropHeight
        }

    fun btn(view: View) {
        when (view.id) {
            R.id.iv_top_mask -> {
                light()
            }
            R.id.top_back -> {
                finish()
            }
            R.id.top_openpicture -> {
                openLocalImage(this)
            }
        }
    }

    private fun light() {
        if (mFlashing) {
            mFlashing = false
            // 开闪光灯
            CameraManager.get().openLight()
        } else {
            mFlashing = true
            // 关闪光灯
            CameraManager.get().offLight()
        }
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder)
            val point = CameraManager.get().cameraResolution
            val width = AtomicInteger(point.y)
            val height = AtomicInteger(point.x)
            val cropWidth1 = rl_capture_crop_layout.width * width.get() / rl_capture_containter.width
            val cropHeight1 = rl_capture_crop_layout.height * height.get() / rl_capture_containter.height
            cropWidth = cropWidth1
            cropHeight = cropHeight1
        } catch (ioe: IOException) {
            return
        } catch (ioe: RuntimeException) {
            return
        }
        if (handler == null) {
            handler = CaptureActivityHandler()
        }
    }

    //--------------------------------------打开本地图片识别二维码 start---------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val resolver = contentResolver
            // 照片的原始资源地址
            val originalUri = data!!.data
            try {
                // 使用ContentProvider通过URI获取原始图片
                val photo = MediaStore.Images.Media.getBitmap(resolver, originalUri)

                // 开始对图像资源解码
                val rawResult = decodeFromPhoto(photo)
                if (rawResult != null) {
                    if (mScanerListener == null) {
                        initDialogResult(rawResult)
                    } else {
                        mScanerListener?.onSuccess("From to Picture", rawResult)
                    }
                } else {
                    if (mScanerListener == null) {
                        RxToast.warning("图片未检测到二维码")

//                        error("图片识别失败.")
                    } else {
                        mScanerListener?.onFail("From to Picture", "图片识别失败")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getLayoutResource() = R.layout.activity_scaner_code

    private fun initDialogResult(result: Result){
        val type = result.barcodeFormat
        val realContent = result.text
        var dialogTitle = ""
        if (scannerDialog == null){
            scannerDialog = ScannerCodeResultDialog(this)
        }
        dialogTitle = if (BarcodeFormat.QR_CODE == type) {
            "二维码扫描结果"
        } else if (BarcodeFormat.EAN_13 == type) {
            "条形码扫描结果"
        } else {
            "扫描结果"
        }
        scannerDialog!!.showScannerCode(realContent,dialogTitle)
//        scannerDialog!!.setSureListener(View.OnClickListener { scannerDialog!!.cancel() })
        scannerDialog!!.setOnCancelListener {
            if (handler != null) {
                // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
                handler!!.sendEmptyMessage(R.id.restart_preview)
            }
        }
        if (!(scannerDialog?.isShowing)!!) {
            scannerDialog?.show()
        }
    }

    //========================================打开本地图片识别二维码 end=================================
//    private fun initDialogResult(result: Result) {
//        val type = result.barcodeFormat
//        val realContent = result.text
//        if (rxDialogSure == null) {
//            //提示弹窗
//            rxDialogSure = RxDialogSure(this)
//        }
//        if (BarcodeFormat.QR_CODE == type) {
//            rxDialogSure!!.setTitle("二维码扫描结果")
//        } else if (BarcodeFormat.EAN_13 == type) {
//            rxDialogSure!!.setTitle("条形码扫描结果")
//        } else {
//            rxDialogSure!!.setTitle("扫描结果")
//        }
//        rxDialogSure!!.setContent(realContent)
//        rxDialogSure!!.setSureListener(View.OnClickListener { rxDialogSure!!.cancel() })
//        rxDialogSure!!.setOnCancelListener {
//            if (handler != null) {
//                // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
//                handler!!.sendEmptyMessage(R.id.restart_preview)
//            }
//        }
//        if (!(rxDialogSure?.isShowing)!!) {
//            rxDialogSure?.show()
//        }
//        putContent(this, RxConstants.SP_SCAN_CODE, (RxDataTool.stringToInt(getContent(this, RxConstants.SP_SCAN_CODE)) + 1).toString())
//    }

    fun handleDecode(result: Result) {
        inactivityTimer!!.onActivity()
        //扫描成功之后的振动与声音提示
        playBeep(this, vibrate)
        val result1 = result.text
        Log.v("二维码/条形码 扫描结果", result1)
        if (mScanerListener == null) {
            success(result1)
            initDialogResult(result)
        } else {
            mScanerListener!!.onSuccess("From to Camera", result)
        }
    }

    //==============================================================================================解析结果 及 后续处理 end
    @SuppressLint("HandlerLeak")
    internal inner class CaptureActivityHandler : Handler() {
        var decodeThread: DecodeThread? = null
        private var state: State
        override fun handleMessage(message: Message) {
            if (message.what == R.id.auto_focus) {
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus)
                }
            } else if (message.what == R.id.restart_preview) {
                restartPreviewAndDecode()
            } else if (message.what == R.id.decode_succeeded) {
                state = State.SUCCESS
                handleDecode(message.obj as Result) // 解析成功，回调
            } else if (message.what == R.id.decode_failed) {
                state = State.PREVIEW
                CameraManager.get().requestPreviewFrame(decodeThread!!.getHandler(), R.id.decode)
            }
        }

        fun quitSynchronously() {
            state = State.DONE
            decodeThread!!.interrupt()
            CameraManager.get().stopPreview()
            val quit = Message.obtain(decodeThread!!.getHandler(), R.id.quit)
            quit.sendToTarget()
            try {
                decodeThread!!.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                removeMessages(R.id.decode_succeeded)
                removeMessages(R.id.decode_failed)
                removeMessages(R.id.decode)
                removeMessages(R.id.auto_focus)
            }
        }

        private fun restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW
                CameraManager.get().requestPreviewFrame(decodeThread?.getHandler(), R.id.decode)
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus)
            }
        }

        init {
            decodeThread = DecodeThread()
            decodeThread?.start()
            state = State.SUCCESS
            CameraManager.get().startPreview()
            restartPreviewAndDecode()
        }
    }

    internal inner class DecodeThread : Thread() {
        private val handlerInitLatch: CountDownLatch = CountDownLatch(1)
        private var handler: Handler? = null
        fun getHandler(): Handler? {
            try {
                handlerInitLatch.await()
            } catch (ie: InterruptedException) {
                // continue?
            }
            return handler
        }

        override fun run() {
            Looper.prepare()
            handler = DecodeHandler()
            handlerInitLatch.countDown()
            Looper.loop()
        }

    }

    @SuppressLint("HandlerLeak")
    internal inner class DecodeHandler : Handler() {
        override fun handleMessage(message: Message) {
            if (message.what == R.id.decode) {
                decode(message.obj as ByteArray, message.arg1, message.arg2)
            } else if (message.what == R.id.quit) {
                Looper.myLooper()?.quit()
            }
        }
    }

    private var multiFormatReader: MultiFormatReader? = null
    private fun decode(data: ByteArray, width: Int, height: Int) {
        var width1 = width
        var height1 = height
        val start = System.currentTimeMillis()
        var rawResult: Result? = null

        //modify here
        val rotatedData = ByteArray(data.size)
        for (y in 0 until height1) {
            for (x in 0 until width1) {
                rotatedData[x * height1 + height1 - y - 1] = data[x + y * width1]
            }
        }
        // Here we are swapping, that's the difference to #11
        val tmp = width1
        width1 = height1
        height1 = tmp
        val source = CameraManager.get().buildLuminanceSource(rotatedData, width1, height1)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            if (bitmap.width > 0 && bitmap.height > 0) {
                rawResult = multiFormatReader?.decodeWithState(bitmap)
            } else {
                multiFormatReader?.reset()
            }
        } catch (e: ReaderException) {
            // continue
        } finally {
            multiFormatReader?.reset()
        }
        if (rawResult != null) {
            val end = System.currentTimeMillis()
            Log.d(ContentValues.TAG, """Found barcode (${end - start} ms):$rawResult""")
            val message = Message.obtain(handler, R.id.decode_succeeded, rawResult)
            val bundle = Bundle()
            bundle.putParcelable("barcode_bitmap", source.renderCroppedGreyscaleBitmap())
            message.data = bundle
            //TLog.d(TAG, "Sending decode succeeded message...");
            message.sendToTarget()
        } else {
            val message = Message.obtain(handler, R.id.decode_failed)
            message.sendToTarget()
        }
    }

    private enum class State {
        //预览
        PREVIEW,  //成功
        SUCCESS,  //完成
        DONE
    }

    companion object {
        /**
         * 扫描结果监听
         */
        private var mScanerListener: OnRxScanerListener? = null

        /**
         * 设置扫描信息回调
         */
        fun setScanerListener(scanerListener: OnRxScanerListener?) {
            mScanerListener = scanerListener
        }
    }
}