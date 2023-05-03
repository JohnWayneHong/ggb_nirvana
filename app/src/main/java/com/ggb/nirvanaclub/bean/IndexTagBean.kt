package com.ggb.nirvanaclub.bean

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class IndexTagBean (
//    @SerializedName("tagId")val tagId:String,
    @SerializedName("id")val tagId:String,
//    @SerializedName("tagName")val tagName:String,
    @SerializedName("name")val tagName:String,
    @SerializedName("description")val description:String,
    @SerializedName("hot")val hot:Int,
    @SerializedName("blogs")val blogs:JsonArray,

    @SerializedName("isSelected")var isSelected:Boolean = true
): Serializable

