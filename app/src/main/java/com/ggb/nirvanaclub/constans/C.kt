package com.ggb.nirvanaclub.constans

import com.ggb.nirvanaclub.bean.IndexTagBean

object C {
    var USER_ID = ""
    var USER_DOWNLOAD_URL = ""
    var IS_MUST_UPDATE = false
    var IS_LOGIN = false
    var IS_SIGN_REQUEST = false
    var userTag: ArrayList<IndexTagBean> = arrayListOf<IndexTagBean>()

    fun setUserTag(userTag:List<IndexTagBean>){
        this.userTag.clear()
        this.userTag.addAll(userTag)
    }

    const val SIGN_SECRET_KEY = "suxiang998866"
    const val QQ_LOGIN_APPLY_ID = "102027954"

    const val PUBLIC_PAGER_NUMBER = "10"

    const val HX_HOST_TYPE = 1
    const val HX_BASE_ADDRESS = "https://nirvana1234.xyz/api/android/"
    const val OLD_BASE_ADDRESS = "https://nirvana1234.xyz/api/"

    const val ANDROID_UPDATE_CONFIG_ADDRESS = "https://nirvana1234.xyz/apk/latest"
    const val ANDROID_UPDATE_TIMES_ADDRESS = "https://f.m.suning.com/api/ct.do"
    //const val HX_BASE_ADDRESS = "http://app.suxiang986.com/"

    const val U_MENG_APP_KEY = "5f7197c180455950e49a2143"
    const val A_MAP_API = "e35db695463eff59c125c09c7eb466fd"
    const val BUGLY_APP_ID = "dfb9469c57"
    const val WX_APP_ID = "wxc0d7373e2a591882"
    const val WX_APP_SECRET = "cfb961561f0e1188ed43807b3d987c52"
    const val QQ_APP_IP = "101896563"
    const val QQ_APP_KEY = "7ee5b904496e4b6ca71acc3d60f6a950"


    const val TITLE_CUSTOM = 0
    const val TITLE_NORMAL = 1
    const val TITLE_RIGHT_TEXT = 2
    const val TITLE_RIGHT_TEXT_BACKGROUND = 3

    const val ORDER_ALL = -1
    const val ORDER_NO_PAY = 0
    const val ORDER_NO_SEND = 1
    const val ORDER_NO_RECEIVE = 2
    const val ORDER_RECEIVE_OVER = 3
    const val ORDER_ORDER_COMPLETE = 4
    const val ORDER_ORDER_CANCEL = 13

    const val MARKET_ORDER_STATUS_BUY = 0
    const val MARKET_ORDER_STATUS_SELL = 1

    const val ACTIVITY_RECYCLE_CREATE = 1
    const val ACTIVITY_RECYCLE_START = 2
    const val ACTIVITY_RECYCLE_RESUME = 3
    const val ACTIVITY_RECYCLE_PAUSE = 4
    const val ACTIVITY_RECYCLE_STOP = 5
    const val ACTIVITY_RECYCLE_DESTROY = 6

    /************************************Article****************************************/
    /**
     * 点赞或取消点赞发送请求的 key
     */
    const val ARTICLE_LIKE_ORDER_KEY = "order"
    /**
     * 点赞发送请求的 value
     */
    const val ARTICLE_LIKE_ON = "on"
    /**
     * 取消点赞发送请求的 value
     */
    const val ARTICLE_LIKE_OFF = "off"
}