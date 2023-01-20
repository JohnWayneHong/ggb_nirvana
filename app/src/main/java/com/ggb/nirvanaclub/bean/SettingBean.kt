package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SettingBean (
    @SerializedName("title")val title:String,
    @SerializedName("childList")val childList:List<SettingListBean>
): Serializable

class SettingListBean(
    @SerializedName("childTitle")val childTitle:String,
    @SerializedName("childSubTitle")val childSubTitle:String = ""
): Serializable