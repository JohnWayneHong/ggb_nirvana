package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.base.BasePresent
import com.ggb.nirvanaclub.bean.*
import com.ggb.nirvanaclub.rx.RxSchedulersHelper

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

    override fun login(account: String, password: String, type: Int) {
        model.login(account,password, type)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<Any>(mContext,true){
                override fun onSuccess(t: HttpResult<Any>?) {
                    view.onSuccess(GGBContract.LOGIN,t?.data)
                }

                override fun onCodeError(t: HttpResult<Any>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun info(type: Int) {
        model.info(type)
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

}