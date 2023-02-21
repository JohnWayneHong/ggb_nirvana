package com.ggb.nirvanaclub.bean

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OnCodeIsNot200 (
    @SerializedName("code")val code:String,
    @SerializedName("data")val data:JsonObject,
    @SerializedName("message")val message:String,
    @SerializedName("success")val success:Boolean
): Serializable
