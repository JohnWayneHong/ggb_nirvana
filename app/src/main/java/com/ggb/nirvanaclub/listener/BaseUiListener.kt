package com.ggb.nirvanaclub.listener

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.bean.QQInfo
import com.ggb.nirvanaclub.bean.QQLogin
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.UserStateChangeEvent
import com.ggb.nirvanaclub.modules.login.AvatarActivity
import com.ggb.nirvanaclub.utils.showToast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tencent.connect.UserInfo
import com.tencent.mmkv.MMKV
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

open class BaseUiListener(private val mTencent: Tencent) : DefaultUiListener() {

    private val kv = MMKV.defaultMMKV()

    override fun onComplete(response: Any?) {
        if (response == null) {
            "返回为空,登录失败".showToast()
            return
        }
        val jsonResponse = response as JSONObject
        if (jsonResponse.length() == 0) {
            "返回为空,登录失败".showToast()
            return
        }
        kv.encode("qq_login",response.toString())
        "登录成功".showToast()
        doComplete(response)
        getQQInfo()

//        val intent = Intent(App.context,AvatarActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(App.context, intent, null)
    }

    private fun doComplete(values: JSONObject?) {
        val gson = Gson()
        val qqLogin = gson.fromJson(values.toString(), QQLogin::class.java)
        mTencent.setAccessToken(qqLogin.access_token, qqLogin.expires_in.toString())
        mTencent.openId = qqLogin.openid

    }
    override fun onError(e: UiError) {
        Log.e("TAG", "onError: $e")
    }

    override fun onCancel() {
        "取消登录".showToast()
    }
    private fun getQQInfo(){
        val qqToken = mTencent.qqToken
        val info = UserInfo(App.context,qqToken)
        info.getUserInfo(object :BaseUiListener(mTencent){
            override fun onComplete(response: Any?){
                kv.encode("qq_info",response.toString())
                EventBus.getDefault().post(UserStateChangeEvent(1))
                Log.e("TAG", "登录成功回调的qq_info--->: "+response.toString() )
            }
        })
    }
}