package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 博客的全部内容
 */

class ArticleHistoryBean(
    @SerializedName("id")val id:String,
    @SerializedName("viewTime")val viewTime:String,
    @SerializedName("blog")val blog:IndexArticleInfoBean
): Serializable

