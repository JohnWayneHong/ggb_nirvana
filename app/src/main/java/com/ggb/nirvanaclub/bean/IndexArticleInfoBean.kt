package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 仅包含博客的重要信息，不包含博客的全部内容
 */
class IndexArticleInfoBean (
    @SerializedName("list")val list:List<IndexArticleInfoListBean>,
    @SerializedName("hasNext")val hasNext:Boolean,
): Serializable

class IndexArticleInfoListBean(
    @SerializedName("articleId")val articleId:String,
    @SerializedName("authorId")val authorId:String,
    @SerializedName("authorName")val authorName:String,
    @SerializedName("authorAvatar")val authorAvatar:String,
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String?,
    @SerializedName("img1")val img1:String?="",
    @SerializedName("img2")val img2:String?="",
    @SerializedName("img3")val img3:String?="",
    @SerializedName("readCount")val readCount:String,
    @SerializedName("commentCount")val commentCount:String,
    @SerializedName("likeCount")val likeCount:String
): Serializable

