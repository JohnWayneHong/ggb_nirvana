package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 玩安卓知识体系数据
 */
class CommunityKnowledgeBean (
    @SerializedName("children") val children: MutableList<CommunityKnowledgeListBean>,
    @SerializedName("courseId") val courseId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("parentChapterId") val parentChapterId: Int,
    @SerializedName("visible") val visible: Int

): Serializable

class CommunityKnowledgeListBean(
    @SerializedName("children") val children: List<Any>,
    @SerializedName("courseId") val courseId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("parentChapterId") val parentChapterId: Int,
    @SerializedName("visible") val visible: Int
): Serializable




