package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 玩安卓导航体系数据
 */
class CommunityNavigationBean (
    @SerializedName("articles") val articles: MutableList<CommunityAndroidListBean>,
    @SerializedName("cid") val cid: Int,
    @SerializedName("name") val name: String
): Serializable





