package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 玩安卓首页数据
 */
class CommunityAndroidBean (
    @SerializedName("curPage")val curPage:Int,
    @SerializedName("datas")val datas:MutableList<CommunityAndroidListBean>,
    @SerializedName("offset")val offset:Int,
    @SerializedName("over")val over:Boolean,
    @SerializedName("pageCount")val pageCount:Int,
    @SerializedName("size")val size:Int,
    @SerializedName("total")val total:Int

): Serializable

class CommunityAndroidListBean (
    @SerializedName("apkLink") val apkLink: String,
    @SerializedName("audit") val audit: Int,
    @SerializedName("author") val author: String,
    @SerializedName("chapterId") val chapterId: Int,
    @SerializedName("chapterName") val chapterName: String,
    @SerializedName("collect") var collect: Boolean,
    @SerializedName("courseId") val courseId: Int,
    @SerializedName("desc") val desc: String,
    @SerializedName("envelopePic") val envelopePic: String,
    @SerializedName("fresh") val fresh: Boolean,
    @SerializedName("id") val id: Int,
    @SerializedName("link") val link: String,
    @SerializedName("niceDate") val niceDate: String,
    @SerializedName("niceShareDate") val niceShareDate: String,
    @SerializedName("origin") val origin: String,
    @SerializedName("prefix") val prefix: String,
    @SerializedName("projectLink") val projectLink: String,
    @SerializedName("publishTime") val publishTime: Long,
    @SerializedName("shareDate") val shareDate: String,
    @SerializedName("shareUser") val shareUser: String,
    @SerializedName("superChapterId") val superChapterId: Int,
    @SerializedName("superChapterName") val superChapterName: String,
    @SerializedName("tags") val tags: MutableList<CommunityAndroidListTagBean>,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("visible") val visible: Int,
    @SerializedName("zan") val zan: Int,
    @SerializedName("top") var top: String
): Serializable

class CommunityAndroidListTagBean(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)



