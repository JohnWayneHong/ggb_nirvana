package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NewsListBean(
    @SerializedName("id")val id:String,
    @SerializedName("title")val title:String,
    @SerializedName("summary")val summary:String,
    @SerializedName("img")val img:String,
    @SerializedName("content")val content:String
): Serializable