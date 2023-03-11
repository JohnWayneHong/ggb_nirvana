package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //获取用户标签
    @GET("main/tag/user")
    fun getTag():Observable<HttpResult<List<IndexTagBean>>>

    //用户登录发送验证码
    @POST("sys/phoneCode")
    fun sendCode(@Body phone : String):Observable<HttpResult<SimpleUserInfo>>

    //用户登出
    @POST("/v2/api/user/login")
    fun login(@Body body: RequestBody):Observable<HttpResult<Any>>

    //用户登录
    @POST("/v2/api/user/logout")
    fun loginOut(@Body body: RequestBody):Observable<HttpResult<Any>>

    //获取基础用户信息
    @GET("/v2/api/user/myinfo")
    fun info() : Observable<HttpResult<SimpleUserInfo>>

    //查询所有的标签
    @GET("main/tag/all")
    fun getTagAll(): Observable<HttpResult<List<IndexTagBean>>>

    //保存用户订阅标签
    @POST("main/tag/user/save")
    fun saveUserTags(@Body tagIds: String): Observable<HttpResult<Any>>

    /**
     * 分页查询某个标签的文章
     * @param tagId 查询对应标签的文章
     * @param page 查询第几页
     * @param pageSize 查几条数据
     */
    @GET("main/article/info/{tagId}/{page}")
    fun pageArticleByTag(@Path("tagId") tagId: String, @Path("page") page: Int, @Query("pageSize") pageSize: Int): Observable<HttpResult<IndexArticleInfoBean>>

    //获取文章的内容
    @GET("main/article/{articleId}")
    fun getArticle(@Path("articleId") articleId: String): Observable<HttpResult<ArticleContentBean>>

    //获取文章的内容
//    @GET("main/getBlogByPage/{currentPage}/{query}/{order}")
    @GET("main/getBlogByPage")
    fun searchArticleByTime(@Query("currentPage") currentPage: Int,@Query("query") query: String,@Query("order") order: String): Observable<HttpResult<SearchArticleBean>>

    //点赞或取消点赞文章
    @PUT("main/article/like/{articleId}")
    fun likeOrCancel(@Path("articleId") articleId: String, @Body order: Map<String, String>): Observable<HttpResult<Any>>

    //获取随机美女图片
    @GET("image/girl/list/random")
    fun developGetRandomGirl(): Observable<ThirdHttpResult<List<DevelopRandomGirlListBean>>>

    //获取随机纯文字
    @POST("home/text")
    fun communityText(): Observable<ThirdHttpResult<List<DevelopJokesListBean>>>

    //获取随机纯文字
    @POST("home/pic")
    fun communityPicture(): Observable<ThirdHttpResult<List<DevelopJokesListBean>>>

    //获取主页的推荐关注数据
    @POST("home/attention/recommend")
    fun communityRecommendSubscript(): Observable<ThirdHttpResult<List<DevelopJokesSubscriptListBean>>>

    //获取主页的推荐关注数据
    @GET("article/list/{pager}/json")
    fun communityAndroid(@Path("pager") page :Int): Observable<PlayAndroidHttpResult<CommunityAndroidBean>>

    //获取主页的推荐关注数据
    @GET("user_article/list/{pager}/json")
    fun communitySquare(@Path("pager") page :Int): Observable<PlayAndroidHttpResult<CommunityAndroidBean>>

    /**
     * 获取知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    fun communityKnowledgeTree(): Observable<PlayAndroidHttpResult<List<CommunityKnowledgeBean>>>

    /**
     * 导航数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("navi/json")
    fun communityNavigation(): Observable<PlayAndroidHttpResult<List<CommunityNavigationBean>>>

    /**
     * 开眼视频Banner数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("v2/feed?num=1")
    fun communityDailyBanner(): Observable<CommunityDailyBean>

    /**
     * 开眼视频往期视频列表数据
     * http://www.wanandroid.com/navi/json
     */
    @GET
    fun communityDailyVideo(@Url url: String): Observable<CommunityDailyBean>

    /**
     * 开眼视频视频详情数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("v4/video/related")
    fun communityDailyVideoContent(@Query("id") id: Int): Observable<CommunityDailyIssueBean>

}