package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 获取用户收藏博客的返回字段
 */
class ArticleCollectionInfoBean (
    @SerializedName("favoriteTime")val favoriteTime:String,
    @SerializedName("blog")val blog:ArticleCollectionInfoBolgBean?
): Serializable

class ArticleCollectionInfoBolgBean(
    @SerializedName("id")val id:String,
    @SerializedName("authId")val authId:String,
    @SerializedName("readCount")val readCount:Long,
    @SerializedName("likeCount")val likeCount:Long,
    @SerializedName("commentCount")val commentCount:Long,
    @SerializedName("favoriteCount")val favoriteCount:Long,
    @SerializedName("title")val title:String?="",
    @SerializedName("introduction")val introduction:String?="",
    @SerializedName("img")val img:String?="",
    @SerializedName("authName")val authName:String,
    @SerializedName("authPhoto")val authPhoto:String
): Serializable

