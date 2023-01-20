package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.base.BaseView
import com.ggb.nirvanaclub.bean.*
import io.reactivex.Observable


interface GGBContract {

    companion object{
        var GETTAG = "getTag"
        var GETTAGALL = "getTagAll"
        var LOGIN = "login"
        var SENDCODE = "sendCode"
        var INFO = "info"
        var ARTICLEINFO = "articleInfo"
        var ARTICLE = "article"
        var SAVEUSERTAGS = "saveUserTags"
        var SEARCHARTICLEBYTIME = "searchArticleByTime"
        var LIKEORCANCEL = "likeOrCancel"
    }

    interface View : BaseView

    interface Model{
        fun getTag():Observable<HttpResult<List<IndexTagBean>>>
        fun sendCode(phone:String): Observable<HttpResult<SimpleUserInfo>>
        fun login(account:String,password:String,type:Int):Observable<HttpResult<Any>>
        fun info(type:Int):Observable<HttpResult<SimpleUserInfo>>
        fun getTagAll():Observable<HttpResult<List<IndexTagBean>>>
        fun pageArticleByTag(tagId:String,page:Int,pageSize:Int):Observable<HttpResult<IndexArticleInfoBean>>
        fun getArticle(articleId:String):Observable<HttpResult<ArticleContentBean>>
        fun saveUserTags(tagId: String):Observable<HttpResult<Any>>
        fun searchArticleByTime(pager:Int,query:String,other:String):Observable<HttpResult<SearchArticleBean>>
        fun likeOrCancelArticle(articleId:String,amILike:Boolean):Observable<HttpResult<Any>>
    }

    interface Present{
        fun getTag()
        fun sendCode(phone:String)
        fun login(account:String,password:String,type:Int)
        fun info(type:Int)
        fun getTagAll()
        fun pageArticleByTag(tagId:String,page:Int,pageSize:Int)
        fun getArticle(articleId:String)
        fun saveUserTags(tagId: String)
        fun searchArticleByTime(pager:Int,query:String,other:String)
        fun likeOrCancelArticle(articleId:String,amILike:Boolean)
    }
}