package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 搜索后的博客信息
 */
class SearchArticleBean (
    @SerializedName("id")val id:String,
    @SerializedName("authId")val authId:String,
    @SerializedName("readCount")val readCount:Long,
    @SerializedName("likeCount")val likeCount:Long,
    @SerializedName("commentCount")val commentCount:Long,
    @SerializedName("favoriteCount")val favoriteCount:Long,
    @SerializedName("title")val title:String,
    @SerializedName("introduction")val introduction:String,
    @SerializedName("img")val img:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("authName")val authName:String,
    @SerializedName("tags")val tags:List<SearchArticleListBean>,
): Serializable

class SearchArticleListBean(
    @SerializedName("id")val id:String,
    @SerializedName("name")val name:String
): Serializable

