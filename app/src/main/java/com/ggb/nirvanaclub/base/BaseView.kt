package com.ggb.nirvanaclub.base

interface BaseView {
    fun onSuccess(flag:String? = "",data:Any?)
    fun onFailed(string: String?="",isRefreshList:Boolean)
    fun onNetError(boolean: Boolean,isRefreshList:Boolean)
}