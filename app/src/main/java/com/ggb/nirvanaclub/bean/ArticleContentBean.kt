package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 博客的全部内容
 */

class ArticleContentBean(
    @SerializedName("articleId")val articleId:String,
    @SerializedName("authId")val authId:String,
    @SerializedName("authName")val authName:String,
    @SerializedName("authAvatar")val authAvatar:String,
    @SerializedName("title")val title:String,
    @SerializedName("readCount")val readCount:Long,
    @SerializedName("commentCount")val commentCount:Long,
    @SerializedName("contentMd")val contentMd:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("likeCount")val likeCount:Long,
    @SerializedName("amILike")val amILike:Boolean
): Serializable

