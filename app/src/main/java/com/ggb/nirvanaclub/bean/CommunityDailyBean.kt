package com.ggb.nirvanaclub.bean

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 开眼视频 日常实体类
 */

class CommunityDailyBean(
    @SerializedName("dialog") val dialog: Any,
    @SerializedName("issueList") val issueList: List<CommunityDailyIssueBean>,
//    @SerializedName("issueList") val issueList: JsonArray,
    @SerializedName("newestIssueType") val newestIssueType: String,
    @SerializedName("nextPageUrl") val nextPageUrl: String,
    @SerializedName("nextPublishTime") val nextPublishTime: Long
): Serializable

class CommunityDailyIssueBean(
    @SerializedName("total") val total:Int,
    @SerializedName("count") val count: Int,
    @SerializedName("date") val date: Long,
    @SerializedName("itemList") val itemList: MutableList<CommunityDailyItemBean>,
    @SerializedName("publishTime") val publishTime: Long,
    @SerializedName("releaseTime") val releaseTime: Long,
    @SerializedName("type") val type: String,
    @SerializedName("nextPageUrl") val nextPageUrl: String
): Serializable

class CommunityDailyItemBean(
    @SerializedName("adIndex") val adIndex: Int,
    @SerializedName("data") val data: CommunityDailyDataBean,
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("itemType") val itemType: Int
): Serializable

class CommunityDailyDataBean(
    @SerializedName("actionUrl") val actionUrl: String,
    @SerializedName("ad") val ad: Boolean,
    @SerializedName("author") val author: CommunityDailyAuthorBean,
    @SerializedName("owner") val owner: CommunityDailyOwnerBean?,
    @SerializedName("autoPlay") val autoPlay: Boolean,
    @SerializedName("category") val category: String,
    @SerializedName("collected") val collected: Boolean,
    @SerializedName("consumption") val consumption: CommunityDailyConsumptionBean,
    @SerializedName("cover") val cover: CommunityDailyCoverBean,
    @SerializedName("dataType") val dataType: String,
    @SerializedName("date") val date: Long,
    @SerializedName("description") val description: String,
    @SerializedName("text") val text: String,
    @SerializedName("descriptionEditor") val descriptionEditor: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("header") val header: CommunityDailyHeaderBean,
    @SerializedName("id") val id: Int,
    @SerializedName("idx") val idx: Int,
    @SerializedName("ifLimitVideo") val ifLimitVideo: Boolean,
    @SerializedName("image") val image: String,
    @SerializedName("library") val library: String,
    @SerializedName("playInfo") val playInfo: List<CommunityDailyPlayInfoBean>,
    @SerializedName("playUrl") val playUrl: String,
    @SerializedName("played") val played: Boolean,
    @SerializedName("provider") val provider: CommunityDailyProviderBean,
    @SerializedName("reallyCollected") val reallyCollected: Boolean,
    @SerializedName("releaseTime") val releaseTime: Long,
    @SerializedName("resourceType") val resourceType: String,
    @SerializedName("searchWeight") val searchWeight: Int,
    @SerializedName("shade") val shade: Boolean,
    @SerializedName("tags") val tags: List<CommunityDailyTagBean>,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("webUrl") val webUrl: CommunityDailyWebUrlBean,
    @SerializedName("itemList") val itemList: List<CommunityDailyItemBean>,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("urls") val urls: List<String>,
): Serializable

class CommunityDailyAuthorBean(
    @SerializedName("approvedNotReadyVideoCount") val approvedNotReadyVideoCount: Int,
    @SerializedName("description") val description: String,
    @SerializedName("expert") val expert: Boolean,
    @SerializedName("follow") val follow: CommunityDailyFollowBean,
    @SerializedName("icon") val icon: String,
    @SerializedName("id") val id: Int,
    @SerializedName("ifPgc") val ifPgc: Boolean,
    @SerializedName("latestReleaseTime") val latestReleaseTime: Long,
    @SerializedName("link") val link: String,
    @SerializedName("name") val name: String,
    @SerializedName("recSort") val recSort: Int,
    @SerializedName("shield") val shield: CommunityDailyShieldBean,
    @SerializedName("videoNum") val videoNum: Int
): Serializable

class CommunityDailyOwnerBean(
    @SerializedName("actionUrl") val actionUrl: String,
    @SerializedName("area") val area: Any,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("birthday") val birthday: Any,
    @SerializedName("city") val city: Any,
    @SerializedName("country") val country: Any,
    @SerializedName("cover") val cover: Any,
    @SerializedName("description") val description: String,
    @SerializedName("expert") val expert: Boolean,
    @SerializedName("followed") val followed: Boolean,
    @SerializedName("gender") val gender: Any,
    @SerializedName("ifPgc") val ifPgc: Boolean,
    @SerializedName("job") val job: Any,
    @SerializedName("library") val library: String,
    @SerializedName("limitVideoOpen") val limitVideoOpen: Boolean,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("registDate") val registDate: Long,
    @SerializedName("releaseDate") val releaseDate: Long,
    @SerializedName("uid") val uid: Int,
    @SerializedName("university") val university: Any,
    @SerializedName("userType") val userType: String
): Serializable

class CommunityDailyConsumptionBean(
    @SerializedName("collectionCount") val collectionCount: Int,
    @SerializedName("realCollectionCount") val realCollectionCount: Int,
    @SerializedName("replyCount") val replyCount: Int,
    @SerializedName("shareCount") val shareCount: Int
): Serializable

class CommunityDailyCoverBean(
    @SerializedName("blurred") val blurred: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("feed") val feed: String,
    @SerializedName("homepage") val homepage: String
): Serializable

class CommunityDailyHeaderBean(
    @SerializedName("actionUrl") val actionUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("expert") val expert: Boolean,
    @SerializedName("issuerName") val issuerName: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("iconType") val iconType: String,
    @SerializedName("id") val id: Int,
    @SerializedName("time") val time: Long,
    @SerializedName("ifPgc") val ifPgc: Boolean,
    @SerializedName("ifShowNotificationIcon") val ifShowNotificationIcon: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("uid") val uid: Int
): Serializable

class CommunityDailyPlayInfoBean(
    @SerializedName("height") val height: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlList") val urlList: List<CommunityDailyUrlBean>,
    @SerializedName("width") val width: Int
): Serializable

class CommunityDailyProviderBean(
    @SerializedName("alias") val alias: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("name") val name: String
): Serializable

class CommunityDailyTagBean(
    @SerializedName("actionUrl") val actionUrl: String,
    @SerializedName("bgPicture") val bgPicture: String,
    @SerializedName("communityIndex") val communityIndex: Int,
    @SerializedName("desc") val desc: String,
    @SerializedName("haveReward") val haveReward: Boolean,
    @SerializedName("headerImage") val headerImage: String,
    @SerializedName("id") val id: Int,
    @SerializedName("ifNewest") val ifNewest: Boolean,
    @SerializedName("name") val name: String,
    @SerializedName("tagRecType") val tagRecType: String
): Serializable

class CommunityDailyWebUrlBean(
    @SerializedName("forWeibo") val forWeibo: String,
    @SerializedName("raw") val raw: String
): Serializable

class CommunityDailyUrlBean(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
): Serializable

class CommunityDailyFollowBean(
    @SerializedName("followed") val followed: Boolean,
    @SerializedName("itemId") val itemId: Int,
    @SerializedName("itemType") val itemType: String
): Serializable

class CommunityDailyShieldBean(
    @SerializedName("itemId") val itemId: Int,
    @SerializedName("itemType") val itemType: String,
    @SerializedName("shielded") val shielded: Boolean
): Serializable




