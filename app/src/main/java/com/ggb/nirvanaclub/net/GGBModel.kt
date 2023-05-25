package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.utils.RsaEncryptionUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class GGBModel :GGBContract.Model{

    override fun getTag(): Observable<HttpResult<List<IndexTagBean>>> {
        return Api.getDefault(0,0).getTag()
    }

    override fun sendCode(account: String,type: String,nickname:String,password: String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("account", account)
            jsonObject.put("type", type)
            jsonObject.put("nickName", nickname)
            jsonObject.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).sendCode(body)
    }

    override fun verifyCode(account: String,code: String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("account", account)
            jsonObject.put("code", code)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).verifyCode(body)
    }

    override fun login(account:String,password:String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            //新地址，密码需要加密传输
            jsonObject.put("account", account)
            jsonObject.put("password", RsaEncryptionUtils().rsaEncode(password))
            jsonObject.put("rememberMe", true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).login(body)

    }

    override fun loginOut(): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).loginOut(body)

    }

    override fun info(): Observable<HttpResult<SimpleUserInfo>> {
        return Api.getDefault(0,0).info()
    }

    override fun getTagAll(): Observable<HttpResult<List<IndexTagBean>>> {
        return Api.getDefault().getTagAll()
    }

    override fun pageArticleByTag(tagId: String, page: Int, pageSize: Int): Observable<HttpResult<List<IndexArticleInfoBean>>> {
        return Api.getDefault(0,0).pageArticleByTag(tagId,page,pageSize)
    }

    override fun pageArticleByLike(page: Int, pageSize: Int): Observable<HttpResult<List<ArticleLikeInfoBean>>> {
        return Api.getDefault(0,0).pageArticleByLike(page,pageSize)
    }

    override fun pageArticleByCollection(page: Int, pageSize: Int): Observable<HttpResult<List<ArticleCollectionInfoBean>>> {
        return Api.getDefault(0,0).pageArticleByCollection(page,pageSize)
    }

    override fun getArticle(articleId: String): Observable<HttpResult<ArticleContentBean>> {
        return Api.getDefault(0,0).getArticle(articleId)
    }

    override fun saveUserTags(tagId: String): Observable<HttpResult<Any>> {
        return Api.getDefault().saveUserTags(tagId)
    }

    override fun searchArticle(pager:Int,query:String,size:Int): Observable<HttpResult<List<SearchArticleBean>>> {
        return Api.getDefault(0,0).searchArticle(pager,query,size)
    }

    override fun likeArticle(type:Int,targetId: String,receiver: String,chain: String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("type", type)
            jsonObject.put("targetId", targetId)
            jsonObject.put("receiver", receiver)
            jsonObject.put("chain", chain)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).likeArticle(body)
    }

    override fun dislikeArticle(type:Int,targetId: String,receiver: String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("type", type)
            jsonObject.put("targetId", targetId)
            jsonObject.put("receiver", receiver)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).dislikeArticle(body)
    }

    override fun addArticleToCollection(blogId:String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", blogId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).addArticleToCollection(body)
    }

    override fun deleteArticleToCollection(blogId:String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", blogId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).deleteArticleToCollection(body)
    }

    override fun getArticleHistory(page:Int,size: Int): Observable<HttpResult<List<ArticleHistoryBean>>> {
        return Api.getDefault(0,0).getArticleHistory(page, size)
    }

    override fun deleteSingleArticleHistory(blogId:String): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", blogId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).deleteSingleArticleHistory(body)
    }

    override fun deleteAllArticleHistory(): Observable<HttpResult<Any>> {
        return Api.getDefault(0,0).deleteAllArticleHistory()
    }

    override fun checkAccountIsRegister(account:String): Observable<HttpResult<RegisterCheckBean>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("account", account)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault(0,0).checkAccountIsRegister(body)
    }

    override fun developGetRandomGirl(): Observable<ThirdHttpResult<List<DevelopRandomGirlListBean>>> {
        return Api.getDefault(3,3).developGetRandomGirl()
    }

    override fun communityText(): Observable<ThirdHttpResult<List<DevelopJokesListBean>>> {
        return Api.getDefault(3,3).communityText()
    }

    override fun communityPicture(): Observable<ThirdHttpResult<List<DevelopJokesListBean>>> {
        return Api.getDefault(3,3).communityPicture()
    }

    override fun communityRecommendSubscript(): Observable<ThirdHttpResult<List<DevelopJokesSubscriptListBean>>> {
        return Api.getDefault(3,3).communityRecommendSubscript()
    }

    override fun communityAndroid(pager: Int): Observable<PlayAndroidHttpResult<CommunityAndroidBean>> {
        return Api.getDefault(4,4).communityAndroid(pager)
    }

    override fun communitySquare(pager: Int): Observable<PlayAndroidHttpResult<CommunityAndroidBean>> {
        return Api.getDefault(4,4).communitySquare(pager)
    }

    override fun communityKnowledge(): Observable<PlayAndroidHttpResult<List<CommunityKnowledgeBean>>> {
        return Api.getDefault(4,4).communityKnowledgeTree()
    }

    override fun communityNavigation(): Observable<PlayAndroidHttpResult<List<CommunityNavigationBean>>> {
        return Api.getDefault(4,4).communityNavigation()
    }

    override fun communityDailyBanner(): Observable<CommunityDailyBean> {
        return Api.getDefault(5,5).communityDailyBanner()
    }

    override fun communityDailyVideo(nextPage:String): Observable<CommunityDailyBean> {
        return Api.getDefault(5,5).communityDailyVideo(nextPage)
    }

    override fun communityDailyVideoContent(videoId:Int): Observable<CommunityDailyIssueBean> {
        return Api.getDefault(5,5).communityDailyVideoContent(videoId)
    }

    override fun getIsHavePatch(versionName: String,versionCode: String): Observable<HttpResult<AppUpdateListBean>> {
        return Api.getDefault(0,0).getIsHavePatch(versionName,versionCode)
    }

}