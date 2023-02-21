package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.base.BaseView
import com.ggb.nirvanaclub.bean.*
import io.reactivex.Observable


interface GGBContract {

    companion object{
        var GETTAG = "getTag"
        var GETTAGALL = "getTagAll"
        var LOGIN = "login"
        var LOGINOUT = "loginOut"
        var SENDCODE = "sendCode"
        var INFO = "info"
        var ARTICLEINFO = "articleInfo"
        var ARTICLE = "article"
        var SAVEUSERTAGS = "saveUserTags"
        var SEARCHARTICLEBYTIME = "searchArticleByTime"
        var LIKEORCANCEL = "likeOrCancel"
        var GETRANDOMGIRL = "getRandomGirl"
        var GETJOKESTEXT = "getJokesText"
        var GETJOKESPICTURE = "getJokesPicture"
        var GETJOKESRECOMMENDSUBSCRIPT = "getJokesRecommendSubscript"
    }

    interface View : BaseView

    interface Model{
        fun getTag():Observable<HttpResult<List<IndexTagBean>>>
        fun sendCode(phone:String): Observable<HttpResult<SimpleUserInfo>>
        fun login(account:String,password:String):Observable<HttpResult<Any>>
        fun loginOut():Observable<HttpResult<Any>>
        fun info():Observable<HttpResult<SimpleUserInfo>>
        fun getTagAll():Observable<HttpResult<List<IndexTagBean>>>
        fun pageArticleByTag(tagId:String,page:Int,pageSize:Int):Observable<HttpResult<IndexArticleInfoBean>>
        fun getArticle(articleId:String):Observable<HttpResult<ArticleContentBean>>
        fun saveUserTags(tagId: String):Observable<HttpResult<Any>>
        fun searchArticleByTime(pager:Int,query:String,other:String):Observable<HttpResult<SearchArticleBean>>
        fun likeOrCancelArticle(articleId:String,amILike:Boolean):Observable<HttpResult<Any>>
        fun developGetRandomGirl():Observable<ThirdHttpResult<List<DevelopRandomGirlListBean>>>
        fun communityText():Observable<ThirdHttpResult<List<DevelopJokesListBean>>>
        fun communityPicture():Observable<ThirdHttpResult<List<DevelopJokesListBean>>>
        fun communityRecommendSubscript():Observable<ThirdHttpResult<List<DevelopJokesSubscriptListBean>>>
    }

    interface Present{
        fun getTag()
        fun sendCode(phone:String)
        fun login(account:String,password:String)
        fun loginOut()
        fun info()
        fun getTagAll()
        fun pageArticleByTag(tagId:String,page:Int,pageSize:Int)
        fun getArticle(articleId:String)
        fun saveUserTags(tagId: String)
        fun searchArticleByTime(pager:Int,query:String,other:String)
        fun likeOrCancelArticle(articleId:String,amILike:Boolean)
        fun developGetRandomGirl()
        fun communityText()
        fun communityPicture()
        fun communityRecommendSubscript()

    }
}