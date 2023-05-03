package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 博客的全部内容
 */

class ArticleContentBean(
    @SerializedName("id")val id:String,
    @SerializedName("authId")val authId:String,
    @SerializedName("readCount")val readCount:Long,
    @SerializedName("likeCount")val likeCount:Long,
    @SerializedName("commentCount")val commentCount:Long,
    @SerializedName("favoriteCount")val favoriteCount:Long,
    @SerializedName("title")val title:String,
    @SerializedName("introduction")val introduction:String,
    @SerializedName("contentMd")val contentMd:String,
    @SerializedName("highlight")val highlight:String,
    @SerializedName("img")val img:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("hasLiked")val hasLiked:Boolean,
    @SerializedName("tags")val tags:List<ArticleContentTagListBean>,
    @SerializedName("authInfo")val authInfo:ArticleContentInfoListBean
): Serializable

class ArticleContentTagListBean(
    @SerializedName("id")val id:String,
    @SerializedName("name")val name:String
): Serializable

class ArticleContentInfoListBean(
    @SerializedName("id")val id:String,
    @SerializedName("nickName")val nickName:String,
    @SerializedName("photo")val photo:String,
    @SerializedName("publishCount")val publishCount:Long,
    @SerializedName("readCount")val readCount:Long,
    @SerializedName("likeCount")val likeCount:Long
): Serializable

