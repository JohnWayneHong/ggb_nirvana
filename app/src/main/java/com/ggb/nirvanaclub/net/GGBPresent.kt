package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.base.BasePresent
import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.rx.RxSchedulersHelper
import retrofit2.HttpException

class GGBPresent(baseView: GGBContract.View) :BasePresent<GGBContract.View>(baseView),GGBContract.Present{

    private val model = GGBModel()

    override fun getTag() {
        model.getTag()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<List<IndexTagBean>>(mContext,true){
                override fun onSuccess(t: HttpResult<List<IndexTagBean>>?) {
                    view.onSuccess(GGBContract.GETTAG,t?.data)
                }

                override fun onCodeError(t: HttpResult<List<IndexTagBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun sendCode(phone:String) {
        model.sendCode(phone)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<SimpleUserInfo>(mContext,true){
                override fun onSuccess(t: HttpResult<SimpleUserInfo>?) {
                    view.onSuccess(GGBContract.SENDCODE,t?.data)
                }
                override fun onCodeError(t: HttpResult<SimpleUserInfo>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun login(account: String, password: String) {
        model.login(account,password)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<Any>(mContext,true){
                override fun onSuccess(t: HttpResult<Any>?) {
                    view.onSuccess(GGBContract.LOGIN,t?.data)
                }

                override fun onCodeError(t: HttpResult<Any>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                    //处理Retrofit中如何取得状态码非200（出现错误）
                    if (e is HttpException){
                        val body = e.response().errorBody()
                        if (body != null) {
                            view.onFailed(body.string(),false)
                        }
                    }else{
                        view.onNetError(isNetWorkError,false)
                    }
                }
            })
    }

    override fun loginOut() {
        model.loginOut()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<Any>(mContext,true){
                override fun onSuccess(t: HttpResult<Any>?) {
                    view.onSuccess(GGBContract.LOGINOUT,t?.data)
                }

                override fun onCodeError(t: HttpResult<Any>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                    //处理Retrofit中如何取得状态码非200（出现错误）
                    if (e is HttpException){
                        val body = e.response().errorBody()
                        if (body != null) {
                            view.onFailed(body.string(),false)
                        }
                    }else{
                        view.onNetError(isNetWorkError,false)
                    }
                }
            })
    }

    override fun info() {
        model.info()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<SimpleUserInfo>(mContext,true){
                override fun onSuccess(t: HttpResult<SimpleUserInfo>?) {
                    view.onSuccess(GGBContract.INFO,t?.data)
                }

                override fun onCodeError(t: HttpResult<SimpleUserInfo>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getTagAll() {
        model.getTagAll()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<List<IndexTagBean>>(mContext,true){
                override fun onSuccess(t: HttpResult<List<IndexTagBean>>?) {
                    view.onSuccess(GGBContract.GETTAGALL,t?.data)
                }

                override fun onCodeError(t: HttpResult<List<IndexTagBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun pageArticleByTag(tagId: String, page: Int, pageSize: Int) {
        model.pageArticleByTag(tagId, page, pageSize)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<IndexArticleInfoBean>(mContext,true){
                override fun onSuccess(t: HttpResult<IndexArticleInfoBean>?) {
                    view.onSuccess(GGBContract.ARTICLEINFO,t?.data)
                }

                override fun onCodeError(t: HttpResult<IndexArticleInfoBean>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getArticle(articleId: String) {
        model.getArticle(articleId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<ArticleContentBean>(mContext,true){
                override fun onSuccess(t: HttpResult<ArticleContentBean>?) {
                    view.onSuccess(GGBContract.ARTICLE,t?.data)
                }

                override fun onCodeError(t: HttpResult<ArticleContentBean>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun saveUserTags(tagId: String) {
        model.saveUserTags(tagId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<Any>(mContext,true){
                override fun onSuccess(t: HttpResult<Any>?) {
                    view.onSuccess(GGBContract.SAVEUSERTAGS,t?.data)
                }

                override fun onCodeError(t: HttpResult<Any>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun searchArticleByTime(pager:Int,query:String,other:String) {
        model.searchArticleByTime(pager, query, other)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<SearchArticleBean>(mContext,true){
                override fun onSuccess(t: HttpResult<SearchArticleBean>?) {
                    view.onSuccess(GGBContract.SEARCHARTICLEBYTIME,t?.data)
                }

                override fun onCodeError(t: HttpResult<SearchArticleBean>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun likeOrCancelArticle(articleId: String,amILike:Boolean) {
        model.likeOrCancelArticle(articleId,amILike)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :BaseObserver<Any>(mContext,true){
                override fun onSuccess(t: HttpResult<Any>?) {
                    view.onSuccess(GGBContract.LIKEORCANCEL,t?.data)
                }

                override fun onCodeError(t: HttpResult<Any>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun developGetRandomGirl() {
        model.developGetRandomGirl()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :ThirdBaseObserver<List<DevelopRandomGirlListBean>>(mContext,true){
                override fun onSuccess(t: ThirdHttpResult<List<DevelopRandomGirlListBean>>?) {
                    view.onSuccess(GGBContract.GETRANDOMGIRL,t?.data)
                }

                override fun onCodeError(t: ThirdHttpResult<List<DevelopRandomGirlListBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityText() {
        model.communityText()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :ThirdBaseObserver<List<DevelopJokesListBean>>(mContext,true){
                override fun onSuccess(t: ThirdHttpResult<List<DevelopJokesListBean>>?) {
                    view.onSuccess(GGBContract.GETJOKESTEXT,t?.data)
                }

                override fun onCodeError(t: ThirdHttpResult<List<DevelopJokesListBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityPicture() {
        model.communityPicture()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :ThirdBaseObserver<List<DevelopJokesListBean>>(mContext,true){
                override fun onSuccess(t: ThirdHttpResult<List<DevelopJokesListBean>>?) {
                    view.onSuccess(GGBContract.GETJOKESPICTURE,t?.data)
                }

                override fun onCodeError(t: ThirdHttpResult<List<DevelopJokesListBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityRecommendSubscript() {
        model.communityRecommendSubscript()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :ThirdBaseObserver<List<DevelopJokesSubscriptListBean>>(mContext,true){
                override fun onSuccess(t: ThirdHttpResult<List<DevelopJokesSubscriptListBean>>?) {
                    view.onSuccess(GGBContract.GETJOKESRECOMMENDSUBSCRIPT,t?.data)
                }

                override fun onCodeError(t: ThirdHttpResult<List<DevelopJokesSubscriptListBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityAndroid(pager: Int) {
        model.communityAndroid(pager)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :PlayAndroidBaseObserver<CommunityAndroidBean>(mContext,true){
                override fun onSuccess(t: PlayAndroidHttpResult<CommunityAndroidBean>?) {
                    view.onSuccess(GGBContract.COMMUNITYANDROID,t?.data)
                }

                override fun onCodeError(t: PlayAndroidHttpResult<CommunityAndroidBean>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communitySquare(pager: Int) {
        model.communitySquare(pager)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :PlayAndroidBaseObserver<CommunityAndroidBean>(mContext,true){
                override fun onSuccess(t: PlayAndroidHttpResult<CommunityAndroidBean>?) {
                    view.onSuccess(GGBContract.COMMUNITYSQUARE,t?.data)
                }

                override fun onCodeError(t: PlayAndroidHttpResult<CommunityAndroidBean>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityKnowledge() {
        model.communityKnowledge()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :PlayAndroidBaseObserver<List<CommunityKnowledgeBean>>(mContext,true){
                override fun onSuccess(t: PlayAndroidHttpResult<List<CommunityKnowledgeBean>>?) {
                    view.onSuccess(GGBContract.COMMUNITYKNOWLEDGE,t?.data)
                }

                override fun onCodeError(t: PlayAndroidHttpResult<List<CommunityKnowledgeBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityNavigation() {
        model.communityNavigation()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :PlayAndroidBaseObserver<List<CommunityNavigationBean>>(mContext,true){
                override fun onSuccess(t: PlayAndroidHttpResult<List<CommunityNavigationBean>>?) {
                    view.onSuccess(GGBContract.COMMUNITYNAVIGATION,t?.data)
                }

                override fun onCodeError(t: PlayAndroidHttpResult<List<CommunityNavigationBean>>) {
                    view.onFailed(t.message,false)
                }

                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun communityDailyBanner() {
        model.communityDailyBanner()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :OpenEyesBaseObserver<CommunityDailyBean>(mContext,true){
                override fun onSuccess(t: CommunityDailyBean?) {
                    view.onSuccess(GGBContract.COMMUNITYDAILYBANNER,t)
                }
            })
    }

    override fun communityDailyVideo(nextPage:String) {
        model.communityDailyVideo(nextPage)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :OpenEyesBaseObserver<CommunityDailyBean>(mContext,true){
                override fun onSuccess(t: CommunityDailyBean?) {
                    view.onSuccess(GGBContract.COMMUNITYDAILYVIDEO,t)
                }
            })
    }

    override fun communityDailyVideoContent(videoId: Int) {
        model.communityDailyVideoContent(videoId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object :OpenEyesBaseObserver<CommunityDailyIssueBean>(mContext,true){
                override fun onSuccess(t: CommunityDailyIssueBean?) {
                    view.onSuccess(GGBContract.COMMUNITYDAILYVIDEOCONTENT,t)
                }
            })
    }

}