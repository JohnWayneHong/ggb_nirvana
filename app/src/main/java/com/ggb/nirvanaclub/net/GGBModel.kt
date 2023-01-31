package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.constans.C
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class GGBModel :GGBContract.Model{

    override fun getTag(): Observable<HttpResult<List<IndexTagBean>>> {
        return Api.getDefault().getTag()
    }

    override fun sendCode(phone: String): Observable<HttpResult<SimpleUserInfo>> {
        return Api.getDefault().sendCode(phone)
    }

    override fun login(account:String,password:String,type:Int): Observable<HttpResult<Any>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("account", account)
            jsonObject.put("password", password)
            jsonObject.put("type", type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().login(body)
    }

    override fun info(type: Int): Observable<HttpResult<SimpleUserInfo>> {
        return Api.getDefault().info(type)
    }

    override fun getTagAll(): Observable<HttpResult<List<IndexTagBean>>> {
        return Api.getDefault().getTagAll()
    }

    override fun pageArticleByTag(tagId: String, page: Int, pageSize: Int): Observable<HttpResult<IndexArticleInfoBean>> {
        return Api.getDefault().pageArticleByTag(tagId,page,pageSize)
    }

    override fun getArticle(articleId: String): Observable<HttpResult<ArticleContentBean>> {
        return Api.getDefault().getArticle(articleId)
    }

    override fun saveUserTags(tagId: String): Observable<HttpResult<Any>> {
        return Api.getDefault().saveUserTags(tagId)
    }

    override fun searchArticleByTime(pager:Int,query:String,other:String): Observable<HttpResult<SearchArticleBean>> {
        return Api.getDefault(2,1).searchArticleByTime(pager,query,other)
    }

    override fun likeOrCancelArticle(articleId: String,amILike:Boolean): Observable<HttpResult<Any>> {
        return Api.getDefault().likeOrCancel(articleId, mapOf(
            C.ARTICLE_LIKE_ORDER_KEY to
                    if (amILike) C.ARTICLE_LIKE_ON
                    else C.ARTICLE_LIKE_OFF
        ))
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

}