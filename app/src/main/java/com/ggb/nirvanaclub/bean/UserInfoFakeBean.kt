package com.ggb.nirvanaclub.bean

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserInfoFakeBean (
    @SerializedName("tagName")val tagName:String,
    @SerializedName("tagId")val tagId:Int,

    @SerializedName("isSelected")var isSelected:Boolean = true
): Serializable

