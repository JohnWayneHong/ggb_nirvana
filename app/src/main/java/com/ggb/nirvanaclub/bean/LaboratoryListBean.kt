package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LaboratoryListBean(
    @SerializedName("name")val name:String,
    @SerializedName("image")val image:Int
): Serializable