package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 美女图片
 */
class DevelopRandomGirlBean (
    @SerializedName("code")val code:Int,
    @SerializedName("msg")val msg:String,
    @SerializedName("data")val data:List<DevelopRandomGirlListBean>
): Serializable

class DevelopRandomGirlListBean (
    @SerializedName("imageUrl")val imageUrl:String,
    @SerializedName("imageSize")val imageSize:String,
    @SerializedName("imageFileLength")val imageFileLength:String
): Serializable



