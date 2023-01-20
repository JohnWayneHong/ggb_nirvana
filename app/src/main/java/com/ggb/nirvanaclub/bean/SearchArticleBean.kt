package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 搜索后的博客信息
 */
class SearchArticleBean (
    @SerializedName("blogs")val blogs:List<SearchArticleListBean>,
    @SerializedName("total")val total:Int,
    @SerializedName("pages")val pages:Int
): Serializable

class SearchArticleListBean(
    @SerializedName("blogId")val blogId:String,
    @SerializedName("readCount")val readCount:String,
    @SerializedName("title")val title:String,
    @SerializedName("introduction")val introduction:String
): Serializable

