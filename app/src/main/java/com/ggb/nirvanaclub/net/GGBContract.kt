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
        var VERIFYCODE = "verifyCode"
        var INFO = "info"
        var ARTICLEINFO = "articleInfo"
        var ARTICLELIKEINFO = "articleLikeInfo"
        var ARTICLECOLLECTIONINFO = "articleCollectionInfo"
        var ARTICLE = "article"
        var ARTICLEHISTORY = "articleHistory"
        var DELETESINGLEARTICLEHISTORY = "deleteSingleArticleHistory"
        var DELETEALLARTICLEHISTORY = "deleteAllArticleHistory"
        var CHECKACCOUNTISREGISTER = "checkAccountIsRegister"
        var SAVEUSERTAGS = "saveUserTags"
        var SEARCHARTICLEBYTIME = "searchArticleByTime"
        var LIKEARTICLE = "likeArticle"
        var ADDARTICLETOCOLLECTION = "addArticleToCollection"
        var DELETEARTICLETOCOLLECTION = "deleteArticleToCollection"
        var DISLIKEARTICLE = "dislikeArticle"
        var GETRANDOMGIRL = "getRandomGirl"
        var GETJOKESTEXT = "getJokesText"
        var GETJOKESPICTURE = "getJokesPicture"
        var GETJOKESRECOMMENDSUBSCRIPT = "getJokesRecommendSubscript"
        var COMMUNITYANDROID = "communityAndroid"
        var COMMUNITYSQUARE = "communitySquare"
        var COMMUNITYKNOWLEDGE = "communityKnowledge"
        var COMMUNITYNAVIGATION = "communityNavigation"
        var COMMUNITYDAILYBANNER = "communityDailyBanner"
        var COMMUNITYDAILYVIDEO = "communityDailyVideo"
        var COMMUNITYDAILYVIDEOCONTENT = "communityDailyVideoContent"
        var GETPATCH = "getPatch"
    }

    interface View : BaseView

    interface Model{
        fun getTag():Observable<HttpResult<List<IndexTagBean>>>
        fun sendCode(account: String,type: String,nickname:String,password: String): Observable<HttpResult<Any>>
        fun verifyCode(account: String,code: String): Observable<HttpResult<Any>>
        fun login(account:String,password:String):Observable<HttpResult<Any>>
        fun loginOut():Observable<HttpResult<Any>>
        fun info():Observable<HttpResult<SimpleUserInfo>>
        fun getTagAll():Observable<HttpResult<List<IndexTagBean>>>
        fun pageArticleByTag(tagId:String,page:Int,pageSize:Int):Observable<HttpResult<List<IndexArticleInfoBean>>>
        fun pageArticleByLike(page:Int,pageSize:Int):Observable<HttpResult<List<ArticleLikeInfoBean>>>
        fun pageArticleByCollection(page:Int,pageSize:Int):Observable<HttpResult<List<ArticleCollectionInfoBean>>>
        fun getArticle(articleId:String):Observable<HttpResult<ArticleContentBean>>
        fun saveUserTags(tagId: String):Observable<HttpResult<Any>>
        fun searchArticle(pager:Int,query:String,size:Int):Observable<HttpResult<List<SearchArticleBean>>>
        fun likeArticle(type:Int,targetId: String,receiver: String,chain: String):Observable<HttpResult<Any>>
        fun dislikeArticle(type:Int,targetId: String,receiver: String):Observable<HttpResult<Any>>
        fun addArticleToCollection(blogId:String):Observable<HttpResult<Any>>
        fun deleteArticleToCollection(blogId:String):Observable<HttpResult<Any>>
        fun getArticleHistory(page:Int,size: Int):Observable<HttpResult<List<ArticleHistoryBean>>>
        fun deleteSingleArticleHistory(blogId:String):Observable<HttpResult<Any>>
        fun deleteAllArticleHistory():Observable<HttpResult<Any>>
        fun checkAccountIsRegister(account:String):Observable<HttpResult<RegisterCheckBean>>
        fun developGetRandomGirl():Observable<ThirdHttpResult<List<DevelopRandomGirlListBean>>>
        fun communityText():Observable<ThirdHttpResult<List<DevelopJokesListBean>>>
        fun communityPicture():Observable<ThirdHttpResult<List<DevelopJokesListBean>>>
        fun communityRecommendSubscript():Observable<ThirdHttpResult<List<DevelopJokesSubscriptListBean>>>
        fun communityAndroid(pager: Int):Observable<PlayAndroidHttpResult<CommunityAndroidBean>>
        fun communitySquare(pager: Int):Observable<PlayAndroidHttpResult<CommunityAndroidBean>>
        fun communityKnowledge():Observable<PlayAndroidHttpResult<List<CommunityKnowledgeBean>>>
        fun communityNavigation():Observable<PlayAndroidHttpResult<List<CommunityNavigationBean>>>
        fun communityDailyBanner():Observable<CommunityDailyBean>
        fun communityDailyVideo(nextPage:String):Observable<CommunityDailyBean>
        fun communityDailyVideoContent(videoId: Int):Observable<CommunityDailyIssueBean>
        fun getIsHavePatch(versionName: String,versionCode: String):Observable<HttpResult<AppUpdateListBean>>
    }

    interface Present{
        fun getTag()
        fun sendCode(account: String,type: String,nickname:String,password: String)
        fun verifyCode(account: String,code: String)
        fun login(account:String,password:String)
        fun loginOut()
        fun info()
        fun getTagAll()
        fun pageArticleByTag(tagId:String,page:Int,pageSize:Int)
        fun pageArticleByLike(page:Int,pageSize:Int)
        fun pageArticleByCollection(page:Int,pageSize:Int)
        fun getArticle(articleId:String)
        fun saveUserTags(tagId: String)
        fun searchArticle(pager:Int,query:String,size:Int)
        fun likeArticle(type:Int,targetId: String,receiver: String,chain: String)
        fun dislikeArticle(type:Int,targetId: String,receiver: String)
        fun addArticleToCollection(blogId: String)
        fun deleteArticleToCollection(blogId: String)
        fun getArticleHistory(page:Int,size: Int)
        fun deleteSingleArticleHistory(blogId:String)
        fun deleteAllArticleHistory()
        fun checkAccountIsRegister(account:String)
        fun developGetRandomGirl()
        fun communityText()
        fun communityPicture()
        fun communityRecommendSubscript()
        fun communityAndroid(pager: Int)
        fun communitySquare(pager: Int)
        fun communityKnowledge()
        fun communityNavigation()
        fun communityDailyBanner()
        fun communityDailyVideo(nextPage:String)
        fun communityDailyVideoContent(videoId: Int)
        fun getIsHavePatch(versionName: String,versionCode: String)

    }
}