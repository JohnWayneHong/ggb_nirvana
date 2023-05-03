package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 获取用户收藏博客的返回字段
 */
class ArticleLikeInfoBean (
    @SerializedName("likeTime")val likeTime:String,
    @SerializedName("blog")val blog:ArticleCollectionInfoBolgBean?
): Serializable

