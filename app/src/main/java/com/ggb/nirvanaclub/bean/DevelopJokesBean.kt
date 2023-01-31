package com.ggb.nirvanaclub.bean

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 美女图片
 */
class DevelopJokesBean (
    @SerializedName("code")val code:Int,
    @SerializedName("msg")val msg:String,
    @SerializedName("data")val data:List<DevelopJokesListBean>
): Serializable

class DevelopJokesListBean (
    @SerializedName("info")val info:DevelopJokesInfoBean,
    @SerializedName("joke")val joke:DevelopJokesJokeBean,
    @SerializedName("user")val user:DevelopJokesUserBean
): Serializable

class DevelopJokesInfoBean (
    @SerializedName("commentNum")val commentNum:Int,
    @SerializedName("disLikeNum")val disLikeNum:Int,
    @SerializedName("isAttention")val isAttention:Boolean,
    @SerializedName("isLike")val isLike:Boolean,
    @SerializedName("isUnlike")val isUnlike:Boolean,
    @SerializedName("likeNum")val likeNum:Int,
    @SerializedName("shareNum")val shareNum:Int
): Serializable

class DevelopJokesJokeBean(
    @SerializedName("addTime")val addTime:String,
    @SerializedName("audit_msg")val audit_msg:String,
    @SerializedName("content")val content:String,
    @SerializedName("hot")val hot:Boolean,
    @SerializedName("imageSize")val imageSize:String,
    @SerializedName("imageUrl")val imageUrl:String,
    @SerializedName("jokesId")val jokesId:Int,
    @SerializedName("latitudeLongitude")val latitudeLongitude:String,
    @SerializedName("showAddress")val showAddress:String,
    @SerializedName("thumbUrl")val thumbUrl:String,
    @SerializedName("type")val type:Int,
    @SerializedName("userId")val userId:Int,
    @SerializedName("videoSize")val videoSize:String,
    @SerializedName("videoTime")val videoTime:Int,
    @SerializedName("videoUrl")val videoUrl:String
): Serializable

class DevelopJokesUserBean (
    @SerializedName("avatar")val avatar:String,
    @SerializedName("nickName")val nickName:String,
    @SerializedName("signature")val signature:String,
    @SerializedName("userId")val userId:Int
): Serializable

