package com.ggb.nirvanaclub.view.dialog

import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.AppUpdateBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.FileReaderUtil
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.activity_crash.*
import org.jetbrains.anko.toast
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

class CrashActivityDialog : Activity() {

    private lateinit var info : AppUpdateBean

    private var StorageState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a = obtainStyledAttributes(R.styleable.AppCompatTheme)
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar)
        }
        a.recycle()
        setContentView(R.layout.activity_crash)
        this.setFinishOnTouchOutside(false)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//            mFilePath = Environment.getExternalStorageDirectory() + "/" + App.ERROR_FILENAME;
            StorageState = true
        } else {
//            Log.i(App.TAG, "哦豁，说好的SD呢...");
        }

//        Thread(upLog).start()
        initView()
        initEvent()
    }

    fun initView() {

        /*向Intent 中取数据***/
        val intent = intent
        val filePath = intent.getSerializableExtra("file_name").toString()
        Log.e("TAG", "传递过来的文件路径是=============》: $filePath")

        tv_crash_path.text = filePath
        val errorData = FileReaderUtil.readTxt(filePath)
        tv_crash_error.text = errorData

        ImageLoaderUtil().displayImage(this,R.mipmap.bear_crazy,iv_crash_bear)
    }

    fun initEvent() {
        tv_cash_exit.setOnClickListener {
            exit()
        }
        tv_cash_restart.setOnClickListener {
            restart()
        }
        tv_cash_copy.setOnClickListener {
            copyErrorToClipboard()
        }
    }

    // 上传错误信息
    var upLog = Runnable {
        try {
            val Mobile = Build.MODEL
            val maxMemory = "" + getmem_TOLAL() / 1024 + "m"
            val nowMemory = "" + getmem_UNUSED(this) / 1024 + "m"
            val eMessage = "未获取到错误信息"
            if (StorageState) {
//                    eMessage = FileTxt.ReadTxt(mFilePath).replace("'", "");
            }
            //                Log.i(App.TAG, "Mobile:" + Mobile + " | maxMemory:" + maxMemory + " |nowMemory:" + nowMemory
//                        + " |eMessage:" + eMessage);
            /**
             * 可以在这调你自己的接口上传信息
             */
            /**
             * 可以在这调你自己的接口上传信息
             */
            /**
             * 可以在这调你自己的接口上传信息
             */
            /**
             * 可以在这调你自己的接口上传信息
             */
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun exit() {
        System.exit(0)
        Process.killProcess(Process.myPid())
    }

    private fun restart() {
        val intent = baseContext.packageManager
            .getLaunchIntentForPackage(baseContext.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        exit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exit()
    }

    //复制错误内容到剪切板
    private fun copyErrorToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        //Are there any devices without clipboard...?
        val clip = ClipData.newPlainText(getString(R.string.crash_error_details_clipboard_label), tv_crash_error.text)
        clipboard.setPrimaryClip(clip)
        toast(R.string.crash_error_details_copied).setGravity(Gravity.CENTER, 0, 0)

    }

    // 获取可用内存
    fun getmem_UNUSED(mContext: Context): Long {
        val MEM_UNUSED: Long
        val am = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        MEM_UNUSED = mi.availMem / 1024
        return MEM_UNUSED
    }

    // 获取剩余内存
    fun getmem_TOLAL(): Long {
        val mTotal: Long
        val path = "/proc/meminfo"
        var content: String? = null
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(path), 8)
            var line: String?
            if (br.readLine().also { line = it } != null) {
                content = line
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        val begin = content!!.indexOf(':')
        val end = content.indexOf('k')
        content = content.substring(begin + 1, end).trim { it <= ' ' }
        mTotal = content.toInt().toLong()
        return mTotal
    }

}