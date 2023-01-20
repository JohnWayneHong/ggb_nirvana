package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HealthyBean (
    @SerializedName("api")val api:String,
    @SerializedName("code")val code:String,
    @SerializedName("currentTime")val currentTime:Long,
    @SerializedName("msg")val msg:String,
): Serializable