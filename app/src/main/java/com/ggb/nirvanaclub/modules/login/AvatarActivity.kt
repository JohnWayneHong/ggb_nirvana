package com.ggb.nirvanaclub.modules.login

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.tencent.smtt.export.external.interfaces.PermissionRequest
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.fragment_community_webrtc.*
import org.jetbrains.anko.runOnUiThread

/**
 * 腾讯X5测试
 */
class AvatarActivity : BaseActivity() {

    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL)

    override fun getLayoutResource(): Int = R.layout.activity_login_avatar

    override fun initView() {
        initWebView()
    }

    private fun initWebView(){

        //设置JavaScrip
        wv_community_webrtc.settings.setJavaScriptEnabled(true);

        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv_community_webrtc.settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wv_community_webrtc.settings.setSupportMultipleWindows(true);

        wv_community_webrtc.settings.setUseWideViewPort(true);//自适应手机屏幕
        wv_community_webrtc.settings.setAllowContentAccess(true);

        wv_community_webrtc.settings.setDomStorageEnabled(true);
        wv_community_webrtc.settings.setLoadsImagesAutomatically(true);
        wv_community_webrtc.settings.setLoadWithOverviewMode(true);
        wv_community_webrtc.settings.setAppCacheEnabled(true);
        wv_community_webrtc.settings.setBlockNetworkImage(true);
        wv_community_webrtc.settings.setAllowFileAccess(true);

//
        //加载本地html文件
        // mWVmhtml.loadUrl("file:///android_asset/hello.html");
        //加载网络URL
//        wv_community_webrtc.loadUrl("https://nirvana1234.xyz/v2/")
        wv_community_webrtc.loadUrl("https://nirvana1234.xyz/v2/backend/product")
//        //设置在当前WebView继续加载网页
        wv_community_webrtc.webViewClient = MyWebViewClient()
        wv_community_webrtc.webChromeClient = MyWebChromeClient();



    }

    class MyWebViewClient : WebViewClient() {
        //WebView代表是当前的WebView
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url!!)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d("WebView", "开始访问网页")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("WebView", "访问网页结束")
        }

        override fun onReceivedError(webView: WebView?, i: Int, s: String?, s1: String?) {
            Log.d("WebView", "***********onReceivedError***********")
            super.onReceivedError(webView, i, s, s1)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
            handler.proceed() // 等待证书响应
        }
    }

    class MyWebChromeClient : WebChromeClient() {
        //监听加载进度
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }

        //接受网页标题
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            //把当前的Title设置到Activity的title上显示
//            setTitle(title)
        }

        override fun onPermissionRequest(request: PermissionRequest) {

            App.instance.runOnUiThread { request.grant(request.resources) }
        }
    }

    override fun initEvent() {

    }
}