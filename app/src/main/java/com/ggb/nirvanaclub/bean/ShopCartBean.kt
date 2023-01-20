package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ShopCartBean(
    @SerializedName("id")val id:String,
    @SerializedName("constitute")val constitute:String,
    @SerializedName("constituteId")val constituteId:String,
    @SerializedName("goodsId")val goodsId:String,
    @SerializedName("goodsName")val goodsName:String,
    @SerializedName("goodsNumber")var goodsNumber:Int,
    @SerializedName("firstImage")val firstImage:String,
    @SerializedName("price")val price:Double,
    var isSelected:Boolean
): Serializable