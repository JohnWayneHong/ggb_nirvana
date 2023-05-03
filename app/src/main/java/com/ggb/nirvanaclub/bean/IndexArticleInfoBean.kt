package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 仅包含博客的重要信息，不包含博客的全部内容
 */
class IndexArticleInfoBean (
    @SerializedName("id")val id:String,
    @SerializedName("authId")val authId:String,
    @SerializedName("readCount")val readCount:Long,
    @SerializedName("likeCount")val likeCount:Long,
    @SerializedName("commentCount")val commentCount:Long,
    @SerializedName("favoriteCount")val favoriteCount:Long,
    @SerializedName("title")val title:String?="",
    @SerializedName("introduction")val introduction:String?="",
    @SerializedName("img")val img:String?="",
    @SerializedName("createTime")val createTime:String,
    @SerializedName("authName")val authName:String,
    @SerializedName("authPhoto")val authPhoto:String,
    @SerializedName("tags")val tags:List<IndexArticleInfoListBean>,
//    @SerializedName("list")val list:List<IndexArticleInfoListBean>,
//    @SerializedName("hasNext")val hasNext:Boolean,
): Serializable

class IndexArticleInfoListBean(
    @SerializedName("id")val id:String,
    @SerializedName("name")val name:String,
//    @SerializedName("authorName")val authorName:String,
//    @SerializedName("authorAvatar")val authorAvatar:String,
//    @SerializedName("title")val title:String,
//    @SerializedName("content")val content:String?,
//    @SerializedName("img1")val img1:String?="",
//    @SerializedName("img2")val img2:String?="",
//    @SerializedName("img3")val img3:String?="",
//    @SerializedName("readCount")val readCount:String,
//    @SerializedName("commentCount")val commentCount:String,
//    @SerializedName("likeCount")val likeCount:String
): Serializable

