package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MeOptionBean (
    @SerializedName("title")var title:String,
    @SerializedName("subtitle")var subtitle:String = "",

    @SerializedName("isSelected")var isSelected:Boolean = true
): Serializable