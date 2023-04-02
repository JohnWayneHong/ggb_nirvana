package com.ggb.nirvanaclub.modules.community

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.DevelopJokesListBean
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread
import com.tencent.smtt.export.external.interfaces.PermissionRequest
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.fragment_community_webrtc.*

class CommunityWebRtcFragment : BaseFragment(), GGBContract.View{

    private var present:GGBPresent?=null

    override fun getLayoutResource() = R.layout.fragment_community_webrtc

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        initWebView()
        initEvent()
    }

    private fun initEvent(){
//        wv_community_webrtc.loadUrl("https://nirvana1234.xyz/v2/")
    }

    private fun initWebView(){

        //设置JavaScrip
        wv_community_webrtc.settings.setJavaScriptEnabled(true);

        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv_community_webrtc.settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
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

    internal class MyWebViewClient : WebViewClient() {
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

    internal class MyWebChromeClient : WebChromeClient() {
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
            runOnUiThread(Runnable { request.grant(request.resources) })
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        fun newInstance(): CommunityWebRtcFragment {
            val fragment = CommunityWebRtcFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETJOKESTEXT -> {
                    data?.let {
                        data as List<DevelopJokesListBean>

                    }
                }
                else -> {

                }

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }

}