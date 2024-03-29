package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AppUpdateBean (
        @SerializedName("success")val success:Boolean,
        @SerializedName("code")val code:Int,
        @SerializedName("message")val message:String,
        @SerializedName("data")val data:AppUpdateListBean
): Serializable

class AppUpdateListBean(
//        @SerializedName("versionId")val versionId:String,
        @SerializedName("id")val id:String,
        @SerializedName("versionName")val versionName:String,
        @SerializedName("downloadUrl")val downloadUrl:String,
        @SerializedName("publishDate")val publishDate:String,
        @SerializedName("versionCode")val versionCode:String,
        @SerializedName("isForce")val isForce:String,
        @SerializedName("message")val message:String
): Serializable