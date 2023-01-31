package com.ggb.nirvanaclub.bean

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 美女图片
 */
class DevelopJokesSubscriptBean (
    @SerializedName("code")val code:Int,
    @SerializedName("msg")val msg:String,
    @SerializedName("data")val data:List<DevelopJokesSubscriptListBean>
): Serializable

class DevelopJokesSubscriptListBean (
    @SerializedName("userId")val userId:Int,
    @SerializedName("nickname")val nickname:String,
    @SerializedName("jokesNum")val jokesNum:String,
    @SerializedName("isAttention")var isAttention:Boolean,
    @SerializedName("fansNum")val fansNum:String,
    @SerializedName("avatar")val avatar:String
): Serializable

